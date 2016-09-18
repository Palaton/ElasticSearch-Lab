package com.paranora;

import com.paranora.Jest.JestApp;
import io.searchbox.client.JestClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by yangqun on 2016/09/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/spring-context-test.xml")
public class JestAppTest {

    @Test
    public void getIndex(){
        JestClient jestClient = JestApp.getClient();
    }
}
