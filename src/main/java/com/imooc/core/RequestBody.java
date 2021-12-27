package com.imooc.core;

import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RequestBody {

    // 表单格式
    public static final String FORM = "multipart/form-data";
    private String type;

    private String boundary = createBoundary();
    private String startBoundary = "--"+ boundary;
    private String endBoundary = startBoundary + "--";

    private String createBoundary() {
        return "OkHttp" + UUID.randomUUID().toString();
    }


    // 表单参数
    private Map<String,Object> params;
    public RequestBody() {
        params = new HashMap<>();
    }

    public String getContentType() {
        return type +"; boundary = "+ boundary;
    }

    public long getContentLength() {
        long length = 0;
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof  String){
                String postTextStr = getText(key, (String) value);
                length += postTextStr.getBytes().length;
            }

            if (value instanceof  Bindary){
                String postTextStr = getText(key, (Bindary) value);
                length += postTextStr.getBytes().length;
            }
        }

        if (params.size() !=0 ){
            length += endBoundary.getBytes().length;
        }
        return length;
    }

    private String getText(String key,String value){
        return startBoundary+"\r\n"+
                "Content-Disposition: for-data; name = \""+key+"\"\r\n"+
                "Content-Type: text/plain\r\n"+
                "\r\n\r\n"+
                value+
                "\r\n";
    }
    private String getText(String key,Bindary value){
        return startBoundary+"\r\n"+
                "Content-Disposition: for-data; name = \""+key+"\" filename = \""+value.fileName()+""+
                "Content-Type: "+value.mimType()+"\r\n"+
                "\r\n";
    }

    public void onWriteBody(OutputStream outputStream) throws IOException {
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof  String){
                String postTextStr = getText(key, (String) value);
                outputStream.write(postTextStr.getBytes());
            }


            if (value instanceof  Bindary){
                Bindary bindary = (Bindary) value;
                String postTextStr = getText(key, bindary);
                outputStream.write(postTextStr.getBytes());
                bindary.onWrite(outputStream);
                outputStream.write("\r\n".getBytes());
            }
        }

        if (params.size() !=0 ){
            outputStream.write(endBoundary.getBytes());
        }
    }


    public RequestBody addParam(String key, String value) {
        params.put(key, value);
        return this;
    }

    public RequestBody type(String type){
        this.type = type;
        return this;
    }

    public static Bindary create(final File file){
        return new Bindary() {
            @Override
            public long fileLength() {
                return file.length();
            }

            @Override
            public String mimType() {
                FileNameMap fileNameMap = URLConnection.getFileNameMap();
                String mimType = fileNameMap.getContentTypeFor(file.getAbsolutePath());
                if (TextUtils.isEmpty(mimType)){
                    mimType = "application/octet-stream";
                }
                return mimType;
            }

            @Override
            public String fileName() {
                return file.getName();
            }

            @Override
            public void onWrite(OutputStream outputStream) throws IOException {
                FileInputStream inputStream = new FileInputStream(file);

                byte[] buffer = new byte[2048];
                int len = 0;
                while ((len = inputStream.read(buffer))!=-1){
                    outputStream.write(buffer,0,len);
                }
                inputStream.close();
            }
        };
    }
}
