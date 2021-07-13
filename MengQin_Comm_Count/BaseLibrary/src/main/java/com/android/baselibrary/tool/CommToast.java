package com.android.baselibrary.tool;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/8/21.
 */
public class CommToast {

    public static void showToast(Context context, String tvString, int ... time){
        Toast.makeText(context,tvString, Toast.LENGTH_SHORT).show();
    }
}
