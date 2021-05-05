package com.example.qrcodescanning_demo_androidx;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.zxing.activity.CaptureActivity;
import com.google.zxing.util.Constant;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

//    private static final int RESULT_OK = -1;
    private ImageButton ibOpen;
    private static final int CAMERA_OK = 1;
//    public static final int REQ_PERM_EXTERNAL_STORAGE = 11004;
    private static boolean isPress = false;
    private static boolean isAllPremissions = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_activity_main);
        ibOpen = (ImageButton)findViewById(R.id.ib_open);
        ibOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isPress){
                    getPermission();
//                  startQrCode();
                }

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        isPress = false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//      Toast.makeText(this,"此二维码无效，请重新扫描",Toast.LENGTH_SHORT).show();

        //扫描结果回调
//        if (requestCode == Constant.REQ_QR_CODE && resultCode == CaptureActivity.RESULT_OK) {
//            Bundle bundle = data.getExtras();
//            String scanResult = bundle.getString(Constant.INTENT_EXTRA_KEY_QR_SCAN);
//
////            Toast.makeText(this,"此二维码有效，请重新扫描",Toast.LENGTH_SHORT).show();
//
//            //判断扫描二维码是否是网址，并将扫描出的信息显示出来
//            String regex = "(((https|http)?://)?([a-z0-9]+[.])|(www.))"
//                    + "\\w+[.|\\/]([a-z0-9]{0,})?[[.]([a-z0-9]{0,})]+((/[\\S&&[^,;\u4E00-\u9FA5]]+)+)?([.][a-z0-9]{0,}+|/?)";//设置正则表达式
//
//            Pattern pat = Pattern.compile(regex.trim());//比对
//            Matcher mat = pat.matcher(scanResult.trim());
//            if(mat.matches()){
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setData(Uri.parse(scanResult));
//                startActivity(intent);
//            }else{
//                Toast.makeText(this,"此二维码无效，请重新扫描",Toast.LENGTH_SHORT).show();
//            }
//
//        }
        if (requestCode == Constant.REQ_QR_CODE && resultCode == CaptureActivity.RESULT_OK) {
            Toast.makeText(this,"此二维码无效，请重新扫描",Toast.LENGTH_SHORT).show();
        }
    }

    //获取相机和存储权限
    private void getPermission() {

         //判断系统版本
         if (Build.VERSION.SDK_INT>22){
            //判断是否获取相机和存储权限权限
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    android.Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            //未获得权限,进行申请
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},CAMERA_OK);
                        return;

            }else {
                //说明已经获取到摄像头权限了
//                Log.i("MainActivity","已经获取了权限");
                // 二维码扫码
                isPress = true;
                Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
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
                    Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
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
        AlertDialog dialog = new AlertDialog.Builder(this)
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


//    // 开始扫码
//    private void startQrCode() {
//        //判断系统版本
//        if (Build.VERSION.SDK_INT > 22) {
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//                //已经打开相机和文件读写权限
//                //启动二维码扫码
//                Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
//                startActivityForResult(intent, Constant.REQ_QR_CODE);
//            } else {
//                // 申请相机权限
//                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
////                    // 申请权限
////                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission
////                            .CAMERA)) {
////                        Toast.makeText(this, "请至权限中心打开本应用的相机访问权限", Toast.LENGTH_SHORT).show();
////                    }
//                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, Constant.REQ_PERM_CAMERA);
//                    return;
//                }
//                // 申请文件读写权限（部分朋友遇到相册选图需要读写权限的情况，这里一并写一下）
//                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
////                    // 申请权限
////                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission
////                            .WRITE_EXTERNAL_STORAGE)) {
////                        Toast.makeText(this, "请至权限中心打开本应用的文件读写权限", Toast.LENGTH_SHORT).show();
////                    }
//                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQ_PERM_EXTERNAL_STORAGE);
//                    return;
//                }
//            }
//        }
//    }

}