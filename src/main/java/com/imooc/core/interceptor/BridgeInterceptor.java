package com.imooc.core.interceptor;

import com.imooc.dialogdemo.okhttp.Request;
import com.imooc.dialogdemo.okhttp.RequestBody;
import com.imooc.dialogdemo.okhttp.Response;

import java.io.IOException;

public class BridgeInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        // 添加请求头
        request.header("Connection","keep-alive");
        RequestBody requestBody = request.requestBody();
        if (requestBody!=null){
            request.header("Content-Type",requestBody.getContentType());
            request.header("Content-Length",Long.toString(requestBody.getContentLength()));
        }
        Response response = chain.proceed(request);
        return response;
    }
}
