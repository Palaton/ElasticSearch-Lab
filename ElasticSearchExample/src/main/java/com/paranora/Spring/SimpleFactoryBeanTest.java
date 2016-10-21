package com.paranora.Spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by yangqun on 2016/10/21.
 */
@Component
public class SimpleFactoryBeanTest {

    @Autowired
    public Simple simple;

    public void test(){
        simple.sayHello();
    }
}
