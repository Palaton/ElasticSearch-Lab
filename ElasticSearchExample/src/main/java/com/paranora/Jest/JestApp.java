package com.paranora.Jest;

import com.paranora.User;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.client.http.JestHttpClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

import java.util.List;

/**
 * Created by yangqun on 2016/08/27.
 */
public class JestApp {

    public synchronized static JestHttpClient getClient() {
        JestHttpClient client=null;
        if (client == null) {
            JestClientFactory factory = new JestClientFactory();
            factory.setHttpClientConfig(new HttpClientConfig.Builder("http://10.4.254.30:9200").multiThreaded(true).build());
            client = (JestHttpClient) factory.getObject();
        }
        return client;
    }

    public static void test() throws Exception {
        JestClient jestClient = getClient();

        String query="{\n" +
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

        List<SearchResult.Hit<User, Void>> hits=result.getHits(User.class);

    }
}
