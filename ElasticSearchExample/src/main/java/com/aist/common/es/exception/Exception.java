package com.aist.common.es.exception;

/**
 * Created by yangqun on 2016/10/24.
 */
public class Exception extends java.lang.Exception {
    private String type;
    private String reason;
    private String status;

    public Exception(String status, String type, String reason) {
        super(reason);
        this.status=status;
        this.type=type;
        this.reason=reason;
    }
}
