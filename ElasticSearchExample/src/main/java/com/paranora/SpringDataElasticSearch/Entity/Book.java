package com.paranora.SpringDataElasticSearch.Entity;

import com.paranora.SpringDataElasticSearch.Entity.Author;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yangqun on 2016/09/10.
 */

@Document(indexName = "book", type = "book", shards = 5, replicas = 1, refreshInterval = "-1")
public class Book {

    @Id
    private String id;

    @Field(type = FieldType.String)
    private String name;

    @Field(type = FieldType.Object)
    private Author author;

    @Field(type = FieldType.Nested)
    private Map<Integer, Collection<String>> buckets = new HashMap<Integer, Collection<String>>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Map<Integer, Collection<String>> getBuckets() {
        return buckets;
    }

    public void setBuckets(Map<Integer, Collection<String>> buckets) {
        this.buckets = buckets;
    }
}