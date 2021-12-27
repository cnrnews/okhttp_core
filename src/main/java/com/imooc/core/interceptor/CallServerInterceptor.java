package com.imooc.core.interceptor;

import com.imooc.dialogdemo.okhttp.Request;
import com.imooc.dialogdemo.okhttp.RequestBody;
import com.imooc.dialogdemo.okhttp.Response;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class CallServerInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        URL url = new URL(request.url);

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        urlConnection.connect();

        // 写内容
        RequestBody requestBody = request.requestBody;
        if (requestBody!=null){
            requestBody.onWriteBody(urlConnection.getOutputStream());
        }

        int statusCode = urlConnection.getResponseCode();
        if (statusCode == 200){
            InputStream inputStream = urlConnection.getInputStream();
            Response response = new Response(inputStream);
            return response;
        }

        return null;
    }
}
