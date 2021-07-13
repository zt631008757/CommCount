package com.android.baselibrary.tool;

import android.app.Activity;
import android.content.Context;

import com.yanzhenjie.permission.AndPermission;

public class AndPermisionUtil {

    public static void requstPermision(Context context, PermisionCallBack callBack, String... permission) {

        AndPermission.with(context)
                .runtime()
                .permission(permission)
                .onGranted(permissions -> {
                    if (callBack != null) {
                        callBack.onGranted();
                    }
                })
                .onDenied(permissions -> {
                    if (callBack != null) {
                        callBack.onDenied();
                    }
                })
                .start();
    }

    public interface PermisionCallBack {
        void onGranted();

        void onDenied();
    }
}
