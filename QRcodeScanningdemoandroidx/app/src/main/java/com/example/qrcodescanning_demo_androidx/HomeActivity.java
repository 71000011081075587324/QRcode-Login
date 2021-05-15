package com.example.qrcodescanning_demo_androidx;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.example.qrcodescanning_demo_androidx.base.BasePager;
import com.example.qrcodescanning_demo_androidx.pager.Peoplepager;
import com.example.qrcodescanning_demo_androidx.pager.QrPager;
import com.example.qrcodescanning_demo_androidx.utils.SharedPreferencesUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HomeActivity extends FragmentActivity {

    private RadioGroup barRg;

    private ArrayList<BasePager> basePagers;

//    private int position;

    private SharedPreferencesUtils sharedPreferencesUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_activity_home);
        findAllViewById();

        //实例化所有Fragement页面
        basePagers = new ArrayList<BasePager>();
        basePagers.add(new QrPager(this));
        basePagers.add(new Peoplepager(this));

        barRg.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
        sharedPreferencesUtils = SharedPreferencesUtils.getInstance(HomeActivity.this);
        switch (sharedPreferencesUtils.readInt("position")){
            default:
                barRg.check(R.id.rb_home);
                break;
            case 0:
                barRg.check(R.id.rb_home);
                break;
            case 1:
                barRg.check(R.id.rb_my);
                break;
        }

    }



    private void  findAllViewById(){

        barRg = findViewById(R.id.rg_main);

    }


    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId){
                default:
                    sharedPreferencesUtils.putInt("position",0);
//                    position = 0;
                    break;
                case R.id.rb_my:
                    sharedPreferencesUtils.putInt("position",1);
//                    position = 1;
                    break;
            }

            setFragment();

        }



    }

    private void setFragment(){
        FragmentManager fragmentManager =  getSupportFragmentManager();  //得到fragmentManager
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fl_main,getBasePager()
//                new Fragment(){
//            @Nullable
//            @org.jetbrains.annotations.Nullable
//            @Override
//            public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
//                BasePager basePager = getBasePager();
//                if(basePager != null){
//                    return basePager.rootView;
//                }
//                return null;    //这行代码实际不会执行
//            }
//        }
        );
        fragmentTransaction.commit();
    }

    public BasePager getBasePager() {
        BasePager basePager = basePagers.get(sharedPreferencesUtils.readInt("position"));     //获得具体的页面信息
        //执行页面初始化数据
        if(basePager != null && basePager.isInitData()){
            basePager.setInitData(true);
            basePager.initData();
        }
        return basePager;
    }

}