package com.lyl.baselibrary;

import android.app.Application;

import com.blankj.utilcode.util.SPStaticUtils;
import com.blankj.utilcode.util.SPUtils;


/**
 * @Author lyl
 * @Date 2022/05/24
 */
public class BaseApplication extends Application {

    private static BaseApplication context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        SPStaticUtils.setDefaultSPUtils(SPUtils.getInstance());
    }




    public static BaseApplication getContext() {
        return context;
    }




}
