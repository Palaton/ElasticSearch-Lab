package com.paranora;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.paranora.Jest.JestApp;
import com.paranora.SirenJoin.JestPlugins.SirenJoinBuilder;
import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.junit.Test;

import java.util.List;

/**
 * Created by yangqun on 2016/09/19.
 */
public class SirenJoinTest
{
    @Test
    public void search() throws Exception{
        JestClient jestClient = JestApp.getClient();

        String query = "{\n" +
                "  \"query\": {\n" +
                "    \"filtered\": {\n" +
                "      \"query\": {\n" +
                "        \"match_all\": {}\n" +
                "      },\n" +
                "      \"filter\": [\n" +
                "        {\n" +
                "          \"bool\": {\n" +
                "            \"must\": [\n" +
                "              {\n" +
                "                \"term\": {\n" +
                "                  \"ParentID\": \"bobin\"\n" +
                "                }\n" +
                "              },\n" +
                "              {\n" +
                "                \"term\": {\n" +
                "                  \"Status\": 1\n" +
                "                }\n" +
                "              },\n" +
                "              {\n" +
                "                \"filterjoin\": {\n" +
                "                  \"DeptID\": {\n" +
                "                    \"indices\": [\n" +
                "                      \"wechatdepartment\"\n" +
                "                    ],\n" +
                "                    \"types\": [\n" +
                "                      \"department\"\n" +
                "                    ],\n" +
                "                    \"path\": \"K3ID\",\n" +
                "                    \"query\": {\n" +
                "                      \"bool\": {\n" +
                "                        \"must\": [\n" +
                "                          {\n" +
                "                            \"term\": {\n" +
                "                              \"DeptName\": \"研发4组\"\n" +
                "                            }\n" +
                "                          }\n" +
                "                        ]\n" +
                "                      }\n" +
                "                    }\n" +
                "                  }\n" +
                "                }\n" +
                "              }\n" +
                "            ]\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  },\n" +
                "  \"sort\": {\n" +
                "    \"Levels\": \"desc\"\n" +
                "  }\n" +
                "}";


        Search search = new SirenJoinBuilder(query)
                .addIndex("wechatuser")
                .addType("user")
                .build();

        SearchResult result = jestClient.execute(search);

//        JsonObject jsonObject =(((JsonArray)((JsonObject)result.getJsonObject().get("hits")).get("hits")).get(0);


        if(result.isSucceeded() && null!=result.getTotal()) {
            List<SearchResult.Hit<User, Void>> hits = result.getHits(User.class);
        } else {
            System.out.println(result.getErrorMessage());
        }

        System.out.println("ok!");
    }
}
