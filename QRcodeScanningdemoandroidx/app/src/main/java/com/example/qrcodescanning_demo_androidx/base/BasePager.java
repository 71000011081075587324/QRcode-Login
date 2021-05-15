package com.example.qrcodescanning_demo_androidx.base;

import android.content.Context;
import android.view.View;

import androidx.fragment.app.Fragment;

//扫一扫，我的 的基类
public abstract class BasePager extends Fragment {
    /**
     * 上下文
     */
    public Context context;

    /**
     * 视图，有各个子页面实例化的结果
     */
    public View rootView;

    private boolean isInitData = false;

    public BasePager(Context context){
        this.context = context;
        rootView = initView();
    }

    public boolean isInitData() {
        return isInitData;
    }

    public void setInitData(boolean initData) {
        isInitData = initData;
    }

    /**
     * 强制孩子实现该方法，实现特定的效果
     * @return
     */
    public abstract View initView();

    /**
     * 当孩子需要初始化数据的时候，重写该方法，用于请求数据，或者显示数据
     */
    public void initData(){

    }

}
