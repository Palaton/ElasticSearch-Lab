package com.aist.common.es.connect.impl;

import com.aist.common.es.connect.EndPoint;

/**
 * Created by yangqun on 2016/10/24.
 */
public class PazoEndPointImpl implements EndPoint {

    private String address;
    private String port;

    @Override
    public String getAddress() {
        return address;
    }

    @Override
    public String getPort() {
        return port;
    }
}
