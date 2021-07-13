package com.android.diandezhun.manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.android.baselibrary.event.Event_Token_Failure;
import com.android.baselibrary.tool.SPUtil;
import com.android.diandezhun.bean.UserInfo;
import com.android.diandezhun.constant.SPConstants;
import com.android.diandezhun.ui.activity.LoginActivity;
import com.android.diandezhun.ui.activity.MainActivity;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Administrator on 2018/8/29.
 */

public class UserManager {

    public static void saveUserInfo(Context context, UserInfo info) {
        SPUtil.putObjectValue(context, SPConstants.UserInfo, info);
    }

    public static UserInfo getUserInfo(Context context) {
        UserInfo userInfo = (UserInfo) SPUtil.getObjectValue(context, UserInfo.class, SPConstants.UserInfo);
        if (userInfo == null) {
            userInfo = new UserInfo();
        }
        return userInfo;
    }

    public static void saveToken(Context context, String Token) {
        SPUtil.putValue(context, SPConstants.Token, Token);
    }

    public static String getToken(Context context) {
        return SPUtil.getStringValue(context, SPConstants.Token, "");
    }

    public static boolean isLogin(Context context) {
        return !TextUtils.isEmpty(getToken(context));
    }

    //是否登陆， 没有登录就跳登录
    public static boolean isLoginAndShowLogin(Activity context) {
        if (isLogin(context)) {
            return true;
        } else {
            toLogin(context);
            return false;
        }
    }

    public static void logout(Activity context) {
        saveUserInfo(context, null);
        saveToken(context, "");
        EventBus.getDefault().post(new Event_Token_Failure());
        toLogin(context);
    }

    public static void toMainActivty(Activity context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
        context.finish();
    }

    public static void toLogin(Activity context) {
        if (!LoginActivity.isOpen) {
            LoginActivity.isOpen = true;
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
//            context.finish();
        }
    }

}
