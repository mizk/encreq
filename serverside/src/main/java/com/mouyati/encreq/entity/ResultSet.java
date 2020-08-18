package com.mouyati.encreq.entity;


import com.fasterxml.jackson.annotation.JsonProperty;

public class ResultSet {

    @JsonProperty("errorMsg")
    private String errorMsg;
    @JsonProperty("errorCode")
    private int errorCode;
    @JsonProperty("data")
    private Object data;

    public static ResultSet valueOf(String errorMsg, int errorCode) {
        ResultSet r = new ResultSet();
        r.setData(null);
        r.setErrorMsg(errorMsg);
        r.setErrorCode(errorCode);
        return r;
    }

    public static ResultSet valueOf(String errorMsg, int errorCode, Object data) {
        ResultSet r = new ResultSet();
        r.setData(data);
        r.setErrorMsg(errorMsg);
        r.setErrorCode(errorCode);
        return r;
    }


    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
