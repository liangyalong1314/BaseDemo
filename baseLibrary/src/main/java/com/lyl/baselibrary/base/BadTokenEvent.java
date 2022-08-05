package com.lyl.baselibrary.base;

/**
 * @Author lyl
 * @Date 2022/05/24
 */
public class BadTokenEvent {

    private String msg;

    public BadTokenEvent(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
