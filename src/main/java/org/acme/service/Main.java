package org.acme.service;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.ingest.PutPipelineRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.*;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.PutIndexTemplateRequest;
import org.elasticsearch.common.bytes.BytesArray;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.IndexNotFoundException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@QuarkusMain
public class Main {

    public static void main(String... args) {
        // index template configuration
        String indexReplicas = System.getProperty("index.number_of_replicas");
        if (indexReplicas == null || indexReplicas.isEmpty()) {
            indexReplicas = "1";
        }
        String indexShards = System.getProperty("index.number_of_shards");
        if (indexShards == null || indexShards.isEmpty()) {
            indexShards = "1";
        }
        // ingest pipeline configuration
        String pipeline = null;
        try (InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("ingest-pipeline.json");
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            pipeline = reader.lines()
                    .collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        PutPipelineRequest pipelineRequest = new PutPipelineRequest(
                "address-default-pipeline",
                new BytesArray(pipeline.getBytes(StandardCharsets.UTF_8)),
                XContentType.JSON);

        // delete index configuration
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest("address-000001");

        // create index configuration
        String index = null;
        try (InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("index.json");
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            index = reader.lines()
                    .collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        CreateIndexRequest createIndexRequest = new CreateIndexRequest("address-000001");
        createIndexRequest.source(
                new BytesArray(index.getBytes(StandardCharsets.UTF_8)),
                XContentType.JSON
        );

        // index template configuration
        PutIndexTemplateRequest addressTemplate = new PutIndexTemplateRequest("address-template")
                .patterns(Arrays.asList("address-*"))
                .settings(Settings.builder()
                        .put("index.max_ngram_diff", 50)
                        .put("index.number_of_replicas", Integer.parseInt(indexReplicas))
                        .put("index.number_of_shards", Integer.parseInt(indexShards))
                        .put("index.default_pipeline", "address-default-pipeline")
                );
        // openshift hostport stuff
        Pattern hostport = Pattern.compile("^(.*):(\\d+)$");
        String elasticCluster = System.getProperty("quarkus.elasticsearch.hosts");
        if (elasticCluster == null || elasticCluster.isEmpty()) {
            elasticCluster = "localhost:9200";
        }
        Matcher matchAddress = hostport.matcher(elasticCluster);
        String host = new String();
        Integer port = 0;
        if (matchAddress.find()) {
            host = matchAddress.group(1);
            port = Integer.parseInt(matchAddress.group(2));
        }
        String elasticClusterUsername = System.getProperty("quarkus.elasticsearch.username");
        String elasticClusterPassword = System.getProperty("quarkus.elasticsearch.password");
        String elasticScheme = System.getProperty("quarkus.elasticsearch.protocol");
        RestClientBuilder builder;
        if (elasticScheme != null && !elasticScheme.isEmpty()) {
            builder = RestClient.builder(new HttpHost(host, port, elasticScheme));
        } else {
            builder = RestClient.builder(new HttpHost(host, port, "http"));
        }
        // ignore credentials and https if not set
        if (elasticClusterPassword != null && !elasticClusterPassword.isEmpty()
                && elasticClusterUsername != null && !elasticClusterUsername.isEmpty()
                && elasticScheme != null && !elasticScheme.isEmpty()) {

            CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY,
                    new UsernamePasswordCredentials(elasticClusterUsername, elasticClusterPassword));

            SSLContext sslContext;
            try {
                sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, new TrustManager[]{TrustAllX509ExtendedTrustManager.getInstance()}, null);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                throw new RuntimeException();
            } catch (KeyManagementException e) {
                e.printStackTrace();
                throw new RuntimeException();
            }
            builder.
                    setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
                        @Override
                        public HttpAsyncClientBuilder customizeHttpClient(
                                HttpAsyncClientBuilder httpClientBuilder) {
                            return httpClientBuilder
                                    .setDefaultCredentialsProvider(credentialsProvider)
                                    .setSSLContext(sslContext)
                                    .setSSLHostnameVerifier((host, session) -> true);
                        }
                    });
        }
        // delete and recreate
        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(builder);
        String recreateIndex = System.getProperty("index.recreate");
        try {
            AcknowledgedResponse pipelineReponse = restHighLevelClient.ingest().putPipeline(pipelineRequest, RequestOptions.DEFAULT);
            assert pipelineReponse.isAcknowledged();
            AcknowledgedResponse templateResponse = restHighLevelClient.indices().putTemplate(addressTemplate, RequestOptions.DEFAULT);
            assert templateResponse.isAcknowledged();
            if (recreateIndex != null && recreateIndex.equalsIgnoreCase("true")) {
                try {
                    restHighLevelClient.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
                } catch (Exception e) {
                    // fine
                }
                CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
                assert createIndexResponse.isAcknowledged();
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Couldn't connect to the elasticsearch server to create necessary templates. Ensure the Elasticsearch user has permissions to create templates.");
        }
        Quarkus.run(MyApp.class, args);
    }

    public static class MyApp implements QuarkusApplication {
        @Override
        public int run(String... args) {
            Quarkus.waitForExit();
            return 0;
        }
    }
}
