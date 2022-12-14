package com.lyl.baselibrary.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.fragment.NavHostFragment;

import com.blankj.utilcode.util.ToastUtils;


import java.lang.reflect.ParameterizedType;

/**
 * @Author lyl
 * @Date 2022/05/24
 */

public abstract class BaseVmFragment<VM extends BaseViewModel, DB extends ViewDataBinding> extends Fragment {

    private AppCompatActivity mActivity;





    public VM viewModel;

    public DB dataBinding;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mActivity = (AppCompatActivity) context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (setNavId() != -1) {
            NavBackStackEntry backStackEntry = NavHostFragment.findNavController(this)
                    .getBackStackEntry(setNavId());
            viewModel = new ViewModelProvider(backStackEntry).get(getVMClass());
        } else {
            viewModel = new ViewModelProvider(requireActivity()).get(getVMClass());
        }

        dataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        dataBinding.setLifecycleOwner(this.getViewLifecycleOwner());
        return dataBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(savedInstanceState);
        initLiveData();
        initData();
    }


    /**
     * ???????????????????????? Class ??????
     */
    public Class<VM> getVMClass() {
        //  ??????????????? Class ??????????????????????????????????????????????????? void????????????????????? Type
        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
        //  ?????????????????????????????????????????? Type ???????????????()??????????????????????????????Class???????????? 0
        return (Class) type.getActualTypeArguments()[0];//<VM>
    }

    protected int setNavId() {
        return -1;
    }

    protected abstract int getLayoutId();

    protected abstract void initView(Bundle savedInstanceState);

    protected abstract void initData();


    @CallSuper
    protected void initLiveData() {
        if (viewModel == null) return;
        viewModel.isLoading.observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading) {
                showLoading();
            } else {
                hideLoading();
            }
        });
    }


    public void toActivity(Class<? extends Activity> clazz) {
        toActivity(clazz, null);
    }


    public void toActivity(Class<? extends Activity> clazz, Bundle args) {
        Intent intent = new Intent(requireActivity(), clazz);
        if (null != args) {
            intent.putExtras(args);
        }
        startActivity(intent);
    }


    public void showLoading() {
        hideLoading();

    }


    public void hideLoading() {

    }


    protected void toast(@StringRes int resId) {
        ToastUtils.showShort(resId);
    }


    protected void toast(String msg) {
        ToastUtils.showShort(msg);
    }

    protected void toastLong(@StringRes int resId) {
        ToastUtils.showLong(resId);
    }


    protected void toastLong(String msg) {
        ToastUtils.showLong(msg);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (dataBinding != null) {
            dataBinding.unbind();
            dataBinding = null;
        }
    }
}

