package com.android.baselibrary.responce;

/**
 * Created by Administrator on 2018/6/13.
 */

public class BaseResponce<T> {
    public static int Status_Success = 200;   //正常返回值
    public static int Status_Token_failure = 401;   //登录失效返回值

    public int statusCode;
    public String message;
    public String result;
    private T data;

    public int getStatusCode() {
        return statusCode;
    }
    public String getMessage() {
        return message;
    }
    public T getData() {
        return data;
    }
}
