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
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.widget.Toast.LENGTH_SHORT;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Button loginBtn;
    private EditText usernameEt;
    private EditText passwordEt;
    private TextView registerTv;
    private int whichClick;
    private SharedPreferencesUtils sharedPreferencesUtils;

    private AlertDialog.Builder dialog;

    private final String TAG = LoginActivity.class.getSimpleName();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_activity_login);
        getSupportActionBar().hide();
        sharedPreferencesUtils = SharedPreferencesUtils.getInstance(LoginActivity.this);
        if(sharedPreferencesUtils.readBoolean("isLogin") == true){
            tokenLogin();
            if(sharedPreferencesUtils.readBoolean("tokenVerity") == true){
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
        findAllViewById();
        setAllOnClickListener();

    }

    @Override
    public void onClick(View v){

        switch (v.getId()){
            //?????????
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

    private void tokenLogin(){
        String urlPrefix = "http://3a955v7566.wicp.vip/user";
        String token =  sharedPreferencesUtils.readString("token");
        Log.d(TAG,"token = " + token);
        if(token != null){
            RequestBody body = new FormBody.Builder().add("token", token).build();

            OkHttpUtils.post(urlPrefix + "/tokenlogin",
                    body,
                    new OkHttpCallback(){
                        @Override
                        public void onFinish(String status, String msg) {
                            super.onFinish(status, msg);

                            Gson gson = new Gson();
//                Type userType = new TypeToken<ServerResponse<UserVo>>(){}.getType();
//                ServerResponse<UserVo> serverResponse = gson.fromJson(msg, userType);
                            Type userType = new TypeToken<ServerResponse<String>>(){}.getType();
                            ServerResponse<String> serverResponse = gson.fromJson(msg, userType);

                            if(serverResponse.getStatus() == 0){
                                sharedPreferencesUtils.putBoolean("tokenVerity",true);
                            }else{
                                sharedPreferencesUtils.putBoolean("tokenVerity",false);
                            }
                        }
                    });
        }

    }

    //????????????????????????????????????
    private void login(){
        //??????????????????
//        String urlPrefix = "http://qrcode.free.idcfengye.com/user";
        String urlPrefix = "http://3a955v7566.wicp.vip/user";
        //????????????????????????
        String username = usernameEt.getText().toString();
        //?????????????????????
        String password = passwordEt.getText().toString();

//        Log.d(TAG,"password = " + password);
//        boolean test1 = (password == "");
//        boolean test2 = (password == null);
//        Log.d(TAG,"test1 = " + test1);
//        Log.d(TAG,"test2 = " + test2);

//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("username",username);
//            jsonObject.put("password",password.hashCode());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        RequestBody body = new FormBody.Builder().add("username",username).add("password", password.equals("") ? "" : String.valueOf(password.hashCode())).build();

        //?????????????????????okhttp???
        OkHttpUtils.post(urlPrefix + "/login",body,new OkHttpCallback(){
            @Override
            public void onFinish(String status, String msg) {
                super.onFinish(status, msg);
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Toast.makeText(LoginActivity.this, "????????????" + msg, Toast.LENGTH_LONG).show();
////                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
////                                startActivity(intent);
//                            }
//                        });

                //??????Gson????????????
                Gson gson = new Gson();
//                Type userType = new TypeToken<ServerResponse<UserVo>>(){}.getType();
//                ServerResponse<UserVo> serverResponse = gson.fromJson(msg, userType);
                Type userType = new TypeToken<ServerResponse<String>>(){}.getType();
                ServerResponse<String> serverResponse = gson.fromJson(msg, userType);

                int statusl = serverResponse.getStatus();

                if(statusl == 0){//????????????
//                            //???????????????????????????SharedPrefrenece???
//                            //getSharedPreferences??????:   userinfo:?????????????????? MODE_PRIVATE:??????????????????????????????
//                            SharedPreferences sharedPreferences = LoginActivity.this.getSharedPreferences("userinfo",MODE_PRIVATE);
//                            SharedPreferences.Editor editor = sharedPreferences.edit();
//                            editor.putBoolean("isLogin",true);
//                            editor.putString("user",msg);
//                            editor.commit();

//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Toast.makeText(LoginActivity.this, "????????????" + msg, Toast.LENGTH_LONG).show();
////                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
////                                startActivity(intent);
//                            }
//                        });
//                    JwtUtil jwtUtil = JwtUtil.getInstance();
                    Log.d(TAG,"?????????msg:" + msg);
                    Log.d(TAG,"?????????msg-data:" + serverResponse.getData());
//                    SharedPreferencesUtils sharedPreferencesUtils = SharedPreferencesUtils.getInstance(LoginActivity.this);
                    sharedPreferencesUtils.putBoolean("isLogin",true);
                    sharedPreferencesUtils.putString("user", username);
                    sharedPreferencesUtils.putString("token",serverResponse.getData());
//                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);


                }else{

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            dialog = new AlertDialog.Builder(LoginActivity.this);
                            dialog.setTitle("????????????");
                            dialog.setMessage(serverResponse.getMsg()+"?????????????????????");
                            dialog.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
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


    //??????????????????
    private void getPermission() {

        //??????????????????
        if (Build.VERSION.SDK_INT>22){
            //?????????????????????????????????????????????
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                //???????????????,????????????
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


    //??????????????????????????????
    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                //??????????????????????????????
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
                .setTitle("?????????")
                .setMessage("???????????????->??????->PermissionDemo->?????????????????????????????????????????????????????????????????????")
                .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // ???????????????????????????????????????????????????????????????????????????????????????
//                        finish();
                        dialog.dismiss();
                    }
                }).show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case RegisterActivity.REGISTER_SUCCESS:
                if(data !=null){
                    Toast.makeText(LoginActivity.this,"????????????", LENGTH_SHORT).show();
                    Bundle bundle = data.getExtras();
                    if(bundle != null){
                        usernameEt.setText(bundle.getString("username"));
                        passwordEt.setText("");
                    }
                }
                break;
        }
    }

    @Override
    protected void onPause() {
        if(dialog != null){
            dialog = null;
        }
        super.onPause();
    }

}