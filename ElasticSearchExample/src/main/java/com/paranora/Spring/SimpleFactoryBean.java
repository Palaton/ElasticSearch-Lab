package com.paranora.Spring;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

/**
 * Created by yangqun on 2016/10/21.
 */
@Component
public class SimpleFactoryBean implements FactoryBean {

    public Object getObject() throws Exception {
       return new SimpleImpl();
    }

    @SuppressWarnings("unchecked")
    public Class getObjectType() {
        return SimpleImpl.class;
    }

    public boolean isSingleton() {
        return true;
    }
}