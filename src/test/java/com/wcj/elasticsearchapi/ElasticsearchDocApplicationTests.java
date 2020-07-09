package com.wcj.elasticsearchapi;

import com.alibaba.fastjson.JSON;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.settings.put.UpdateSettingsRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.*;

@SpringBootTest
class ElasticsearchDocApplicationTests {

    //注入Elasticsearch客户端对象
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    //新增文档对象
    @Test
    public void createDoc() throws IOException {
        //初始化对象
        User user = new User(1, "张三", new Date());
        //创建索引库请求，并设置文档对象
        IndexRequest indexRequest = new IndexRequest("index1");
        /**
         * 如果不设置元数据_id，则Elasticsearch会自动生成随机id
         * 注意：对象中不能将字段名的命名和元数据名称相同，否则会抛出异常
         * reason=Field [_id] is a metadata field and cannot be added inside a document
         **/
        indexRequest.id("1");
        indexRequest.source(JSON.toJSONString(user), XContentType.JSON);
        //使用客户端发送请求
        IndexResponse index = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        System.out.println(index.status());
    }

    @Test
    public void settings() throws IOException {
        CreateIndexRequest index1 = new CreateIndexRequest("index1");

        Settings.Builder builder = Settings.builder();
        builder.put("analysis.analyzer.default.type", "ik_max_word");
//        UpdateSettingsRequest updateSettingsRequest = new UpdateSettingsRequest();
//        UpdateSettingsRequest settings = updateSettingsRequest.settings(builder);
        index1.settings(builder);
        AcknowledgedResponse acknowledgedResponse = restHighLevelClient.indices().create(index1, RequestOptions.DEFAULT);
        System.out.println(acknowledgedResponse.isAcknowledged());
    }

    //获取文档对象
    @Test
    public void getDoc() throws IOException {
        //获取索引库请求，传入元数据id
        GetRequest indexRequest = new GetRequest("index1", "E7k0LXMBdc_zFJAw8GSt");
        //使用客户端发送请求
        GetResponse index = restHighLevelClient.get(indexRequest, RequestOptions.DEFAULT);
        System.out.println(index.getSourceAsString());
    }

    @Test
    public void getDoc1() throws IOException {
        //获取索引库请求，传入元数据id
        SearchRequest indexRequest = new SearchRequest("index1");
        //使用客户端发送请求
        SearchResponse search = restHighLevelClient.search(indexRequest, RequestOptions.DEFAULT);
        System.out.println(search.getHits().getHits());
    }

    //修改文档对象
    @Test
    public void updateDoc() throws IOException {
        //初始化对象
        User user = new User(44,null,null);
        //修改索引库请求，传入元数据id
        UpdateRequest indexRequest = new UpdateRequest("fkt_yl", "45");
        String str ="{'createDate':'2020-07-07 18:00:02'}";
        indexRequest.doc(JSON.parseObject(str), XContentType.JSON);
        //使用客户端发送请求
        UpdateResponse update = restHighLevelClient.update(indexRequest, RequestOptions.DEFAULT);
        System.out.println(update.status());
    }

    //删除文档对象
    @Test
    public void deleteDoc() throws IOException {
        //删除索引库请求，传入元数据id
        DeleteRequest indexRequest = new DeleteRequest("index1", "ErkqLXMBdc_zFJAwBGSH");
        //使用客户端发送请求
        DeleteResponse delete = restHighLevelClient.delete(indexRequest, RequestOptions.DEFAULT);
        System.out.println(delete.status());
    }

    //批量插入文档对象
    @Test
    public void bulkRequest() throws IOException {
        //批量请求
        BulkRequest bulkRequest = new BulkRequest();
        List<User> list = new ArrayList<>();
        list.add(new User(1, "张三", new Date()));
        list.add(new User(2, "张三", new Date()));
        list.add(new User(3, "张三", new Date()));
        list.add(new User(4, "张三", new Date()));
        list.add(new User(5, "张三", new Date()));
        list.add(new User(6, "张三", new Date()));
        for (int i = 0; i < list.size(); i++) {
            bulkRequest.add(new IndexRequest("index1").id((i + 1) + "")
                    .source(JSON.toJSONString(list.get(i)), XContentType.JSON));
        }
        //使用客户端发送新增文档请求
        BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        System.out.println(bulk.hasFailures());
    }

    //条件查询文档对象
    @Test
    public void conditionGet() throws IOException {
        SearchRequest searchRequest = new SearchRequest("index1");
        SearchSourceBuilder builder = new SearchSourceBuilder();
        //使用QueryBuilders工具实现条件的查询
        builder.query(QueryBuilders.termQuery("name.keyword", "张三"));
        searchRequest.source(builder);
        SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHit[] hits = response.getHits().getHits();
        for (int i = 0; i < hits.length; i++) {
            System.out.println(hits[i].getSourceAsString());
        }
    }

    @Test
    public void search() throws IOException {
        SearchRequest searchRequest = new SearchRequest("fkt_yl");
        SearchSourceBuilder searchRequestBuilder = new SearchSourceBuilder();
        //分页
        searchRequestBuilder.from(0);
        searchRequestBuilder.size(10);
        //多条件查询 start==========
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.should(QueryBuilders.matchQuery("textIntroduction", "java"));
        boolQueryBuilder.should(QueryBuilders.matchQuery("name", "java"));
        searchRequestBuilder.query(boolQueryBuilder);
        //多条件查询 end==========
        //高亮查询 start==========
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("name");
        highlightBuilder.field("textIntroduction");
        //是否需要多个高亮
        //highlightBuilder.requireFieldMatch(false);
        highlightBuilder.preTags("<span style='color:red'>");
        highlightBuilder.postTags("</span>");
        searchRequestBuilder.highlighter(highlightBuilder);
        searchRequest.source(searchRequestBuilder);
        //高亮查询 end==========
        SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHit[] hits = search.getHits().getHits();
        List<Map<String, Object>> list = new ArrayList<>();
        for (SearchHit hit:hits) {
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            System.out.println(sourceAsMap);
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            HighlightField name = highlightFields.get("name");
            HighlightField textIntroduction = highlightFields.get("textIntroduction");
            if(name!=null){
                String new_Name = "";
                for (Text fragment : name.fragments()) {
                    new_Name+=fragment;
                };
                sourceAsMap.put("name",new_Name);
            }
            if(textIntroduction!=null){
                String new_textIntroduction = "";
                for (Text fragment : textIntroduction.fragments()) {
                    new_textIntroduction+=fragment;
                };
                sourceAsMap.put("textIntroduction",new_textIntroduction);
            }
            list.add(sourceAsMap);
        }
        SearchHits hits1 = search.getHits();
        System.out.println(hits1.getTotalHits().value);
    }
}
