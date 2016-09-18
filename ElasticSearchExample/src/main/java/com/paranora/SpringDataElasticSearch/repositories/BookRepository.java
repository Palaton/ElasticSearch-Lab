package com.paranora.SpringDataElasticSearch.Repositories;

import com.paranora.SpringDataElasticSearch.Entity.Book;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Created by yangqun on 2016/09/11.
 */
public interface BookRepository extends ElasticsearchRepository<Book, String> {
}
