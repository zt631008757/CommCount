package vip.mengqin.diandezhun.manager;

import android.content.Context;
import com.android.baselibrary.interface_.OkHttpCallBack;
import com.lzy.okgo.model.HttpMethod;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import vip.mengqin.diandezhun.constant.ApiConstants;

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

    //一键登录
    public static void login_onekey(Context context, String loginToken, OkHttpCallBack<?> callBack) {
        String url = Config.SERVER_HOST + ApiConstants.login_onekey;
        Map<String, Object> bodyParames = new HashMap<>();
        bodyParames.put("loginToken", loginToken);
        OkHttpManager.okHttpRequest_Json(context, HttpMethod.POST, url, null, null, bodyParames, callBack);
    }

    //用户信息
    public static void getUserInfo(Context context, OkHttpCallBack<?> callBack) {
        String url = Config.SERVER_HOST + ApiConstants.userinfo;
        OkHttpManager.okHttpRequest_Json(context, HttpMethod.GET, url, null, null, null, callBack);
    }

    //banner
    public static void getBannerList(Context context, OkHttpCallBack<?> callBack) {
        String url = Config.SERVER_HOST + ApiConstants.banners;
        Map<String, Object> bodyParames = new HashMap<>();
        OkHttpManager.okHttpRequest_Json(context, HttpMethod.GET, url, null, null, bodyParames, callBack);
    }



}
