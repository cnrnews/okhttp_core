package com.imooc.core;

import com.imooc.core.interceptor.BridgeInterceptor;
import com.imooc.core.interceptor.CacheInterceptor;
import com.imooc.core.interceptor.CallServerInterceptor;
import com.imooc.core.interceptor.Interceptor;
import com.imooc.core.interceptor.RealInterceptorChain;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RealCall implements Call {

    private final Request orignalReques;
    private final  OkHttpClient okHttpClient;
    private Callback callback;

    public RealCall(Request request, OkHttpClient okHttpClient) {
        this.orignalReques = request;
        this.okHttpClient = okHttpClient;
    }

    public static Call newCall(Request request, OkHttpClient okHttpClient) {
        return new RealCall(request,okHttpClient);
    }

    @Override
    public void enqueue(Callback callback) {
        AsyncCall asyncCall = new AsyncCall(callback);
        okHttpClient.dispatcher.enqueue(asyncCall);
    }

    @Override
    public Response execute() {
        // 开始访问网络
        final Request request = orignalReques;
        try {
            URL url = new URL(request.url);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod(request.method.name);
            urlConnection.setDoOutput(request.method.doOutput());

            RequestBody requestBody = request.requestBody;
            if (requestBody!=null){
                // 头信息
                urlConnection.setRequestProperty("Content-Type",requestBody.getContentType());
                urlConnection.setRequestProperty("Content-Length",
                        Long.toString(requestBody.getContentLength()));
            }


            urlConnection.connect();


            // 写内容
            if (requestBody!=null){
                requestBody.onWriteBody(urlConnection.getOutputStream());
            }


            int statusCode = urlConnection.getResponseCode();
            if (statusCode == 200){
                InputStream inputStream = urlConnection.getInputStream();
                Response response = new Response(inputStream);
                callback.onResponse(RealCall.this,response);
            }
        }  catch (IOException e) {
            callback.onFailure(RealCall.this,e);
        }

        return null;
    }

    final class AsyncCall extends NamedRunnable {

        Callback callback;

        public AsyncCall(Callback callback) {
            this.callback = callback;
        }

        @Override
        protected void execute() {
           try {
               final Request request = orignalReques;

               List<Interceptor> interceptors = new ArrayList<>();
               interceptors.add(new BridgeInterceptor());
               interceptors.add(new CacheInterceptor());
               interceptors.add(new CallServerInterceptor());

               RealInterceptorChain chain = new RealInterceptorChain(interceptors,0,orignalReques);

               Response response = chain.proceed(request);
               callback.onResponse(RealCall.this,response);
            } catch (IOException e) {
               callback.onFailure(RealCall.this,e);
            }
        }
    }
}
