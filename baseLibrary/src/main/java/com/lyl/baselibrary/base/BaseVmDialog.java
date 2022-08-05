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

import com.lyl.baselibrary.R;

/**
 * @Author lyl
 * @Date 2022/05/24
 */
public abstract class BaseVmDialog<DB extends ViewDataBinding> extends Dialog {

    public DB dataBinding;

    public BaseVmDialog(@NonNull Context context) {
        super(context, R.style.AppDialog);
    }
    public BaseVmDialog(@NonNull Context context,int themeResId) {
        super(context,themeResId);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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


}
