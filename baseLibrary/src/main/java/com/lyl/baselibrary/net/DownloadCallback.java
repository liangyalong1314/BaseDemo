package com.lyl.baselibrary.net;

import java.io.File;

import io.reactivex.rxjava3.disposables.Disposable;

/**
 * 作者：梁亚龙
 * 时间：2022/12/9
 * 描述：
 **/
public interface DownloadCallback {
    void onStart(Disposable d);

    void onProgress(long totalByte, long currentByte, int progress);

    void onFinish(File file);

    void onError(String msg);
}

