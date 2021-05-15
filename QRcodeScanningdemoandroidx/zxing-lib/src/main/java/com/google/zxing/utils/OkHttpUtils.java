package com.google.zxing.utils;

import android.util.Log;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class OkHttpUtils {

    private static TokenInterceptor tokenInterceptor = new TokenInterceptor();
    private static final OkHttpClient CLIENT = new OkHttpClient.Builder().build();
    private static final OkHttpClient CLIENTTOKRN = new OkHttpClient.Builder().addInterceptor(tokenInterceptor).build();
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");



    /**
     * 封装get请求(不带拦截器)
     * @param url
     * @param callback
     */
    public static void get(String url, OkHttpCallback callback){
        callback.url = url;
        Request request = new Request.Builder().url(url).build();
        CLIENT.newCall(request).enqueue(callback);

    }


//    /**
//     * 封装post请求(不带拦截器)
//     * @param url
//     * @param json
//     * @param callback
//     */
//    public static void post(String url, String json, OkHttpCallback callback){
//        callback.url = url;
//        RequestBody body = RequestBody.create(MediaType.parse("application/json"),json);
////        MediaType mediaType = MediaType.Companion.parse("application/json;charset=utf-8");
////        RequestBody stringBody = RequestBody.Companion.create(json,mediaType);
//        Request request = new Request.Builder().url(url).post(body).build();
//        Log.d("okhttp-post","Okhttp-post = " + request.toString());
//        CLIENT.newCall(request).enqueue(callback);
//    }

    /**
     * 封装post请求(不带拦截器)
     * @param url
     * @param body
     * @param callback
     */
    public static void post(String url, RequestBody body, OkHttpCallback callback){
        callback.url = url;
//        RequestBody body = RequestBody.create(JSON,json);
        Request request = new Request.Builder().url(url).post(body).build();
        CLIENT.newCall(request).enqueue(callback);
    }


    /**
     * 封装get请求(带拦截器)
     * @param url
     * @param callback
     */
    public static void getWithToken(String url,String token, OkHttpCallback callback){
        callback.url = url;
        Request request = new Request.Builder().url(url).addHeader("token",token).build();
        CLIENTTOKRN.newCall(request).enqueue(callback);

    }

//    /**
//     * 封装post请求(带拦截器)
//     * @param url
//     * @param json
//     * @param callback
//     */
//    public static void postWithToken(String url, String json, String token,OkHttpCallback callback){
//        callback.url = url;
////        RequestBody body = RequestBody.create(JSON,json);
//        MediaType mediaType = MediaType.Companion.parse("application/json;charset=utf-8");
//        RequestBody stringBody=RequestBody.Companion.create(json,mediaType);
//        Request request = new Request.Builder().url(url).addHeader("token",token).post(stringBody).build();
//        CLIENTTOKRN.newCall(request).enqueue(callback);
//    }

    /**
     * 封装post请求(带拦截器)
     * @param url
     * @param body
     * @param callback
     */
    public static void postWithToken(String url, RequestBody body, String token,OkHttpCallback callback){
        callback.url = url;
//        RequestBody body = RequestBody.create(JSON,json);
        Request request = new Request.Builder().url(url).addHeader("token",token).post(body).build();
        CLIENTTOKRN.newCall(request).enqueue(callback);
    }


}
