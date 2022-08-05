package com.lyl.baselibrary.utils.initialization;

public interface ICheckCallBack {
    void onCheckStart(CheckResultBean checkDeviceBean);

    void onCheckEnd(CheckResultBean checkDeviceBean);
}
