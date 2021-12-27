# okhttp_core
手敲OkHttp核心逻辑

### 使用

```
OkHttpClient okHttpClient = new OkHttpClient();

// 构建 post RequestBody
RequestBody requestBody =  new RequestBody()
                .type(RequestBody.FORM)
                .addParam("pageNo","1") // 添加请求参数
                .addParam("pageSize","1");
// 构建 Requset 请求                
Request request = new Request.Builder()
                .url("https://www.baidu.com")
                .post(requestBody)
                .build();                
                
Call call = okHttpClient.newCall(request);
// 添加请求连接池 执行请求
call.enqueue(new Callback(){
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TAG",e.getMessage());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("TAG",response.string());
            }
        });
```
