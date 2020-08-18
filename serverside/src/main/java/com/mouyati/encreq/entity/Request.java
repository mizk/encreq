package com.mouyati.encreq.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Request {
    @JsonProperty("key")
    private String token;
    @JsonProperty("body")
    private String body;
    @JsonProperty("iv")
    private String iv;

    public Request() {
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
