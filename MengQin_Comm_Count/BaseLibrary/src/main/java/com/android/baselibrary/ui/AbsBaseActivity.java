package com.android.baselibrary.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.umeng.analytics.MobclickAgent;
import org.greenrobot.eventbus.EventBus;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/6/12.
 */

public abstract class AbsBaseActivity extends AppCompatActivity  {

    public Context mContext;
    public String title = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        mContext = this;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);   //禁用横屏
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure();
        }
        ButterKnife.bind(this);
        if (isRegistEventbus()) {
            EventBus.getDefault().register(this);
        }
        initView();
        initData();
    }

    /**
     * 获取布局 ID
     */
    protected abstract int getLayoutId();

    /**
     * 初始化控件
     */
    protected abstract void initView();

    /**
     * 获取数据
     */
    protected abstract void initData();

    /**
     * 是否需要注册eventbus
     */
    protected boolean isRegistEventbus() {
        return false;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isRegistEventbus()) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart(TextUtils.isEmpty(title) ? getClass().getSimpleName() : title);
    }


    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd(TextUtils.isEmpty(title) ? getClass().getSimpleName() : title);
    }

    public void StartActivity(Class<?> cls) {
        Intent intent = new Intent(mContext, cls);
        startActivity(intent);
    }
}
