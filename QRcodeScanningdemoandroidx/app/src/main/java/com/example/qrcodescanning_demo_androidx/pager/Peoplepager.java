package com.example.qrcodescanning_demo_androidx.pager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.qrcodescanning_demo_androidx.AlterActivity;
import com.example.qrcodescanning_demo_androidx.LoginActivity;
import com.example.qrcodescanning_demo_androidx.R;
import com.example.qrcodescanning_demo_androidx.base.BasePager;
import com.example.qrcodescanning_demo_androidx.utils.SharedPreferencesUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Peoplepager extends BasePager {

    private View view;
    private Button btnChangePaswrd;
    private Button btnLoginOut;
    private Button btnOutBackground;
    private Button btnSetBackground;
    private TextView tvUsername;
    private ImageView ivUserIcon;
    LinearLayout layout;


    private ImageView ivTest;

    private SharedPreferencesUtils sharedPreferencesUtils;

    public Peoplepager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        view = View.inflate(context, R.layout.module_fragment_peoplepager,null);
        return view;
    }



    @Override
    public void initData() {
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
        btnChangePaswrd = (Button)view.findViewById(R.id.btn_changepassword);
        btnLoginOut = (Button)view.findViewById(R.id.btn_loginout);
        tvUsername = (TextView)view.findViewById(R.id.tv_username);
        btnChangePaswrd = (Button)view.findViewById(R.id.btn_changepassword);
        btnSetBackground = (Button)view.findViewById(R.id.btn_setbackground);
        btnOutBackground = (Button)view.findViewById(R.id.btn_outbackground);
        ivUserIcon = (ImageView)view.findViewById(R.id.iv_usericon);
        ivTest = (ImageView)view.findViewById(R.id.iv_test);
        layout = (LinearLayout)view.findViewById(R.id.ll_people);
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

        if(sharedPreferencesUtils.readBoolean("isLogin")){
            tvUsername.setText(sharedPreferencesUtils.readString("user"));
        }

        btnLoginOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> keyOut = new ArrayList<String>();
                keyOut.add(sharedPreferencesUtils.readString("user") + "userIconPath");
                //退出登录，清空sharedPreference中的数据
                sharedPreferencesUtils.retainPartial(keyOut);
                Intent intent = new Intent(view.getContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        btnChangePaswrd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), AlterActivity.class);
                startActivity(intent);
            }
        });
        btnSetBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 0001);
            }
        });

        btnOutBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferencesUtils.delete(sharedPreferencesUtils.readString("user") + "userIconPath");
                layout.setBackground(null);
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



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0001 && resultCode == Activity.RESULT_OK
                && data != null) {
            Uri selectedImage = data.getData();//返回的是uri
            String [] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = view.getContext().getContentResolver().query(selectedImage, filePathColumn, null,
                    null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String path = cursor.getString(columnIndex);

            sharedPreferencesUtils.putString(sharedPreferencesUtils.readString("user") + "userIconPath",path);

            Bitmap bitmap = BitmapFactory.decodeFile(path);
//            ivUserIcon.setBackground(new BitmapDrawable(view.getContext().getResources(), bitmap));
//            ivUserIcon.setImageDrawable(new BitmapDrawable(view.getContext().getResources(), bitmap));

            layout.setBackground(new BitmapDrawable(view.getContext().getResources(), bitmap));//把bitmap转为drawable,layout为xml文件里的主layout

        }
    }



}
