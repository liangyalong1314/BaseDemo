package com.lyl.baselibrary.utils.initialization;

public interface ICheckTaskCallBack extends ICheckCallBack {
    /**
     * 初始化完成
     */
    void onAllTaskCheckEnd();

    /**
     * 初始化失败
     */
    void onAllTaskCheckError(CheckResultBean checkDeviceBean);
}
