package com.lyl.baselibrary.net;

import okhttp3.ResponseBody;

/**
 * 作者：梁亚龙
 * 时间：2022/12/9
 * 描述：
 **/
public interface DownloadListener {
    void onStart(ResponseBody responseBody);
}

