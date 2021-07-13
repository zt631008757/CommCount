package com.android.baselibrary.tool;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

/**
 * Created by Administrator on 2018/8/14.
 */

public class SPUtil {
    public final static String SHAREPREFERENCE_NAME = "gameplay";

    /**
     * 设置sp通用方法
     *
     * @param context
     * @param key
     * @param value
     */
    public static void putValue(Context context, String key, Object value) {
        SharedPreferences sp = context.getSharedPreferences(SHAREPREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        if (value instanceof String) {
            edit.putString(key, (String) value);
        } else if (value instanceof Integer) {
            edit.putInt(key, (int) value);
        } else if (value instanceof Boolean) {
            edit.putBoolean(key, (boolean) value);
        }
        edit.commit();
    }

    /**
     * 获取sp通用方法
     *
     * @param context
     * @param key
     * @param defaultValue
     */
    public static String getStringValue(Context context, String key, String defaultValue) {
        SharedPreferences sp = context.getSharedPreferences(SHAREPREFERENCE_NAME, Context.MODE_PRIVATE);
        return sp.getString(key, defaultValue);
    }

    public static int getIntValue(Context context, String key, int defaultValue) {
        SharedPreferences sp = context.getSharedPreferences(SHAREPREFERENCE_NAME, Context.MODE_PRIVATE);
        return sp.getInt(key, defaultValue);
    }

    public static boolean getBoolValue(Context context, String key, boolean defaultValue) {
        try {
            SharedPreferences sp = context.getSharedPreferences(SHAREPREFERENCE_NAME, Context.MODE_PRIVATE);
            return sp.getBoolean(key, defaultValue);
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    /**
     * 设置sp通用方法, 存json格式的object对象
     */
    public static void putObjectValue(Context context, String key, Object value) {
        SharedPreferences sp = context.getSharedPreferences(SHAREPREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        String json = new Gson().toJson(value);
        edit.putString(key, json);
        edit.commit();
    }

    public static Object getObjectValue(Context context, Class<?> getClass, String key) {
        Object obj = null;
        try {
            SharedPreferences sp = context.getSharedPreferences(SHAREPREFERENCE_NAME, Context.MODE_PRIVATE);
            String json = sp.getString(key, "");
            obj = new Gson().fromJson(json, getClass);
            return obj;
        } catch (Exception ex) {
            return obj;
        }
    }

}
