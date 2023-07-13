package com.lyl.baselibrary.net;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.LogUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 作者：梁亚龙
 * 时间：2022/12/9
 * 描述：
 **/
public class RetrofitFactory {
    private  final int TIME_OUT_SECNOD = 15;
    private  OkHttpClient.Builder mBuilder;

    private  Retrofit getDownloadRetrofit(DownloadListener downloadListener) {
        Interceptor headerInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                Request.Builder requestBuilder = originalRequest.newBuilder()
                        .addHeader("Accept-Encoding", "gzip")
                        .method(originalRequest.method(), originalRequest.body());
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        };

        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(@NonNull String message) {
                if (!TextUtils.isEmpty(message)) {
                    LogUtils.d(message);
                }
            }
        });
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        if (null == mBuilder) {
            mBuilder = new OkHttpClient.Builder()
                    .connectTimeout(TIME_OUT_SECNOD, TimeUnit.SECONDS)
                    .readTimeout(TIME_OUT_SECNOD, TimeUnit.SECONDS)
                    .writeTimeout(TIME_OUT_SECNOD, TimeUnit.SECONDS)
                    .addInterceptor(headerInterceptor)
                    .addInterceptor(logInterceptor)
                    .addInterceptor(new DownloadInterceptor(downloadListener));
        }

        return new Retrofit.Builder()
                .baseUrl("http://imtt.dd.qq.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .client(mBuilder.build())
                .build();
    }
    /**
     * 下载文件请求
     */
    public  void downloadFile(String url, long startPos, DownloadListener downloadListener, Observer<ResponseBody> observer) {
        getDownloadRetrofit(downloadListener).create(BaseApi.class).downloadFile("bytes=" + startPos + "-", url)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

}

