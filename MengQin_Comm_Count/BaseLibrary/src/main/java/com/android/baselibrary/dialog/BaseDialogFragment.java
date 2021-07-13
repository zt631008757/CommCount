package com.android.baselibrary.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.android.baselibrary.R;
import com.android.baselibrary.interface_.CommCallBack;
import com.android.baselibrary.util.AnimUtil;
import com.gyf.immersionbar.ImmersionBar;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

import butterknife.ButterKnife;

/**
 * DialogFragment 实现沉浸式的基类
 *
 * @author geyifeng
 * @date 2017 /8/26
 */
public abstract class BaseDialogFragment extends DialogFragment {

    public static final int Direction_FadeIn = 0;
    public static final int Direction_FromTop = 1;
    public static final int Direction_FromBottom = 2;

    protected Activity mContext;
    protected View mRootView;
    public CommCallBack callBack;
    public LinearLayout ll_content;
    public View view_bg;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = (Activity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //全屏
        setStyle(DialogFragment.STYLE_NORMAL, R.style.myDialog);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        //点击外部消失
        dialog.setCanceledOnTouchOutside(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(getLayoutId(), container, false);
        //下一步是重点
        Window window = getDialog().getWindow();
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        return mRootView;
    }

    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        if (isImmersionBarEnabled()) {
            ImmersionBar.with(this).statusBarDarkFont(true).navigationBarDarkIcon(true).init();
        }
        initBaseView();
        showWithAnim();
        initView();
        initData();
    }

    /**
     * 获取布局 ID
     */
    protected abstract int getLayoutId();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 初始化控件
     */
    protected abstract void initView();

    public void setCallback(CommCallBack callBack) {
        this.callBack = callBack;
    }

    /**
     * 是否在Fragment使用沉浸式
     *
     * @return the boolean
     */
    protected boolean isImmersionBarEnabled() {
        return true;
    }

    private void initBaseView() {
        ll_content = mRootView.findViewById(R.id.ll_content);
        view_bg = mRootView.findViewById(R.id.view_bg);
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
                    if (setCanceledOnTouchBACK()) {
                        dismissWithAnim();
                    }
                }
            });
        }
        //返回键拦截
        getDialog().setOnKeyListener((anInterface, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                if (setCanceledOnTouchBACK()) {
                    dismissWithAnim();
                }
                return true;
            }
            return false;
        });
    }

    //进入方向
    protected int getShowDirection() {
        return Direction_FadeIn;
    }

    //进入动画
    private void showWithAnim() {
        AnimUtil.fadeIn(view_bg);
        switch (getShowDirection())
        {
            case Direction_FadeIn:
                AnimUtil.fadeIn(ll_content);
                break;
            case Direction_FromTop:
                AnimUtil.enterFromTop(ll_content);
                break;
            case Direction_FromBottom:
                AnimUtil.enterFromBottom(ll_content);
                break;
        }
    }

    //退出动画
    public void dismissWithAnim() {
        if (view_bg.getAnimation() != null) {
            view_bg.clearAnimation();
        }
        AnimUtil.fadeOut(view_bg, new CommCallBack() {
            @Override
            public void onResult(Object obj) {
                dismiss();
            }
        });
        switch (getShowDirection())
        {
            case Direction_FadeIn:
                AnimUtil.fadeOut(ll_content, null);
                break;
            case Direction_FromTop:
                AnimUtil.outToTop(ll_content, null);
                break;
            case Direction_FromBottom:
                AnimUtil.outToBottom(ll_content, null);
                break;
        }
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        dismissWithAnim();
    }

    public abstract void setData(Map<String, Object> params);

    //是否可以按返回键关闭
    protected boolean setCanceledOnTouchBACK() {
        return true;
    }


}