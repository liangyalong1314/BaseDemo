package com.lyl.baselibrary.base;

import com.google.gson.annotations.SerializedName;

/**
 * @Author lyl
 * @Date 2022/05/24
 */
public class BaseResponse <T>{
    @SerializedName("status")
    private int code;

    @SerializedName("msg")
    private String msg;

    @SerializedName("data")
    private T data;

    public boolean isSuccess() {
        return code == 200;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
