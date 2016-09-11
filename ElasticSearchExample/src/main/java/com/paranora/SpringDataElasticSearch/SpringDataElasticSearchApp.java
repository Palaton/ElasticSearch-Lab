package com.paranora.SpringDataElasticSearch;


import com.paranora.ElasticSearch.ElasticSearchApp;
import org.elasticsearch.client.transport.TransportClient;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.*;

import java.io.InputStream;
import java.util.*;



/**
 * Created by yangqun on 2016/09/10.
 */
public class SpringDataElasticSearchApp {

    public static void test() throws Exception {
        Properties properties = new Properties();
        InputStream fis = SpringDataElasticSearchApp.class.getClassLoader().getResourceAsStream("config.properties");
        properties.load(fis);
        fis.close();
        String clusterName = properties.getProperty("cluster.name");

        searchTest();

//        ClassLoader extensionClassloader=ClassLoader.getSystemClassLoader();
//        System.out.println("the parent of extension classloader : "+extensionClassloader);//
//        System.out.println("abc:"+SpringDataElasticSearchApp.class.getClassLoader().getResource(""));
        System.out.println(clusterName);
    }

    public static void indexTest() throws Exception {
        TransportClient client = ElasticSearchApp.generateClient("10.4.254.30", 9300, "es.cluster.a");
        ElasticsearchTemplate elasticsearchTemplate = new ElasticsearchTemplate(client);
//
//        Map<Integer,Collection<String>> map=new HashMap<Integer,Collection<String>>(){
//            {
//                put(1, new ArrayList<String>() {
//                    {
//                        add("a");add("b");
//                    }
//                });
//            }
//        };

        Book book = new Book();
        book.setId("101");
        book.setName("创世纪-继篇");
        book.setBuckets(new HashMap<Integer,Collection<String>>(){
            {
                put(1, new ArrayList<String>() {
                    {
                        add("a");add("b");
                    }
                });
            }
        });

        Author author = new Author();
        author.setId("100B");
        author.setName("moon");
        book.setAuthor(author);

        IndexQuery indexQuery = new IndexQueryBuilder().withId(book.getId()).withObject(book).build();
        elasticsearchTemplate.index(indexQuery);
    }

    public static void getMappingTest() throws Exception {
        TransportClient client = ElasticSearchApp.generateClient("10.4.254.30", 9300, "es.cluster.a");
        ElasticsearchTemplate elasticsearchTemplate = new ElasticsearchTemplate(client);
        Map userMapping = elasticsearchTemplate.getMapping("wechatorg", "user");
    }

    public static void searchTest() throws Exception{
        TransportClient client = ElasticSearchApp.generateClient("10.4.254.30", 9300, "es.cluster.a");
        ElasticsearchTemplate elasticsearchTemplate = new ElasticsearchTemplate(client);

        String expectedDate = "创世纪";
        String expectedWord = "book";
        CriteriaQuery query = new CriteriaQuery(
                new Criteria("name").contains(expectedDate).and(new Criteria("author.name").contains("moon")));

        List<Book> result = elasticsearchTemplate.queryForList(query, Book.class);
    }
}
