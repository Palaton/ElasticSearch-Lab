package com.paranora;

import com.paranora.Jest.JestApp;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Get;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.List;

/**
 * Created by yangqun on 2016/09/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/spring-context-test.xml")
public class JestTest {

    @Test
    public void getIndex() throws Exception {
        JestClient jestClient = JestApp.getClient();


        System.out.println("end.");
    }

    public static void search() throws Exception {
        JestClient jestClient = JestApp.getClient();

        String query = "{\n" +
                "    query:{\n" +
                "        filtered:{\n" +
                "            filter:{\n" +
                "                bool:{\n" +
                "                    must:[\n" +
                "                        {term:{\"ParentID\":\"bobin\"}}\n" +
                "                    ]\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}";


        Search search = new Search.Builder(query)
                .addIndex("wechatorg")
                .addType("user")
                .build();

        SearchResult result = jestClient.execute(search);
//        SearchResult.Hit<User, Void> hits=result.getFirstHit(User.class);

        List<SearchResult.Hit<User, Void>> hits = result.getHits(User.class);

        System.out.println("end.");
    }

    @Test
    public void get() throws Exception{
        JestClient jestClient = JestApp.getClient();
        Get get = new Get.Builder("wechatorg", "/user/_mapping").build();
        try {
            JestResult rs = jestClient.execute(get);
            System.out.println(rs.getJsonString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
