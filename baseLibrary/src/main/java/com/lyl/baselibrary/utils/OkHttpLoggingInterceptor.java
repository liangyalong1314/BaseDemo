package com.lyl.baselibrary.utils;

import android.util.Log;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.LogUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

/**
 * @Author lyl
 * @Date 2022/07/28
 */
public class OkHttpLoggingInterceptor implements Interceptor {
    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        //chain里面包含了request和response
        Request request = chain.request();
        //获取requestBody
        String requestString = "";
        RequestBody requestBody = request.body();
        if (requestBody != null) {
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);
            requestString = buffer.readUtf8();

            if (requestString.contains("Content-Type: image/*")){
                requestString=  requestString.substring(0,requestString.indexOf("\n\r\n"));
            }
        }
        StringBuffer sb = new StringBuffer();
        sb.append("--> 发起请求：" + request.method() + " " + request.url() + "\n");
        sb.append("--> Header");
        for (int i = 0; i < request.headers().size(); i++) {
            sb.append(request.headers().name(i)).append(" = ").append(request.headers().value(i));
            sb.append("  ,  ");
        }
        sb.append("\n");
        sb.append("--> body: " + requestString);
        LogUtils.i(sb.toString());

        long t1 = System.currentTimeMillis();
        Response response = chain.proceed(request);
        long t2 = System.currentTimeMillis();

        //获取responseBody
        ResponseBody responseBody = response.peekBody(1024 * 1024);

        if (response.isSuccessful()) {

            LogUtils.i("[--> " + request.method() + " " + response.code() + " " + request.url() + " (" + (t2 - t1) + "ms)]\n"
                    + "[--> body: " + requestString + "]\n"
                    + "[--> resp: " + responseBody.string()
            );

        } else {
            LogUtils.e("[--> " + request.method() + " " + response.code() + " " + request.url() + " (" + (t2 - t1) + "ms)]\n"
                    + "[--> body: " + requestString + "]\n"
                    + "[--> resp: " + responseBody.string() + "]"
            );
        }
        return response;

    }
}
