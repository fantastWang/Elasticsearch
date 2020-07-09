package com.wcj.elasticsearchapi;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wangchaojie
 * @Description TODO
 * @Date 2020/7/7 20:28
 **/
@Configuration
public class ElasticsearchConfig {

    /**
     * @Author wangchaojie
     * @Description 将客户端对象注入IOC容器中
     * @Date 20:29 2020/7/7
     **/
    @Bean
    public RestHighLevelClient restHighLevelClient() {
        return new RestHighLevelClient(RestClient.builder(
                new HttpHost("localhost", 9200, "http")));
    }
}
