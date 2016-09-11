package com.paranora.SpringDataElasticSearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Created by yangqun on 2016/09/11.
 */
public interface BookRepository  extends ElasticsearchRepository<Book, String> {
}
