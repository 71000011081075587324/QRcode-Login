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
     * ????????????????????????Fragment???View???????????????????????????Fragment??????????????????????????????View??????
     * ???????????????????????????
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return  ??????????????????view
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
     * ???Fragment?????????Activity?????????????????????????????????
     * ???????????????????????????????????????
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

        //????????????
        String path = sharedPreferencesUtils.readString(sharedPreferencesUtils.readString("user") + "userIconPath");
        if(path != null && !path.equals("")){
            Bitmap bitmap = BitmapFactory.decodeFile(path);
//            ivUserIcon.setImageDrawable(new BitmapDrawable(view.getContext().getResources(), bitmap));
            layout.setBackground(new BitmapDrawable(view.getContext().getResources(), bitmap));//???bitmap??????drawable,layout???xml???????????????layout
        }

    }

    //???????????????????????????
    private void getPermission() {

        //??????????????????
        if (Build.VERSION.SDK_INT>22){
            //?????????????????????????????????????????????
            if (ContextCompat.checkSelfPermission(view.getContext(),
                    android.Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(view.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                //???????????????,????????????
                ActivityCompat.requestPermissions((Activity) view.getContext(),
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},CAMERA_OK);
                return;

            }else {
                //???????????????????????????????????????
//                Log.i("MainActivity","?????????????????????");
                // ???????????????
                isPress = true;
                Intent intent = new Intent(view.getContext(), CaptureActivity.class);
//                intent.putExtra("token",token);
                startActivityForResult(intent, Constant.REQ_QR_CODE);
            }
        }
    }
    //
    //??????????????????????????????
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        switch (requestCode) {
            case CAMERA_OK: {
                isAllPremissions = true;

                //??????????????????????????????
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

                    // ???????????????
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
                .setTitle("?????????")
                .setMessage("???????????????->??????->PermissionDemo->?????????????????????????????????????????????????????????????????????")
                .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // ???????????????????????????????????????????????????????????????????????????????????????
//                        finish();
                    }
                }).show();
    }

}
