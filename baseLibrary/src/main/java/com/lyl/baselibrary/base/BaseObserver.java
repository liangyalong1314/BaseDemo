package com.lyl.baselibrary.base;


import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.JsonParseException;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;

import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.text.ParseException;

import io.reactivex.rxjava3.observers.DisposableObserver;
import retrofit2.HttpException;

/**
 * @Author lyl
 * @Date 2022/05/24
 */

public abstract class BaseObserver<T> extends DisposableObserver<T> {

    /**
     * 是否需要显示加载框
     */
    public boolean isShowDialog;
    public final BaseViewModel vm;
    public boolean isShowShortFailure;

    public BaseObserver(BaseViewModel vm) {
        this.vm = vm;
    }

    public BaseObserver() {
        this.vm = null;
    }

    public BaseObserver(BaseViewModel vm, boolean isShowDialog) {
        this(vm, isShowDialog, true);
    }

    public BaseObserver(BaseViewModel vm, boolean isShowDialog, boolean isShowShortFailure) {
        this.vm = vm;
        this.isShowDialog = isShowDialog;
        this.isShowShortFailure = isShowShortFailure;
    }


    @Override
    protected void onStart() {
        if (vm != null && isShowDialog) {
            vm.isLoading.postValue(true);
        }
    }


    @Override
    public void onNext(@NonNull T response) {
        onSuccess(response);

    }


    @Override
    public void onError(@NonNull Throwable e) {
        if (vm != null && isShowDialog) {
            vm.isLoading.postValue(false);
        }
        BaseException be;

        if (e instanceof BaseException) {
            be = (BaseException) e;
        } else if (e instanceof HttpException) {
            //   HTTP错误
            be = new BaseException(BaseException.BAD_NETWORK_MSG, e, BaseException.BAD_NETWORK);
        } else if (e instanceof ConnectException
                || e instanceof UnknownHostException) {
            //   连接错误
            be = new BaseException(BaseException.CONNECT_ERROR_MSG, e, BaseException.CONNECT_ERROR);
        } else if (e instanceof InterruptedIOException) {
            //  连接超时
            be = new BaseException(BaseException.CONNECT_TIMEOUT_MSG, e, BaseException.CONNECT_TIMEOUT);
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            //  解析错误
            be = new BaseException(BaseException.PARSE_ERROR_MSG, e, BaseException.PARSE_ERROR);
        } else {
            be = new BaseException(BaseException.OTHER_MSG, e, BaseException.OTHER);
        }

        if (be.getCode() == BaseException.BAD_TOKEN) {
            //  token 失效
            EventBus.getDefault().post(new BadTokenEvent(be.getMsg()));
        } else {
            onFailure(be);
        }
    }


    @Override
    public void onComplete() {
        //  通过 viewmodel 加载框的显示或者隐藏
        //  viewmodel 只是控制器,不直接操作 UI
        if (vm != null && isShowDialog) {
            vm.isLoading.postValue(false);
        }
    }


    public abstract void onSuccess(T data);


    public void onFailure(BaseException error) {
        if (isShowShortFailure) {
            ToastUtils.showShort(error.getMsg());
        }
    }

}
