package com.lyl.baselibrary.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;

import com.lyl.baselibrary.R;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * @Author lyl
 * @Date 2022/05/24
 */
public abstract class BaseVmDialog<DB extends ViewDataBinding> extends Dialog implements LifecycleOwner {

    public DB dataBinding;
    private LifecycleRegistry lifecycleRegistry;
    public BaseVmDialog(@NonNull Context context) {
        super(context, R.style.AppDialog);
    }
    public BaseVmDialog(@NonNull Context context,int themeResId) {
        super(context,themeResId);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       lifecycleRegistry = new LifecycleRegistry(this);
        dataBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), getLayoutId(), null, false);

        setContentView(dataBinding.getRoot());
        initView(savedInstanceState);
        initData();
    }

    /**
     * 获取资源 ID
     */
    protected abstract int getLayoutId();

    protected abstract void initView(Bundle savedInstanceState);

    protected abstract void initData();


    @Override
    public void show() {
        super.show();
        lifecycleRegistry.setCurrentState(Lifecycle.State.CREATED);
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START);
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME);

    }

    @Override
    public void dismiss() {
        super.dismiss();
        removeCompositeDisposable();
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE);
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP);
        lifecycleRegistry.setCurrentState(Lifecycle.State.DESTROYED);
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return lifecycleRegistry;
    }
    private CompositeDisposable compositeDisposable;

    public void removeCompositeDisposable() {
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
    }
    public void addDisposable(Observable<?> observable, DisposableObserver observer) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(
                observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(observer));

    }
}
