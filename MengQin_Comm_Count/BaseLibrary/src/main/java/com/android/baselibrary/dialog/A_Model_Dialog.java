package com.android.baselibrary.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;

import com.android.baselibrary.R;
import com.android.baselibrary.interface_.CommCallBack;
import com.android.baselibrary.util.AnimUtil;
import com.android.baselibrary.util.StatusBarUtil_Dialog;


/**
 * Created by Administrator on 2018/8/29.
 */

public class A_Model_Dialog extends BaseDialog {
    public A_Model_Dialog(@NonNull Context context) {
        super(context);
    }

    CommCallBack callBack;

    public void setCallBack(CommCallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_commloading);
        initView();
        StatusBarUtil_Dialog.setImmersiveStatusBar(this, false);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_comm;
    }

    @Override
    protected void initView() {
        AnimUtil.fadeIn(view_bg);
        AnimUtil.fadeIn(ll_content);

    }

}
