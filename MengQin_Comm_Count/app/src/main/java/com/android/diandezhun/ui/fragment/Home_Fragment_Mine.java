package com.android.diandezhun.ui.fragment;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.baselibrary.interface_.OkHttpCallBack;
import com.android.baselibrary.responce.BaseResponce;
import com.android.baselibrary.tool.CommLoading;
import com.android.baselibrary.tool.CommToast;
import com.android.baselibrary.ui.BaseFragment;
import com.android.diandezhun.R;
import com.android.diandezhun.bean.UserInfo;
import com.android.diandezhun.manager.API_Manager;
import com.android.diandezhun.manager.UserManager;
import com.android.diandezhun.ui.activity.LoginActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class Home_Fragment_Mine extends BaseFragment {

    private static Home_Fragment_Mine fragment = null;

    public static Home_Fragment_Mine newInstance() {
        if (fragment == null) {
            fragment = new Home_Fragment_Mine();
        }
        return fragment;
    }

    @BindView(R.id.tv_login)
    TextView tv_login;
    @BindView(R.id.tv_logout)
    TextView tv_logout;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home_mine;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

//    @OnClick({R.id.ctv_yaoqingma})
//    public void onViewClicked(View view) {
//        Intent intent = null;
//        switch (view.getId()) {
//            case R.id.ctv_setting:   //设置
//                StartActivity(SettingActivity.class);
////                CommLoading.showLoading(this, "加载中");
//                break;
//
//        }
//    }

    @Override
    public void onResume() {
        super.onResume();
        if (mRootView != null) {
//            showUserInfo();
//            if (UserManager.isLogin(mContext)) {
//                getUserInfo();
//            }
            if (UserManager.isLogin(mContext)) {
                tv_login.setVisibility(View.GONE);
                tv_logout.setVisibility(View.VISIBLE);
            } else {
                tv_login.setVisibility(View.VISIBLE);
                tv_logout.setVisibility(View.GONE);
            }
        }
    }

    @OnClick({R.id.tv_login, R.id.tv_logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_login:
                StartActivity(LoginActivity.class);
                break;
            case R.id.tv_logout:
                UserManager.logout((Activity) mContext);
                break;
        }
    }


}
