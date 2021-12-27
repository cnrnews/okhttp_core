package com.imooc.core;

import java.util.HashMap;
import java.util.Map;

public class Request {


    public final String url;
    public final Method method;
    public final Map<String,String>headers;
    public final RequestBody requestBody;
    public Request(Builder builder) {
        this.url = builder.url;
        this.method = builder.method;
        this.headers = builder.headers;
        this.requestBody = builder.requestBody;
    }

    public RequestBody requestBody() {
        return requestBody;
    }

    public void header(String key, String value){
        headers.put(key, value)
;    }

    public static class Builder{
        String url;
        Method method;
        Map<String,String>headers;
        RequestBody requestBody;
        public Builder() {
            method = Method.GET;
            headers = new HashMap<>();
        }

        public Builder url(String url){
            this.url = url;
            return this;
        }
        public Request build(){
            return new Request(this);
        }

        public Builder get(){
            method = Method.GET;
            return this;
        }
        public Builder post(RequestBody body){
            method = Method.POST;
            this.requestBody = body;
            return this;
        }
        public void header(String key,String value){
            headers.put(key,value);
        }

    }
}
