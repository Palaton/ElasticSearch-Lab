package com.paranora;

import com.paranora.ElasticSearch.ElasticSearchApp;
import com.paranora.SpringDataElasticSearch.Author;
import com.paranora.SpringDataElasticSearch.Book;
import com.paranora.SpringDataElasticSearch.repositories.BookRepository;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by yangqun on 2016/09/11.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/spring-context-test.xml")
public class SpringDataElasticSearchAppTest {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private BookRepository repository;

    @Test
    public void saveIndex() {

        Book book = new Book();
        book.setId("103");
        book.setName("创世纪-继篇-V2");
        book.setBuckets(new HashMap<Integer, Collection<String>>() {
            {
                put(1, new ArrayList<String>() {
                    {
                        add("a");
                        add("b");
                        add("a1");
                        add("b1");
                        add("a2");
                        add("b2");
                    }
                });
            }
        });

        Author author = new Author();
        author.setId("100C");
        author.setName("flyceek");
        book.setAuthor(author);

        repository.save(book);


        System.out.println("hello i wanna test.");
    }

    @Test
    public void getIndex() {
        Book searchBook = repository.findOne("103");
    }

    @Test
    public void deleteIndex() {
//        elasticsearchTemplate.deleteIndex(Book.class);

//        elasticsearchTemplate.deleteIndex("book");

        try {
            TransportClient client = ElasticSearchApp.generateClient("10.4.254.30", 9300, "es.cluster.a");
            DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest("book");
            DeleteRequest deleteRequest = new DeleteRequest("book");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void refreshIndex() {
        elasticsearchTemplate.refresh(Book.class);
    }

    @Test
    public void createIndex() {
        elasticsearchTemplate.createIndex(Book.class);
    }
}
