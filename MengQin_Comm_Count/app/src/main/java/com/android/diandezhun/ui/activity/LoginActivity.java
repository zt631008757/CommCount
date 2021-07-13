package com.android.diandezhun.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.baselibrary.interface_.CommCallBack;
import com.android.baselibrary.interface_.OkHttpCallBack;
import com.android.baselibrary.responce.BaseResponce;
import com.android.baselibrary.tool.CommLoading;
import com.android.baselibrary.tool.CommToast;
import com.android.baselibrary.tool.SPUtil;
import com.android.baselibrary.view.Comm_EditView;
import com.android.baselibrary.view.Comm_SubmitBtnView;
import com.android.diandezhun.R;
import com.android.diandezhun.constant.SPConstants;
import com.android.diandezhun.constant.URLConstants;
import com.android.diandezhun.manager.API_Manager;
import com.android.diandezhun.manager.UserManager;
import com.android.diandezhun.responce.TokenInfo;
import com.gyf.immersionbar.ImmersionBar;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/6/20.
 */

public class LoginActivity extends BaseActivity {

    public static boolean isOpen = false;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isOpen = false;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @BindView(R.id.et_account)
    Comm_EditView et_account;
    @BindView(R.id.et_code)
    Comm_EditView et_code;
    @BindView(R.id.tv_sendcode)
    TextView tv_sendcode;
    @BindView(R.id.csb_login)
    Comm_SubmitBtnView csb_login;
    @BindView(R.id.ll_title)
    LinearLayout ll_title;

    @Override
    protected boolean isImmersion() {
        return false;
    }

    @Override
    protected void initView() {
        //首页沉浸式
        ImmersionBar.with(this).titleBar(ll_title);
        ImmersionBar.with(this).statusBarDarkFont(true).navigationBarDarkIcon(true).init();


        checkInput();
        String account = SPUtil.getStringValue(mContext, SPConstants.Phone, "");
        if (!TextUtils.isEmpty(account)) {
            et_account.setText(account);
        }

        et_account.setInputCallBack(inputCallback);
        et_code.setInputCallBack(inputCallback);
    }

    @Override
    protected void initData() {

    }

    CommCallBack inputCallback = new CommCallBack() {
        @Override
        public void onResult(Object obj) {
            checkInput();
        }
    };

    private void checkInput() {
        csb_login.setEnabled(false);
        if (TextUtils.isEmpty(et_account.getText())) {
            return;
        }
        if (TextUtils.isEmpty(et_code.getText())) {
            return;
        }
        if (et_account.getText().length() != 11 || et_code.getText().length() != 4) {
            return;
        }
        csb_login.setEnabled(true);
    }

    //发验证码
    private void sendcode() {
        String account = et_account.getText();
        if (TextUtils.isEmpty(account)) {
            CommToast.showToast(mContext, "请输入手机号");
            return;
        }
        SPUtil.putValue(mContext, SPConstants.Phone, account);
        CommLoading.showLoading((AppCompatActivity) mContext);
        API_Manager.sendcode(mContext, account, new OkHttpCallBack<BaseResponce<String>>() {
            @Override
            public void onSuccess(BaseResponce<String> baseResponce) {
                CommLoading.dismissLoading();
                CommToast.showToast(mContext, "发送成功");
                handler.removeCallbacks(runnable);
                handler.post(runnable);
                et_code.requestFocus();
            }

            @Override
            public void onFailure(BaseResponce<String> baseResponce) {
                CommLoading.dismissLoading();
                CommToast.showToast(mContext, baseResponce.message);
            }
        });
    }

    int num = 60;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            num--;
            if (num == 0) {
                tv_sendcode.setEnabled(true);
//                tv_sendcode.setTextColor(Color.parseColor("#f2493a"));
                tv_sendcode.setText("获取验证码");
                num = 60;
            } else {
                tv_sendcode.setText(num + "秒后重新获取");
                tv_sendcode.setEnabled(false);
//                tv_sendcode.setTextColor(Color.parseColor("#999999"));
                handler.postDelayed(runnable, 1000);
            }
        }
    };

    private void login() {
        final String account = et_account.getText().toString();
        final String code = et_code.getText().toString();

        if (TextUtils.isEmpty(account)) {
            CommToast.showToast(mContext, "请输入手机号");
            et_account.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(code)) {
            CommToast.showToast(mContext, "请输入验证码");
            et_code.requestFocus();
            return;
        }

        CommLoading.showLoading((AppCompatActivity) mContext);
        API_Manager.login(mContext, account, code, new OkHttpCallBack<BaseResponce<TokenInfo>>() {
            @Override
            public void onSuccess(BaseResponce<TokenInfo> baseResponce) {
                CommLoading.dismissLoading();
                TokenInfo tokenInfo = baseResponce.getData();
                UserManager.saveToken(mContext, tokenInfo.token);
                UserManager.toMainActivty((Activity) mContext); //跳首页
            }

            @Override
            public void onFailure(BaseResponce<TokenInfo> baseResponce) {
                CommLoading.dismissLoading();
                CommToast.showToast(mContext, baseResponce.message);
            }
        });
    }

    @OnClick({R.id.tv_sendcode, R.id.csb_login, R.id.tv_gongyue, R.id.tv_xieyi,R.id.iv_back})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.tv_sendcode:
                sendcode();
                break;
            case R.id.csb_login:
                login();
                break;
            case R.id.tv_xieyi:
                intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra("url", URLConstants.URL_xieyi1);
                startActivity(intent);
                break;
            case R.id.tv_gongyue:
                intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra("url", URLConstants.URL_xieyi2);
                startActivity(intent);
                break;
            case R.id.iv_back:
                finish();
                break;

        }
    }



}
