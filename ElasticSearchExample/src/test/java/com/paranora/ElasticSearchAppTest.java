package com.paranora;

import com.paranora.ElasticSearch.ElasticSearchApp;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by yangqun on 2016/09/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/spring-context-test.xml")
public class ElasticSearchAppTest {

    @Test
    public void getIndex() throws Exception{
        System.out.println("test");
//        TransportClient client = ElasticSearchApp.generateClient("10.4.254.30", 9300, "es.cluster.a");
//
//        GetResponse response = client.prepareGet("wechatorg", "user", "1").get();
//
//        String user = new String(response.getSourceAsBytes());
    }
}
