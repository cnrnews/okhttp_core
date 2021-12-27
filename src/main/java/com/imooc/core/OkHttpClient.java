package com.imooc.core;

public class OkHttpClient {

    final  Dispatcher dispatcher;

    public OkHttpClient(Builder builder) {
        dispatcher = builder.dispatcher;
    }
    public OkHttpClient(){
        this(new Builder());
    }

    public Call newCall(Request request) {
        return RealCall.newCall(request,this);
    }

    public static class Builder{
        Dispatcher dispatcher;

        public Builder(){
            dispatcher = new Dispatcher();
        }

    }
}
