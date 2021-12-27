package com.imooc.core;

public interface Call {

    void enqueue(Callback callback);

    Response execute();
}
