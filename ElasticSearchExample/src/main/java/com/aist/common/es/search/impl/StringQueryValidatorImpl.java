package com.aist.common.es.search.impl;

import com.aist.common.es.search.Validator;

/**
 * Created by yangqun on 2016/10/24.
 */
public class StringQueryValidatorImpl implements Validator<String> {

    @Override
    public boolean Validate(String query) {
        return true;
    }
}
