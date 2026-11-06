package com.dchealth.elasticsearch.config;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
@Configuration
public class ElasticSearchConfig {
    private final ElasticSearchConfigProperties elasticSearchConfigProperties;

    public ElasticSearchConfig(ElasticSearchConfigProperties elasticSearchConfigProperties) {
        this.elasticSearchConfigProperties = elasticSearchConfigProperties;
    }

    @Bean
    public RestClientBuilder  restClientBuilder() {
        // 拆分地址
        List<HttpHost> httpHostList = new ArrayList<>();
        String[] hostArray = elasticSearchConfigProperties.getAddress().split(",");
        for (String element : hostArray) {
            String host = element.split(":")[0];
            String port = element.split(":")[1];
            httpHostList.add(new HttpHost(host, Integer.parseInt(port),
                    elasticSearchConfigProperties.getSchema()));
        }

        final CredentialsProvider credentialsProvider =
                new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(elasticSearchConfigProperties.getUsername(), elasticSearchConfigProperties.getPasswd()));
                
        // 转换成 HttpHost 数组
        HttpHost[] httpHostArray = httpHostList.toArray(new HttpHost[]{});
        // 构建连接对象
        RestClientBuilder builder = RestClient.builder(httpHostArray);
        // 异步连接延时配置
        builder.setRequestConfigCallback(requestConfigBuilder -> {
            requestConfigBuilder.setConnectTimeout(elasticSearchConfigProperties.getConnectTimeout());
            requestConfigBuilder.setSocketTimeout(elasticSearchConfigProperties.getSocketTimeout());
            requestConfigBuilder.setConnectionRequestTimeout(elasticSearchConfigProperties.getConnectionRequestTimeout());
            return requestConfigBuilder;
        });

        // 异步连接数配置
        builder.setHttpClientConfigCallback(httpClientBuilder -> {
            httpClientBuilder.setMaxConnTotal(elasticSearchConfigProperties.getMaxConnectNum());
            httpClientBuilder.setMaxConnPerRoute(elasticSearchConfigProperties.getMaxConnectPerRoute());
            httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
            return httpClientBuilder;
        });
        return builder;
    }

    @Bean
    public ElasticsearchTransport elasticsearchTransport(RestClientBuilder restClientBuilder) {
        return new RestClientTransport(restClientBuilder.build(), new JacksonJsonpMapper());
    }

    @Bean
    public ElasticsearchClient elasticsearchClient(ElasticsearchTransport transport) {
        return new ElasticsearchClient(transport);
    }

    @Bean
    public RestHighLevelClient restHighLevelClient(RestClientBuilder restClientBuilder) {
        return new RestHighLevelClient(restClientBuilder);
    }

}
