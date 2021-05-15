//package com.example.qrcodescanning_demo_androidx.utils;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.drawable.BitmapDrawable;
//import android.view.View;
//
//public class BackgroundUtils {
//
//    private static BackgroundUtils backgroundUtils;
//    private  Context context;
//
//    private BackgroundUtils(Context context){
//        this.context = context;
//    }
//
//    public static BackgroundUtils getInstance(Context context){
//        if(backgroundUtils == null){
//            synchronized (BackgroundUtils.class){
//                if(backgroundUtils == null){
//                    backgroundUtils = new BackgroundUtils(context);
//                }
//            }
//        }
//        return backgroundUtils;
//    }
//
//    public void getBackground(String path , View view) {
//        Bitmap bitmap = BitmapFactory.decodeFile(path);
//        view.setImageDrawable(new BitmapDrawable(view.getContext().getResources(), bitmap));
//    }
//}
