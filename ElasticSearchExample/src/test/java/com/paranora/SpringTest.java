package com.paranora;

import com.paranora.Spring.SimpleFactoryBeanTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by yangqun on 2016/10/21.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration( locations = {"classpath:/spring-context-test.xml"})
public class SpringTest {

    @Test
    public void test(){
        BeanFactory factory = new ClassPathXmlApplicationContext("spring-context-test.xml");
        SimpleFactoryBeanTest obj=factory.getBean(SimpleFactoryBeanTest.class);

        obj.test();

        String str=String.format("\"%s\"","flyceek");
        System.out.println(str);

        System.out.println("i want to test spring now.");
    }
}
