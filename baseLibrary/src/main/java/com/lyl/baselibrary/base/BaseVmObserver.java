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

public abstract class BaseVmObserver<T> extends BaseObserver<BaseResponse<T>> {


    public BaseVmObserver(BaseViewModel vm) {
        super(vm);
    }

    public BaseVmObserver(BaseViewModel vm, boolean isShowDialog) {
        super(vm, isShowDialog);
    }
    @Override
    public void onNext(@NonNull BaseResponse<T> response) {
        if (response.isSuccess()) {
            onSuccess(response);
        } else {
            onError(new BaseException(response.getMsg(), response.getCode()));
        }
    }


}
