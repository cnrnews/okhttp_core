package com.imooc.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Response {

    private final InputStream inputStream;
    public Response(InputStream inputStream) {
        this.inputStream = inputStream;
    }
    public String string(){
        return convertStreamToString(inputStream);
    }

    public String convertStreamToString(InputStream is){
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine())!=null){
                sb.append(line);
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
