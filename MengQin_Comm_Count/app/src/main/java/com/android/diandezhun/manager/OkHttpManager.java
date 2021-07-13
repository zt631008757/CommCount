package com.android.diandezhun.manager;

import android.content.Context;

import com.android.baselibrary.interface_.OkHttpCallBack;
import com.android.baselibrary.manager.EasyHttpUtil;
import com.android.baselibrary.manager.OkGoUtil;
import com.lzy.okgo.model.HttpMethod;

import java.io.File;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/8/13.
 */

public class OkHttpManager {

    /**
     * Json格式通用接口请求，适用大部分接口
     *
     * @param context
     * @param methed      请求方式
     * @param url         请求地址
     * @param urlParam    url参数
     * @param headers     head参数
     * @param requestBody body参数
     * @param callBack
     */
    public static void okHttpRequest_Json(Context context, HttpMethod methed, String url, Map<String, Object> urlParam, Map<String, Object> headers, Map<String, Object> requestBody, OkHttpCallBack<?> callBack) {
        //添加headers参数
        if (headers == null) {
            headers = new HashMap<>();
        }
        //token信息
        if (UserManager.isLogin(context) && !headers.containsKey("Authorization")) {
            headers.put("Authorization", "Bearer " + UserManager.getToken(context));
        }
        headers.putAll(SignManager.getHead());
        OkGoUtil.okHttpRequest(context, methed, url, urlParam, headers, requestBody, callBack, true);
    }


    //文件上传
    public static void okHttpFileUpload(Context context, String url, Map<String, Object> headers, Map<String, Object> requestBody, File file, OkHttpCallBack<?> callBack, boolean isJson) {
        //headers参数
        if (headers == null) {
            headers = new HashMap<>();
        }
        if (UserManager.isLogin(context) && !headers.containsKey("Authorization")) {
            headers.put("Authorization", "Bearer " + UserManager.getToken(context));
        }
        headers.putAll(SignManager.getSimpleHead());
        OkGoUtil.uploadByOkhttp(context, url, headers, requestBody, file, callBack, isJson);
    }


}
