package com.paranora;

import com.paranora.ElasticSearch.ElasticSearchApp;
import com.paranora.SpringDataElasticSearch.Entity.Author;
import com.paranora.SpringDataElasticSearch.Entity.Book;
import com.paranora.SpringDataElasticSearch.Repositories.BookRepository;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Created by yangqun on 2016/09/11.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/spring-context-test.xml")
public class SpringDataElasticSearchTest {

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
    public void search() throws Exception{
        TransportClient client = ElasticSearchApp.generateClient("10.4.254.30", 9300, "es.cluster.a");

        ElasticsearchTemplate elasticsearchTemplate = new ElasticsearchTemplate(client);
        String expectedDate = "创世纪";
        String expectedWord = "book";
        CriteriaQuery query = new CriteriaQuery(
                new Criteria("name").contains(expectedDate).and(new Criteria("author.name").contains("moon")));

        List<Book> result = elasticsearchTemplate.queryForList(query, Book.class);
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
