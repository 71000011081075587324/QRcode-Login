package com.example.qrcodescanning_demo_androidx.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.qrcodescanning_demo_androidx.vo.ServerResponse;
import com.example.qrcodescanning_demo_androidx.vo.UserVo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.decoding.Intents;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

//单例实现SharedPreferencesUtils工具类
public class SharedPreferencesUtils {

    private static SharedPreferencesUtils sharedPreferencesUtils;
    private static SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static final String FILENAME="qrcode";
    private static Set<String> hashset =  new HashSet<String>();

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
        hashset.add(key);
    }

    public void putString(String key, String value){
        editor.putString(key,value);
        editor.commit();
        hashset.add(key);
    }

    public void putInt(String key,int value){
        editor.putInt(key,value);
        editor.commit();
        hashset.add(key);
    }

    public boolean readBoolean(String key){
        return sharedPreferences.getBoolean(key,false);
    }

    public String readString(String key){
        return sharedPreferences.getString(key,"");
    }

    public int readInt(String key){
        return sharedPreferences.getInt(key,-1);
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
        hashset.remove(key);
    }

    public void retainPartial(List<String> keyOut){
        String key;
        Iterator<String> it = hashset.iterator();
        while (it.hasNext()){
            key = it.next();
            if(!keyOut.contains(key)){
                editor.remove(key);
                editor.commit();
                it.remove();
            }
        }
    }

    public void clear(){
        editor.clear();
        editor.commit();
        hashset.clear();
    }

}
