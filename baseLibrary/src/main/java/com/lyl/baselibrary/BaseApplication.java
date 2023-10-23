package com.lyl.baselibrary;

import android.app.Application;

import com.blankj.utilcode.util.SPStaticUtils;
import com.blankj.utilcode.util.SPUtils;

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

    public static BaseApplication context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        SPStaticUtils.setDefaultSPUtils(SPUtils.getInstance());

    }




    public static BaseApplication getContext() {
        return context;
    }



    public static CompositeDisposable compositeDisposable;

    public static void removeCompositeDisposable() {
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
    }
    public static void addDisposable(Observable<?> observable, DisposableObserver observer) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(
                observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(observer));

    }
}
