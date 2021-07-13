package com.android.diandezhun.util;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.android.baselibrary.tool.CommToast;


public class RouterUtil {

    public static void startActivity(Context mContext, String param) {
        try {
            String arr[];
            if (param.contains("?")) {
                int index = param.indexOf("?");
                arr = new String[2];
                arr[0] = param.substring(0, index);
                arr[1] = param.substring(index + 1);
            } else {
                arr = new String[]{param};
            }
            //链接
            String className = arr[0];
            //参数
            Bundle bundle = new Bundle();
            if (arr.length > 1) {
                if (arr[1].contains("url")) {
                    int index = param.indexOf("=");
                    String url = param.substring(index + 1);
                    bundle.putString("url", url);
                } else {
                    String[] arrParam = arr[1].split("&");   // title=11
                    for (int i = 0; i < arrParam.length; i++) {
                        if (!TextUtils.isEmpty(arrParam[i])) {
                            String[] keyvalue = arrParam[i].split("=");
                            String key = keyvalue[0];
                            String value = keyvalue[1];
                            bundle.putString(key, value);
                        }
                    }
                }
            }
            String fullText = mContext.getPackageName() + ".ui.activity." + className;
            Intent intent = new Intent();
            intent.setClassName(mContext.getPackageName(), fullText);
            intent.putExtras(bundle);
            mContext.startActivity(intent);
        } catch (Exception e) {
            CommToast.showToast(mContext, "即将开启，敬请期待");
        }
    }

}
