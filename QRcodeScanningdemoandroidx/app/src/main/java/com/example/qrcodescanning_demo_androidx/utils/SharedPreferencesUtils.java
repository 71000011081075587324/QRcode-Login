package com.example.qrcodescanning_demo_androidx.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.qrcodescanning_demo_androidx.vo.ServerResponse;
import com.example.qrcodescanning_demo_androidx.vo.UserVo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.decoding.Intents;

import java.lang.reflect.Type;

//单例实现SharedPreferencesUtils工具类
public class SharedPreferencesUtils {

    private static SharedPreferencesUtils sharedPreferencesUtils;
    private static SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static final String FILENAME="qrcode";

    private SharedPreferencesUtils(Context context){
        sharedPreferences = context.getSharedPreferences(FILENAME,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static SharedPreferencesUtils getInstance(Context context){
        if(sharedPreferences == null){
            synchronized (SharedPreferencesUtils.class){
                if(sharedPreferences == null){
                    sharedPreferencesUtils = new SharedPreferencesUtils(context);
                }
            }
        }
        return sharedPreferencesUtils;
    }

    public void putBoolean(String key,boolean value){
        editor.putBoolean(key,value);
        editor.commit();
    }

    public void putString(String key, String value){
        editor.putString(key,value);
        editor.commit();
    }

    public boolean readBoolean(String key){
        return sharedPreferences.getBoolean(key,false);
    }

    public String readString(String key){
        return sharedPreferences.getString(key,"");
    }

    public <T> ServerResponse<T> readObject(String key,Type userType){
        String str = sharedPreferences.getString(key,"");
        Gson gson = new Gson();
//        Type userType = new TypeToken<ServerResponse<T>>(){}.getType();
        return gson.fromJson(str,userType);
    }


    public void delete(String key){
        editor.remove(key);
        editor.commit();
    }

    public void clear(){
        editor.clear();
        editor.commit();
    }

}
