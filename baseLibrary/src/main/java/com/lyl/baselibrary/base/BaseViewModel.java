package com.lyl.baselibrary.base;

import androidx.annotation.StringRes;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.blankj.utilcode.util.ToastUtils;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class BaseViewModel extends ViewModel {
    private CompositeDisposable compositeDisposable;

    public MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public void addDisposable(Observable<?> observable, DisposableObserver observer) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(
                observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(observer));

    }


    public void addDisposable2(Disposable disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposable);
    }


    public void removeCompositeDisposable() {
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        removeCompositeDisposable();
    }


    protected void toast(String msg) {
        ToastUtils.showShort(msg);
    }


    protected void toast(@StringRes int resId) {
        ToastUtils.showShort(resId);
    }



}
