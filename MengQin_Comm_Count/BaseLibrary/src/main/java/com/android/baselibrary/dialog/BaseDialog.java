package com.android.baselibrary.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.android.baselibrary.R;
import com.android.baselibrary.interface_.CommCallBack;
import com.android.baselibrary.util.AnimUtil;
import com.android.baselibrary.util.StatusBarUtil_Dialog;
import com.gyf.immersionbar.ImmersionBar;

public abstract class BaseDialog extends Dialog {

    public BaseDialog(@NonNull Context context) {
        super(context, R.style.myDialog);
        this.mContext = context;
    }

    public Context mContext;

    public LinearLayout ll_content;
    public View view_bg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
//        ImmersionBar.with(getOwnerActivity(),this).statusBarDarkFont(false).navigationBarDarkIcon(false).init();
        StatusBarUtil_Dialog.setImmersiveStatusBar(this, true);
        initBaseView();
        initView();
    }

    private void initBaseView() {
        ll_content = findViewById(R.id.ll_content);
        view_bg = findViewById(R.id.ll_content);
        if (ll_content != null) {
            ll_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
        if (view_bg != null) {
            view_bg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismissWithAnim();
                }
            });
        }
    }

    /**
     * 获取布局 ID
     */
    protected abstract int getLayoutId();

    /**
     * 初始化控件
     */
    protected abstract void initView();


    public void dismissWithAnim() {
        if (view_bg.getAnimation() != null) return;
        AnimUtil.fadeOut(ll_content, null);
        AnimUtil.fadeOut(view_bg, new CommCallBack() {
            @Override
            public void onResult(Object obj) {
                dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        dismissWithAnim();
    }

}
