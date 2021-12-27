package com.imooc.core;

public enum Method {
    POST("HEAD"),GET("GET"),PUT("PUT"),
    DELETE("DELETE"),PATCH("PATCH"),HEAD("HEAD");
    Method(String name) {
        this.name = name;
    }
    String name;

    public boolean doOutput(){
        switch (this){
            case POST:
            case PUT:
                return true;
        }
        return false;
    }
}
