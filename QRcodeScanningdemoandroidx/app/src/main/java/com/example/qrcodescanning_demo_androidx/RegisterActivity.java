package com.example.qrcodescanning_demo_androidx;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qrcodescanning_demo_androidx.utils.OkHttpCallback;
import com.example.qrcodescanning_demo_androidx.utils.OkHttpUtils;
import com.example.qrcodescanning_demo_androidx.utils.SharedPreferencesUtils;
import com.example.qrcodescanning_demo_androidx.vo.ServerResponse;
import com.example.qrcodescanning_demo_androidx.vo.UserVo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import static android.widget.Toast.*;
import static android.widget.Toast.LENGTH_LONG;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView titleTv;
    private Button registerBtn;
    private EditText usernameEt;
    private EditText passwordEt;
    public static final int REGISTER_SUCCESS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater factory = LayoutInflater.from(this);
        View titleEntryView =  factory.inflate(R.layout.title,null);
        setContentView(R.layout.module_activity_register);
        findAllViewById();
        setAllOnClickListener();
        getSupportActionBar().hide();
        titleTv.setText("注册");
        TextPaint paint = titleTv.getPaint();
        paint.setFakeBoldText(true);
    }

    private void findAllViewById(){
        titleTv = (TextView) findViewById(R.id.tv_title);
        registerBtn = (Button)findViewById(R.id.btn_register);
        usernameEt = (EditText)findViewById(R.id.et_reg_username);
        passwordEt = (EditText)findViewById(R.id.et_reg_password);
    }

    private void setAllOnClickListener(){
        registerBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_register:
                getPermission();
        }
    }


    private void register() {
        //请求接口前缀
        String urlPrefix = "http://3a955v7566.wicp.vip/user";
        //获取输入框用户名
        String username = usernameEt.getText().toString();
        //获取输入框密码
        String password = passwordEt.getText().toString();


        //请求接口（使用okhttp）
        OkHttpUtils.get(urlPrefix + "/register?username=" + username +"&password=" + password,new OkHttpCallback(){
            @Override
            public void onFinish(String status, String msg) {
                super.onFinish(status, msg);

                Gson gson = new Gson();
                Type userType = new TypeToken<ServerResponse<UserVo>>() {
                }.getType();
                ServerResponse<UserVo> serverResponse = gson.fromJson(msg, userType);

                int statusl = serverResponse.getStatus();


                if (statusl == 0) {
                    Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                    intent.putExtra("username",username);
                    setResult(RESULT_OK,intent);
                    finish();

//                    SharedPreferencesUtils sharedPreferencesUtils = SharedPreferencesUtils.getInstance(RegisterActivity.this);
//                    sharedPreferencesUtils.putString("username", username);


                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(RegisterActivity.this);
                            dialog.setTitle("登录失败");
                            dialog.setMessage(serverResponse.getMsg() + "，请重新输入。");
                            dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                            dialog.setNegativeButton(null, null);
                            dialog.show();
                        }
                    });
                }

            }
        });
    }



//     @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
//        setResult(RESULT_CANCELED,intent);
//    }





    //获取存储权限
    private void getPermission() {

        //判断系统版本
        if (Build.VERSION.SDK_INT>22){
            //判断是否获取相机和存储权限权限
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                //未获得权限,进行申请
                ActivityCompat.requestPermissions(RegisterActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return;

            }else {
                 register();
            }
        }
    }



    //请求权限后的回调方法
    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                //判断是否获得所有权限
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    register();
                } else {
                    showWaringDialog();
                }
                return;
            }
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

}