package com.paranora;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.ObjectArraySerializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.AbstractMap;
import java.util.Map;

import static javafx.scene.input.KeyCode.J;
import static org.apache.http.client.methods.RequestBuilder.put;

/**
 * Created by yangqun on 2016/10/27.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/spring-context-test.xml")
public class FastJsonTest {

    @Test
    public void test(){
        String jsonString="{\n" +
                "  \"query\": {\n" +
                "    \"filtered\": {\n" +
                "      \"query\": {\n" +
                "        \"match_all\": {}\n" +
                "      },\n" +
                "      \n" +
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
                "                    \"filter\": {\n" +
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
                "  }}";
        Map<String,Object> jsonObject= JSON.parseObject(jsonString,Map.class);

//        jsonObject.put("filter",new Map<String,Object>(){
//            {
//                put("bool",new Map<String,Object>(){{
//                             put("must",new )
//                    });
//            }
//        });

        jsonObject.put("filter","{\"bool\":{\"must\":{\"range\":{\"Levels\":{\"gt\":1}}}}}");

        jsonString=JSON.toJSONString(jsonObject);

        System.out.println("hi fastjson.");
    }
}