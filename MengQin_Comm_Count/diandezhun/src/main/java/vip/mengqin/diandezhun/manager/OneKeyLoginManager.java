package vip.mengqin.diandezhun.manager;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.baselibrary.interface_.OkHttpCallBack;
import com.android.baselibrary.responce.BaseResponce;
import com.android.baselibrary.view.Comm_HeadView;

import cn.jiguang.verifysdk.api.AuthPageEventListener;
import cn.jiguang.verifysdk.api.JVerificationInterface;
import cn.jiguang.verifysdk.api.JVerifyUIClickCallback;
import cn.jiguang.verifysdk.api.JVerifyUIConfig;
import cn.jiguang.verifysdk.api.LoginSettings;
import cn.jiguang.verifysdk.api.VerifyListener;
import vip.mengqin.diandezhun.R;
import vip.mengqin.diandezhun.responce.TokenInfo;

public class OneKeyLoginManager {

    public static void showLogin(Context context) {
        setStyle(context);
        autoLogin(context);
    }

    private static void autoLogin(Context context) {
        LoginSettings settings = new LoginSettings();
        settings.setAutoFinish(false);//设置登录完成后是否自动关闭授权页
        settings.setTimeout(15 * 1000);//设置超时时间，单位毫秒。 合法范围（0，30000],范围以外默认设置为10000
        //设置授权页事件监听
        settings.setAuthPageEventListener(new AuthPageEventListener() {
            @Override
            public void onEvent(int cmd, String msg) {
                //cmd 2：联通协议，  4：用户协议   5：隐私政策


                Log.i("test", "cmd:" + cmd + " msg:" + msg);
            }
        });

        JVerificationInterface.loginAuth(context, settings, new VerifyListener() {
            @Override
            public void onResult(int code, String content, String operator) {
                if (code == 6000) {
                    Log.i("test", "autoLogin:code=" + code + ", token=" + content + " ,operator=" + operator);
                    API_Manager.login_onekey(context, content, new OkHttpCallBack<BaseResponce<TokenInfo>>() {
                        @Override
                        public void onSuccess(BaseResponce<TokenInfo> baseResponce) {
                            TokenInfo tokenInfo = baseResponce.getData();
                            UserManager.saveToken(context, tokenInfo.token);
                            UserManager.toMainActivty(context); //跳首页
                        }

                        @Override
                        public void onFailure(BaseResponce<TokenInfo> baseResponce) {

                        }
                    });
                } else if (code == 2003 || code == 2016)  //2003:network not reachable   ||  2016: network type not supported    网络不支持，跳验证码登录
                {
                    Log.i("test", "网络不支持，跳验证码登录");
                    UserManager.toLogin(context);
                } else {
                    UserManager.toLogin(context);
                    Log.i("test", "autoLogin:code=" + code + ", message=" + content);
                }
            }
        });
    }

//    private static void getUserinfo(Context context) {
//        APIRepository.getUserInfo(new AbstractMQCallback(context, true) {
//            @Override
//            public <T> void success(T data) {
//                UserInfo userInfo = (UserInfo) data;
//                if (GlobalConfig.getUserInfo() != userInfo) {
//                    GlobalConfig.setUserInfo(userInfo);
//                }
//                PushHelper.INSTANCE.uploadTokenToBackend();
//                closeAuth();
//            }
//        });
//    }

    private static void setStyle(Context context) {
        View rl_view = View.inflate(context, R.layout.view_auto_login, null);

        //返回按钮
        Comm_HeadView comm_title = rl_view.findViewById(R.id.comm_title);

//        view_zeus_toolbar.findViewById(R.id.toolbar_action_back).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                closeAuth();
//            }
//        });

        //验证码登录
        TextView tv_phone = rl_view.findViewById(R.id.tv_phone);
        tv_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserManager.toLogin(context);
            }
        });

        JVerifyUIConfig uiConfig = new JVerifyUIConfig.Builder()
                //标题栏样式
                .setNavHidden(true)         //隐藏标题栏
                .setNavColor(context.getResources().getColor(R.color.colorPrimary))
                .setStatusBarColorWithNav(true)

                //logo样式
                .setLogoHidden(true)        //隐藏logo
                .setLogoOffsetY(346)

                //手机号样式
                .setNumberTextBold(true)    //设置手机号码加粗
                .setNumberSize(28)          //设置手机号码大小
                .setNumberColor(0xff333333) //设置手机号码颜色
                .setNumFieldOffsetY(246)

                //一键登录按钮样式
                .setLogBtnText("本机号码一键登录")
                .setLogBtnTextColor(0xffffffff)
                .setLogBtnHeight(50)
                .setLogBtnTextSize(16)
                .setLogBtnOffsetY(300)
                .setLogBtnImgPath("bg_circle_maincolor")

                //协议样式
                .setAppPrivacyOne("《用户协议》", "https://www.mengqin.vip/agreement.html")
                .setAppPrivacyTwo("《隐私政策》", "https://www.mengqin.vip/privacy.html")
                .setAppPrivacyColor(0xffAAAAAA, 0xff0084FF)   //设置颜色
                .setPrivacyState(true)
                .setPrivacyCheckboxHidden(true)
                .setPrivacyTextSize(13)
                .setPrivacyOffsetX(20)
                .setPrivacyOffsetY(20)
                .setPrivacyWithBookTitleMark(true)   //运营商协议加《》
                .setPrivacyText("登录即代表同意", "和", "、", "并使用本机号码登录")

                //协议H5页样式
                .setPrivacyNavTitleTextSize(20)   //标题大小
                .setPrivacyNavTitleTextBold(true)
                .setPrivacyStatusBarColorWithNav(true)
                .setPrivacyNavColor(context.getResources().getColor(R.color.colorPrimary))   //标题栏颜色


                //运营商标识
                .setSloganHidden(true)   //隐藏标识
//                .setLoadingView(loadingView,null)

                .addCustomView(rl_view, false, new JVerifyUIClickCallback() {
                    @Override
                    public void onClicked(Context context, View view) {
//                        Toast.makeText(context, "点击按钮：" + view.getId(), Toast.LENGTH_SHORT).show();
                    }
                })
                .build();
        JVerificationInterface.setCustomUIWithConfig(uiConfig);
    }

    public static void closeAuth() {
        JVerificationInterface.dismissLoginAuthActivity(true, null);
    }

}
