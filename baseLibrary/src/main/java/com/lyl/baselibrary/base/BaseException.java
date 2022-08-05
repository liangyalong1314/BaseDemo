package com.lyl.baselibrary.base;

import java.io.IOException;

/**
 * @Author lyl
 * @Date 2022/05/24
 */
public class BaseException  extends IOException {

    /**
     * 解析数据失败
     */
    public static final int PARSE_ERROR = 1001;
    public static final String PARSE_ERROR_MSG = "解析数据失败";

    /**
     * 网络问题
     */
    public static final int BAD_NETWORK = 1002;
    public static final String BAD_NETWORK_MSG = "网络问题";
    /**
     * 连接错误
     */
    public static final int CONNECT_ERROR = 1003;
    public static final String CONNECT_ERROR_MSG = "连接错误";
    /**
     * 连接超时
     */
    public static final int CONNECT_TIMEOUT = 1004;
    public static final String CONNECT_TIMEOUT_MSG = "连接超时";
    /**
     * 未知错误
     */
    public static final int OTHER = 1005;
    public static final String OTHER_MSG = "未知错误";
    /**
     * token 失效
     */
    public static final int BAD_TOKEN = 401;
    public static final String BAD_TOKEN_MSG = "登录过期";


    private String errorMsg;
    private int errorCode;


    public String getMsg() {
        return errorMsg;
    }

    public int getCode() {
        return errorCode;
    }

    public BaseException(String message, Throwable cause, int errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
        this.errorMsg = message;
    }

    public BaseException(String message, int errorCode) {
        this.errorCode = errorCode;
        this.errorMsg = message;
    }
}
