package com.example.asus.myapplication.utils;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpUtils {
    OkHttpClient okHttpClient;

    private HttpUtils(){
        okHttpClient=new OkHttpClient();
    }
    static class OkHolder{
        private static final HttpUtils httpUtils=new HttpUtils();
    }
    public static HttpUtils getInstence(){
        return OkHolder.httpUtils;
    }
    public void AsyncGet(String url, Callback callback){
        Request request=new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(callback);
    }
}
