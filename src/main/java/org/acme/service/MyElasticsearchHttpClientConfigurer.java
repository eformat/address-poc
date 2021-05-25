package org.acme.service;

import io.quarkus.elasticsearch.restclient.lowlevel.ElasticsearchClientConfig;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.hibernate.search.backend.elasticsearch.client.spi.ElasticsearchHttpClientConfigurationContext;
import org.hibernate.search.backend.elasticsearch.client.spi.ElasticsearchHttpClientConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;

@ElasticsearchClientConfig
public class MyElasticsearchHttpClientConfigurer implements RestClientBuilder.HttpClientConfigCallback {

    private final Logger log = LoggerFactory.getLogger(MyElasticsearchHttpClientConfigurer.class);

    @Override
    public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpAsyncClientBuilder) {
        RequestConfig requestConfig = RequestConfig.DEFAULT;
        log.debug(">>> requestConfig " + requestConfig);
        httpAsyncClientBuilder.setDefaultRequestConfig(requestConfig);
        return httpAsyncClientBuilder;
    }

//    @Override
//    public void configure(ElasticsearchHttpClientConfigurationContext elasticsearchHttpClientConfigurationContext) {
//        SSLContext sslContext;
//        try {
//            sslContext = SSLContext.getInstance("TLS");
//            sslContext.init(null, new TrustManager[]{ TrustAllX509ExtendedTrustManager.getInstance() }, null);
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//            throw new RuntimeException();
//        } catch (KeyManagementException e) {
//            e.printStackTrace();
//            throw new RuntimeException();
//        }
//        elasticsearchHttpClientConfigurationContext.clientBuilder().setSSLContext(sslContext);
//        elasticsearchHttpClientConfigurationContext.clientBuilder().setSSLHostnameVerifier((hosts, session) -> true);
//    }
}
