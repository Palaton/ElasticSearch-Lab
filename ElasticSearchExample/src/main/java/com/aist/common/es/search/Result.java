package com.aist.common.es.search;

/**
 * Created by yangqun on 2016/10/24.
 */
public class Result<TResult> {
    protected String status;
    protected int took;
    protected TResult Result;

    protected String type;
    protected String reason;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTook() {
        return took;
    }

    public void setTook(int took) {
        this.took = took;
    }

    public TResult getResult() {
        return Result;
    }

    public void setResult(TResult result) {
        Result = result;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
