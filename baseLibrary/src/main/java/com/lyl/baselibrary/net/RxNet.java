package com.lyl.baselibrary.net;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.LogUtils;
import com.lyl.baselibrary.utils.CommonUtils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import okhttp3.ResponseBody;

/**
 * 作者：梁亚龙
 * 时间：2022/12/9
 * 描述：
 **/
public class RxNet {

    public static boolean enableLog = true;

    public static void download( String url,  String filePath,  DownloadCallback callback) {
        Log.i("TAG", filePath+": "+filePath);
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(filePath)) {
            if (null != callback) {
                callback.onError("url or path empty");
            }
            return;
        }
        File oldFile = new File(filePath);
        if (oldFile.exists()) {
            if (null != callback) {
                callback.onFinish(oldFile);
            }
            return;
        }
        DownloadListener listener = new DownloadListener() {
            @Override
            public void onStart(ResponseBody responseBody) {
                Log.i("TAG11", filePath+": "+filePath);
                saveFile(responseBody, url, filePath, callback);
            }
        };

       new  RetrofitFactory().downloadFile(url, CommonUtils.getTempFile(url, filePath).length(), listener, new Observer<ResponseBody>() {
            @Override
            public void onSubscribe(Disposable d) {
                if (null != callback) {
                    callback.onStart(d);
                }
            }

            @Override
            public void onNext( ResponseBody responseBody) {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                LogUtils.i("onError " + e.getMessage());
                if (null != callback) {
                    callback.onError(e.getMessage());
                }
            }

            @Override
            public void onComplete() {
                LogUtils.i("download onComplete ");
            }
        });

    }
    /**
     * 取消网络请求
     */
    public static void cancel(Disposable d) {
        if (null != d && !d.isDisposed()) {
            d.dispose();
        }
    }
    private static void saveFile( ResponseBody responseBody, String url,  String filePath,  DownloadCallback callback) {
        boolean downloadSuccss = true;
         File tempFile = CommonUtils.getTempFile(url, filePath);
        Log.i("TAG", filePath+": "+tempFile.getAbsolutePath());
        try {
            writeFileToDisk(responseBody, tempFile.getAbsolutePath(), callback);
        } catch (Exception e) {
            e.printStackTrace();
            downloadSuccss = false;
        }

        if (downloadSuccss) {
             boolean renameSuccess = tempFile.renameTo(new File(filePath));
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    if (null != callback && renameSuccess) {
                        callback.onFinish(new File(filePath));
                    }
                }
            });
        }
    }

    @SuppressLint("DefaultLocale")
    private static void writeFileToDisk(ResponseBody responseBody, String filePath,  DownloadCallback callback) throws IOException {
        long totalByte = responseBody.contentLength();
        long downloadByte = 0;
        File file = new File(filePath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        byte[] buffer = new byte[1024 * 4];
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rwd");
        long tempFileLen = file.length();
        randomAccessFile.seek(tempFileLen);
        while (true) {
            int len = responseBody.byteStream().read(buffer);
            if (len == -1) {
                break;
            }
            randomAccessFile.write(buffer, 0, len);
            downloadByte += len;
            callbackProgress(tempFileLen + totalByte, tempFileLen + downloadByte, callback);
        }
        randomAccessFile.close();
    }

    private static void callbackProgress( long totalByte,  long downloadByte,  DownloadCallback callback) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @SuppressLint("DefaultLocale")
            @Override
            public void run() {
                if (null != callback) {
                    callback.onProgress(totalByte, downloadByte, (int) ((downloadByte * 100) / totalByte));
                }
            }
        });
    }

}
