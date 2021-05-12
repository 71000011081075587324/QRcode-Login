package com.example.qrcodescanning_demo_androidx;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
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
import com.google.zxing.activity.CaptureActivity;
import com.google.zxing.util.Constant;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.widget.Toast.LENGTH_SHORT;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Button loginBtn;
    private EditText usernameEt;
    private EditText passwordEt;
    private TextView registerTv;
    private int whichClick;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_activity_login);
        getSupportActionBar().hide();
        findAllViewById();
        setAllOnClickListener();

    }

    @Override
    public void onClick(View v){

        switch (v.getId()){
            //登录：
            case R.id.btn_login:
                whichClick = R.id.btn_login;
                getPermission();

            break;
            case R.id.tv_register:
                whichClick = R.id.tv_register;
                getPermission();

            break;
        }

    }

    private void findAllViewById(){
        loginBtn = (Button)findViewById(R.id.btn_login);
        usernameEt = (EditText)findViewById(R.id.et_username);
        passwordEt = (EditText)findViewById(R.id.et_password);
        registerTv = (TextView)findViewById(R.id.tv_register);
    }


    private void setAllOnClickListener(){

        loginBtn.setOnClickListener(this);
        registerTv.setOnClickListener(this);
    }

    //发送登录请求，并处理逻辑
    private void login(){
        //请求接口前缀
//        String urlPrefix = "http://qrcode.free.idcfengye.com/user";
        String urlPrefix = "http://3a955v7566.wicp.vip/user";
        //获取输入框用户名
        String username = usernameEt.getText().toString();
        //获取输入框密码
        String password = passwordEt.getText().toString();


        //请求接口（使用okhttp）
        OkHttpUtils.get(urlPrefix + "/login?username=" + username +"&password=" + password,new OkHttpCallback(){
            @Override
            public void onFinish(String status, String msg) {
                super.onFinish(status, msg);
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Toast.makeText(LoginActivity.this, "登录成功" + msg, Toast.LENGTH_LONG).show();
////                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
////                                startActivity(intent);
//                            }
//                        });

                //使用Gson解析数据
                Gson gson = new Gson();
                Type userType = new TypeToken<ServerResponse<UserVo>>(){}.getType();
                ServerResponse<UserVo> serverResponse = gson.fromJson(msg, userType);

                int statusl = serverResponse.getStatus();

                if(statusl == 0){//登录成功
//                            //保存用户信息（使用SharedPrefrenece）
//                            //getSharedPreferences方法:   userinfo:自定义用户名 MODE_PRIVATE:代表仅本程序可以访问
//                            SharedPreferences sharedPreferences = LoginActivity.this.getSharedPreferences("userinfo",MODE_PRIVATE);
//                            SharedPreferences.Editor editor = sharedPreferences.edit();
//                            editor.putBoolean("isLogin",true);
//                            editor.putString("user",msg);
//                            editor.commit();

//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Toast.makeText(LoginActivity.this, "登录成功" + msg, Toast.LENGTH_LONG).show();
////                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
////                                startActivity(intent);
//                            }
//                        });

                    SharedPreferencesUtils sharedPreferencesUtils = SharedPreferencesUtils.getInstance(LoginActivity.this);
                    sharedPreferencesUtils.putBoolean("isLogin",true);
                    sharedPreferencesUtils.putString("user", msg);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);


                }else{

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(LoginActivity.this);
                            dialog.setTitle("登录失败");
                            dialog.setMessage(serverResponse.getMsg()+"，请重新输入。");
                            dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                            dialog.setNegativeButton(null,null);
                            dialog.show();
                        }
                    });

                }
            }
        });
    }


    private void register(){
        Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
        startActivityForResult(intent,RegisterActivity.REGISTER_SUCCESS);
    }


    //获取存储权限
    private void getPermission() {

        //判断系统版本
        if (Build.VERSION.SDK_INT>22){
            //判断是否获取相机和存储权限权限
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                //未获得权限,进行申请
                ActivityCompat.requestPermissions(LoginActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return;

            }else {
                switch (whichClick){
                    case R.id.btn_login:
                        login();
                        break;
                    case R.id.tv_register:
                        register();
                        break;
                }
            }
        }
    }


    //请求权限后的回调方法
    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                //判断是否获得所有权限
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                        switch (whichClick){
                            case R.id.btn_login:
                                login();
                                break;
                            case R.id.tv_register:
                                register();
                                break;
                        }
                    }else {
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case RegisterActivity.REGISTER_SUCCESS:
                if(data !=null){
                    Toast.makeText(LoginActivity.this,"注册成功", LENGTH_SHORT).show();
                    Bundle bundle = data.getExtras();
                    if(bundle != null){
                        usernameEt.setText(bundle.getString("username"));
                    }
                }
                break;
        }
    }


}