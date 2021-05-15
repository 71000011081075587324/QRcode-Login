package com.example.qrcodescanning_demo_androidx;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.qrcodescanning_demo_androidx.utils.OkHttpCallback;
import com.example.qrcodescanning_demo_androidx.utils.OkHttpUtils;
import com.example.qrcodescanning_demo_androidx.utils.SharedPreferencesUtils;
import com.example.qrcodescanning_demo_androidx.vo.ServerResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class AlterActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView ivBack;
    private Button btnFinish;
    private TextView tvUsername;
    private EditText etPrimaryPassword;
    private EditText etNewPassword;
    private SharedPreferencesUtils sharedPreferencesUtils;
    private AlertDialog.Builder dialog;
    private LinearLayout layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        LayoutInflater factory = LayoutInflater.from(this);
//        View titleEntryView =  factory.inflate(R.layout.title_bar,null);

        setContentView(R.layout.module_activity_alter);
        getSupportActionBar().hide();
        findAllViewById();
        setAllOnClickListener();
        sharedPreferencesUtils = SharedPreferencesUtils.getInstance(AlterActivity.this);
        tvUsername.setText(sharedPreferencesUtils.readString("user"));

        //设置背景
        String path = sharedPreferencesUtils.readString(sharedPreferencesUtils.readString("user") + "userIconPath");
        if(path != null && !path.equals("")){
            Bitmap bitmap = BitmapFactory.decodeFile(path);
//            ivUserIcon.setImageDrawable(new BitmapDrawable(view.getContext().getResources(), bitmap));
            layout.setBackground(new BitmapDrawable(this.getResources(), bitmap));//把bitmap转为drawable,layout为xml文件里的主layout
        }

    }

    private void findAllViewById(){
        ivBack = (ImageView)findViewById(R.id.iv_back);
        btnFinish = (Button)findViewById(R.id.btn_finish);
        tvUsername = (TextView)findViewById(R.id.tv_username);
        etPrimaryPassword = (EditText)findViewById(R.id.et_primarypassword);
        etNewPassword = (EditText)findViewById(R.id.et_newpassword);
        layout = (LinearLayout)findViewById(R.id.ll_alter);
    }

    private void setAllOnClickListener(){
        ivBack.setOnClickListener(this);
        btnFinish.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_finish:
                v.setSaveEnabled(false);    //设置页面不自动保存view的状态，这样后续的et.setText()才能生效
                alterFinish();
                break;
        }
    }

    private void alterFinish() {
        //请求接口前缀
        String urlPrefix = "http://3a955v7566.wicp.vip/user";

        //获取存储的用户名
        String username = sharedPreferencesUtils.readString("user");
        //获取输入框中原密码
        String newPassword = etNewPassword.getText().toString();
        //获取输入框中新密码
        String primaryPassword = etPrimaryPassword.getText().toString();
        //获取token
        String token = sharedPreferencesUtils.readString("token");


        RequestBody body = new FormBody.Builder().add("username",username).add("newPassword", newPassword.equals("") ? "" : String.valueOf(newPassword.hashCode())).add("primaryPassword", primaryPassword.equals("") ? "" : String.valueOf(primaryPassword.hashCode())).build();

        OkHttpUtils.postWithToken(urlPrefix + "/alterlogin",body,token,new OkHttpCallback(){
            @Override
            public void onFinish(String status, String msg) {
                super.onFinish(status, msg);

                //使用Gson解析数据
                Gson gson = new Gson();
                Type userType = new TypeToken<ServerResponse<String>>(){}.getType();
                ServerResponse<String> serverResponse = gson.fromJson(msg, userType);

                int statusl = serverResponse.getStatus();


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog = new AlertDialog.Builder(AlterActivity.this);
                            if(statusl == 0){
                                dialog.setTitle("修改成功");
                                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        etNewPassword.setText("");
                                        etPrimaryPassword.setText("");
                                        dialog.dismiss();

                                    }
                                });
                                dialog.setNegativeButton(null, null);
                                dialog.show();
                            }else{
                                dialog.setTitle("修改失败");
                                dialog.setMessage(serverResponse.getMsg() + "，请重新输入");
                                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        dialog.dismiss();

                                    }
                                });
                                dialog.setNegativeButton(null, null);
                                dialog.show();
                            }
                        }

                    });
            }

        });

    }
}