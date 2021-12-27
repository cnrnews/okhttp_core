package com.imooc.core.interceptor;


import com.imooc.core.Request;
import com.imooc.core.Response;

import java.io.IOException;

public interface Interceptor {

    Response intercept(Chain chain) throws IOException;

    interface Chain{
        Request request();
        Response proceed(Request request)throws IOException;
    }
}
