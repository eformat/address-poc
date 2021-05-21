package org.acme.service;

import org.hibernate.search.backend.elasticsearch.client.spi.ElasticsearchHttpClientConfigurationContext;
import org.hibernate.search.backend.elasticsearch.client.spi.ElasticsearchHttpClientConfigurer;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

// FIXME not working, disabled https in ES for now
// FIXME need to figure out how to attach ssl, https://quarkus.io/guides/elasticsearch#programmatically-configuring-elasticsearch
public class MyElasticsearchHttpClientConfigurer implements ElasticsearchHttpClientConfigurer {

    @Override
    public void configure(ElasticsearchHttpClientConfigurationContext elasticsearchHttpClientConfigurationContext) {
        SSLContext sslContext;
        try {
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{ TrustAllX509ExtendedTrustManager.getInstance() }, null);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException();
        } catch (KeyManagementException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        elasticsearchHttpClientConfigurationContext.clientBuilder().setSSLContext(sslContext);
        elasticsearchHttpClientConfigurationContext.clientBuilder().setSSLHostnameVerifier((hosts, session) -> true);
    }
}
