package com.gear.poi.search.es.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

@Slf4j
@Component
public class Config {

    @Autowired
    ElasticsearchProperties elasticsearchProperties;

    @Bean
    public ElasticsearchClient getRestClientByPassword() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        // URL and API key
        String serverUrl = elasticsearchProperties.getUris().get(0);
        String username = elasticsearchProperties.getUsername();
        String password = elasticsearchProperties.getPassword();

        final CredentialsProvider credentialsProvider =
                new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(username, password));

        SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, (arg0, arg1) -> true).build();
        RestClientBuilder builder = RestClient.builder(HttpHost.create(serverUrl))
                .setHttpClientConfigCallback(httpClientBuilder -> {
                    httpClientBuilder.setSSLContext(sslContext)
                            .setSSLHostnameVerifier((s, sslSession) -> true)
                            .setDefaultCredentialsProvider(credentialsProvider);
                    return httpClientBuilder;
                });
        try {
            RestClient restClient = builder.build();
            ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
            ElasticsearchClient client = new ElasticsearchClient(transport);
            if (client.ping().value()) {
                log.info("-----------------初始化 es 客户端成功！-----------------");
                return client;
            }
        } catch (Exception e) {
            throw new RuntimeException("初始化 es 客户端失败！", e);
        }
        return null;
    }
}
