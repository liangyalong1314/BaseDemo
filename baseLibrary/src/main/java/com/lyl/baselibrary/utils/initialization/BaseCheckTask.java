package com.lyl.baselibrary.utils.initialization;


import android.content.Context;

public abstract class BaseCheckTask {
    public CheckResultBean checkDeviceBean;
    private ICheckCallBack iCheckCallBack = null;
    public Context mContext;

    public BaseCheckTask(Context mContext) {
        this.mContext = mContext;
        checkDeviceBean = new CheckResultBean();
    }


    public void checkEndCallBack() {
        if (iCheckCallBack != null) {
            iCheckCallBack.onCheckEnd(checkDeviceBean);
        }
    }

    public void checkStartCallBack() {
        if (iCheckCallBack != null) {
            iCheckCallBack.onCheckStart(checkDeviceBean);
        }
    }

    public void setICheckCallBack(ICheckCallBack iCheckCallBack) {
        this.iCheckCallBack = iCheckCallBack;
    }

    /**
     * 检测结束
     */
    public void checkEnd() {
        checkEndCallBack();
    }

    /**
     * 开始检测
     */
    public abstract void checkStart();
    public void clear(){

    }
}
