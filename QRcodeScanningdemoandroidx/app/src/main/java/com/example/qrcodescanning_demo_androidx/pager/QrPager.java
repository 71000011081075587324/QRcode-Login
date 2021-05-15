package com.example.qrcodescanning_demo_androidx.pager;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.qrcodescanning_demo_androidx.MainActivity;
import com.example.qrcodescanning_demo_androidx.R;
import com.example.qrcodescanning_demo_androidx.base.BasePager;
import com.google.zxing.activity.CaptureActivity;
import com.google.zxing.util.Constant;
import com.google.zxing.utils.SharedPreferencesUtils;

import org.jetbrains.annotations.NotNull;

public class QrPager extends BasePager {
    private ImageButton ibOpenScan;
    private View view;
//    private SharedPreferencesUtils sharedPreferencesUtils;
//    private String token;

    private static final int CAMERA_OK = 1;
    //    public static final int REQ_PERM_EXTERNAL_STORAGE = 11004;
    private static boolean isPress = false;
    private static boolean isAllPremissions = true;
    private SharedPreferencesUtils sharedPreferencesUtils;
    LinearLayout layout;

    public QrPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        view = View.inflate(context, R.layout.module_fragment_qrpager,null);
        return view;
    }

    @Override
    public void initData() {
//        sharedPreferencesUtils = SharedPreferencesUtils.getInstance(view.getContext());
//        token = sharedPreferencesUtils.readString("token");
        super.initData();
    }


    /**
     * 每次创建、绘制该Fragment的View组件时回调该方法，Fragment将会显示该方法返回的View组件
     * 控件、页面的初始化
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return  返回初始化的view
     */
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        return initView();
    }

    private void findAllViewById(View view){
        ibOpenScan = (ImageButton)view.findViewById(R.id.ib_open);
        layout = (LinearLayout)view.findViewById(R.id.ll_qr);
    }

    /**
     * 当Fragment所在的Activity被启动完成后回调该方法
     * 设置控件的点击事件放在其中
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        findAllViewById(view);
        sharedPreferencesUtils = SharedPreferencesUtils.getInstance(view.getContext());

        ibOpenScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPermission();
            }
        });

        //设置背景
        String path = sharedPreferencesUtils.readString(sharedPreferencesUtils.readString("user") + "userIconPath");
        if(path != null && !path.equals("")){
            Bitmap bitmap = BitmapFactory.decodeFile(path);
//            ivUserIcon.setImageDrawable(new BitmapDrawable(view.getContext().getResources(), bitmap));
            layout.setBackground(new BitmapDrawable(view.getContext().getResources(), bitmap));//把bitmap转为drawable,layout为xml文件里的主layout
        }

    }

    //获取相机和存储权限
    private void getPermission() {

        //判断系统版本
        if (Build.VERSION.SDK_INT>22){
            //判断是否获取相机和存储权限权限
            if (ContextCompat.checkSelfPermission(view.getContext(),
                    android.Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(view.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                //未获得权限,进行申请
                ActivityCompat.requestPermissions((Activity) view.getContext(),
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},CAMERA_OK);
                return;

            }else {
                //说明已经获取到摄像头权限了
//                Log.i("MainActivity","已经获取了权限");
                // 二维码扫码
                isPress = true;
                Intent intent = new Intent(view.getContext(), CaptureActivity.class);
//                intent.putExtra("token",token);
                startActivityForResult(intent, Constant.REQ_QR_CODE);
            }
        }
    }
    //
    //请求权限后的回调方法
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        switch (requestCode) {
            case CAMERA_OK: {
                isAllPremissions = true;

                //判断是否获得所有权限
                for (int i=0;i<grantResults.length;i++){
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED){
                        isAllPremissions = false;
                        break;
                    }
                }

                // If request is cancelled, the result arrays are empty.
                //grantResults.length > 0
                //                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                if (isAllPremissions) {
//                    Log.i(TAG,"onRequestPermissionsResult granted");
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                    // 二维码扫码
                    isPress = true;
                    Intent intent = new Intent(view.getContext(), CaptureActivity.class);
//                    intent.putExtra("token",token);
                    startActivityForResult(intent, Constant.REQ_QR_CODE);

                } else {
//                    Log.i(TAG,"onRequestPermissionsResult denied");
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    showWaringDialog();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void showWaringDialog() {
        AlertDialog dialog = new AlertDialog.Builder(view.getContext())
                .setTitle("警告！")
                .setMessage("请前往设置->应用->PermissionDemo->权限中打开相关权限，否则功能可能无法正常运行！")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 一般情况下如果用户不授权的话，功能是无法运行的，做退出处理
//                        finish();
                    }
                }).show();
    }

}
