package com.lyl.baselibrary;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.ProcessLifecycleOwner;

import com.blankj.utilcode.util.SPStaticUtils;
import com.blankj.utilcode.util.SPUtils;
import com.lyl.baselibrary.base.BaseApplicationObserver;




import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

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
