package com.paranora;

import com.paranora.ElasticSearch.ElasticSearchApp;
import com.paranora.SpringDataElasticSearch.Entity.Book;
import io.searchbox.core.SearchResult;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.util.concurrent.AtomicArray;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.common.xcontent.yaml.YamlXContent;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.suggest.Suggest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.paranora.ElasticSearch.ElasticSearchApp.generateClient;

/**
 * Created by yangqun on 2016/09/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/spring-context-test.xml")
public class ElasticSearchTest {

    @Test
    public void getIndex() throws Exception{
        System.out.println("test");
//        TransportClient client = ElasticSearchApp.generateClient("10.4.254.30", 9300, "es.cluster.a");
//
//        GetResponse response = client.prepareGet("wechatorg", "user", "1").get();
//
//        String user = new String(response.getSourceAsBytes());
    }

    @Test
    public void get() throws Exception {
        TransportClient client = ElasticSearchApp.generateClient("10.4.254.30",9300,"es.cluster.a");

        GetResponse response = client.prepareGet("wechatuser", "user", "1").get();

        String user = new String(response.getSourceAsBytes());

        client.close();

        System.out.println("Hello ElasticSearch API.");
    }

    @Test
    public void searchTest() throws Exception{
        TransportClient client = ElasticSearchApp.generateClient("10.4.254.30", 9300, "es.cluster.a");
        QueryBuilder queryBuilder= QueryBuilders.termQuery("UserID","yangqun");

        String query = "{\n" +
                "    \"query\":{\n" +
                "         \"filtered\":{\n" +
                "            \"filter\":{\n" +
                "                \"bool\":{\n" +
                "                    \"must\":[\n" +
                "                        {\"term\":{\"Levels\":1}}\n" +
                "                    ],\n" +
                "                    \"should\":[\n" +
                "                        {\"term\":{\"ParentID\":\"bobin\"}},\n" +
                "                        {\"term\":{\"ParentID\":\"zuojj\"}}\n" +
                "                    ]\n" +
                "                }\n" +
                "            },\n" +
                "            \"query\":{\n" +
                "                match_all:{}\n" +
                "            }\n" +
                "         }\n" +
                "    }\n" +
                "}";

        SearchResponse searchResponse = client.prepareSearch("wechatuser").setQuery(query).execute().actionGet();

        for(SearchHit hit: searchResponse.getHits()){
            Iterator<Map.Entry<String,Object>> iterator=hit.getSource().entrySet().iterator();
            System.out.println("=====================================================");
            while(iterator.hasNext()){
                Map.Entry<String,Object> entry=iterator.next();
                System.out.println(entry.getKey()+","+entry.getValue());
            }
            System.out.println("=====================================================");
        }

        System.out.println("end");
    }

    @Test
    public void parse() throws  Exception{
        String query = "{\n" +
                "    \"query\":{\n" +
                "         \"filtered\":{\n" +
                "            \"filter\":{\n" +
                "                \"bool\":{\n" +
                "                    \"must\":[\n" +
                "                        {\"term\":{\"Levels\":1}}\n" +
                "                    ],\n" +
                "                    \"should\":[\n" +
                "                        {\"term\":{\"ParentID\":\"bobin\"}},\n" +
                "                        {\"term\":{\"ParentID\":\"zuojj\"}}\n" +
                "                    ]\n" +
                "                }\n" +
                "            },\n" +
                "            \"query\":{\n" +
                "                match_all:{}\n" +
                "            }\n" +
                "         }\n" +
                "    }\n" +
                "}";
        XContentParser parser= YamlXContent.yamlXContent.createParser(query);

        System.out.println("end");
    }
}
