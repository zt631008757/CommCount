package com.android.baselibrary.tool;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.android.baselibrary.dialog.LoadingDialogFragment;
import com.android.baselibrary.dialog.BaseDialogFragment;

import java.util.HashMap;
import java.util.Map;

public class CommLoading {

    static BaseDialogFragment mDialog;

    public static void showLoading(AppCompatActivity mContext, String... text) {
        try {
            Map<String, Object> param = new HashMap<>();
            if (text.length > 0)
                param.put("text", text[0]);
            mDialog = LoadingDialogFragment.showDialog(mContext.getSupportFragmentManager(), param);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showLoading(Fragment mContext, String... text) {
        try {
            Map<String, Object> param = new HashMap<>();
            if (text.length > 0)
                param.put("text", text[0]);
            mDialog = LoadingDialogFragment.showDialog(mContext.getChildFragmentManager(), param);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showLoading(Object mContext, String... text) {
        try {
            Map<String, Object> param = new HashMap<>();
            if (text.length > 0)
                param.put("text", text[0]);
            if (mContext instanceof AppCompatActivity) {
                mDialog = LoadingDialogFragment.showDialog(((AppCompatActivity) mContext).getSupportFragmentManager(), param);
            } else if (mContext instanceof Fragment) {
                mDialog = LoadingDialogFragment.showDialog(((Fragment) mContext).getChildFragmentManager(), param);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //隐藏弹窗
    public static void dismissLoading() {
        try {
            if (mDialog != null) {
                mDialog.dismissWithAnim();
                mDialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //隐藏弹窗
    public static void dismissWithoutAnim() {
        try {
            if (mDialog != null) {
                mDialog.dismiss();
                mDialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
