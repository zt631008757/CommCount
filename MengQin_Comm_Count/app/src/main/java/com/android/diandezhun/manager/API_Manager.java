package com.android.diandezhun.manager;

import android.content.Context;

import com.android.baselibrary.interface_.OkHttpCallBack;
import com.android.baselibrary.util.Util;
import com.android.diandezhun.constant.ApiConstants;
import com.lzy.okgo.model.HttpMethod;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class API_Manager {

    //获取服务器时间
    public static void timestamp(Context context, OkHttpCallBack<?> callBack) {
        String url = Config.SERVER_HOST + ApiConstants.timestamp;
        Map<String, Object> bodyParames = new HashMap<>();
        OkHttpManager.okHttpRequest_Json(context, HttpMethod.GET, url, null, null, bodyParames, callBack);
    }

    //登录
    public static void login(Context context, String phone, String code, OkHttpCallBack<?> callBack) {
        String url = Config.SERVER_HOST + ApiConstants.login;
        Map<String, Object> bodyParames = new HashMap<>();
        bodyParames.put("code", code);
        bodyParames.put("mobile", phone);
        OkHttpManager.okHttpRequest_Json(context, HttpMethod.POST, url, null, null, bodyParames, callBack);
    }

    //发验证码
    public static void sendcode(Context context, String phone, OkHttpCallBack<?> callBack) {
        String url = Config.SERVER_HOST + ApiConstants.sendcode;
        Map<String, Object> bodyParames = new HashMap<>();
        bodyParames.put("mobile", phone);
        OkHttpManager.okHttpRequest_Json(context, HttpMethod.POST, url, null, null, bodyParames, callBack);
    }

    //图像识别
    public static void imgCompute(Context context, String type, File file, OkHttpCallBack<?> callBack) {
        String url = Config.SERVER_HOST_Compute;
        Map<String, Object> bodyParames = new HashMap<>();
        bodyParames.put("type", type);
        OkHttpManager.okHttpFileUpload(context, url, null, bodyParames, file, callBack, false);
    }

    //模板列表
    public static void template(Context context, OkHttpCallBack<?> callBack) {
        String url = Config.SERVER_HOST_Compute + ApiConstants.template;
        OkHttpManager.okHttpRequest_Json(context, HttpMethod.GET, url, null, null, null, callBack);
    }


}
