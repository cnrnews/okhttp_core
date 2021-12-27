package com.imooc.core.interceptor;


import com.imooc.core.Request;
import com.imooc.core.Response;

import java.io.IOException;

public class CacheInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        // 本地有没有缓存，如果有没有过期
        Response response = chain.proceed(request);
        return response;
    }
}
