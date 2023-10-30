package com.online_shopping_shaojin.online_shopping_shaojin.configuration;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchConfig {
    @Bean
    public RestHighLevelClient EsClient() {
        return new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost",
                        9200,"http" ))//9200 是elastic search的端口号
        );
    }
}
