package com.wcj.elasticsearchapi;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.elasticsearch.client.RequestOptions.DEFAULT;

@SpringBootTest
class ElasticsearchApiApplicationTests {

    //注入Elasticsearch客户端对象
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    //创建索引,索引名称必须全部小写，否则会抛出Elasticsearch exception [index name [xx], must be lowercase]
    @Test
    void createIndex() throws IOException {
        CreateIndexRequest indexRequest = new CreateIndexRequest("wcjIndex");
        CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(indexRequest, DEFAULT);
        System.out.println(createIndexResponse.isAcknowledged());
    }

    //查询索引及判断索引是否存在
    @Test
    void getIndex() throws IOException {
        GetIndexRequest index3 = new GetIndexRequest("index3");
        GetIndexResponse getIndexResponse = restHighLevelClient.indices().get(index3, DEFAULT);
        boolean exists = restHighLevelClient.indices().exists(index3, DEFAULT);
        System.out.println(getIndexResponse.getMappings());
        System.out.println(exists);
    }

    //删除索引
    @Test
    void deleteIndex() throws IOException {
        DeleteIndexRequest deleteRequest = new DeleteIndexRequest("wcj");
        AcknowledgedResponse delete = restHighLevelClient.indices().delete(deleteRequest, DEFAULT);
        System.out.println(delete.isAcknowledged());
    }
}
