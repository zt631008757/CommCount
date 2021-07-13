package vip.mengqin.diandezhun.ui.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.baselibrary.interface_.OkHttpCallBack;
import com.android.baselibrary.responce.BaseResponce;
import com.android.baselibrary.ui.BaseFragment;
import com.android.baselibrary.util.GlideUtil;
import com.android.baselibrary.view.MultiStateView;
import com.gyf.immersionbar.ImmersionBar;

import butterknife.BindView;
import butterknife.OnClick;
import vip.mengqin.diandezhun.R;
import vip.mengqin.diandezhun.bean.UserInfo;
import vip.mengqin.diandezhun.manager.API_Manager;
import vip.mengqin.diandezhun.manager.OneKeyLoginManager;
import vip.mengqin.diandezhun.manager.UserManager;
import vip.mengqin.diandezhun.ui.activity.AboutUsActivity;
import vip.mengqin.diandezhun.ui.activity.SettingActivity;

public class HomeMineFragment extends BaseFragment {

    @BindView(R.id.ll_head)
    LinearLayout ll_head;
    @BindView(R.id.iv_head)
    ImageView iv_head;
    @BindView(R.id.tv_name)
    TextView tv_name;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home_mine;
    }

    @Override
    protected void initView() {
        ImmersionBar.with(this).titleBar(ll_head).statusBarDarkFont(false).navigationBarDarkIcon(false).init();


    }

    @Override
    protected void initData() {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mRootView != null) {
            showUserInfo();
            if (UserManager.isLogin(mContext)) {
                getUserInfo();
            }
        }
    }

    //刷新用户信息
    private void showUserInfo() {
        if (UserManager.isLogin(mContext)) {
            UserInfo userInfo = UserManager.getUserInfo(mContext);
            if (userInfo != null) {
                tv_name.setText(userInfo.nickname);
                GlideUtil.displayImage(mContext, userInfo.avatar, iv_head, R.drawable.ico_default_img_fang);
            }
        } else {
            //未登录

        }
    }

    //获取用户信息
    private void getUserInfo() {
        API_Manager.getUserInfo(mContext, new OkHttpCallBack<BaseResponce<UserInfo>>() {
            @Override
            public void onSuccess(BaseResponce<UserInfo> baseResponce) {
                UserManager.saveUserInfo(mContext, baseResponce.getData());
                showUserInfo();
            }

            @Override
            public void onFailure(BaseResponce<UserInfo> baseResponce) {
            }
        });
    }

    private void getData() {
//        API_Manager.sendcode(mContext, "", new OkHttpCallBack<BaseResponce<String>>() {
//            @Override
//            public void onSuccess(BaseResponce<String> baseResponce) {
//                CommToast.showToast(mContext, "发送成功");
//            }
//
//            @Override
//            public void onFailure(BaseResponce<String> baseResponce) {
//                CommLoading.dismissLoading();
//                CommToast.showToast(mContext, baseResponce.msg);
//            }
//        });
    }

    @OnClick({R.id.ctv_guanyuwomen, R.id.ctv_setting,R.id.iv_head})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ctv_guanyuwomen:  //关于我们
                StartActivity(AboutUsActivity.class);
                break;
            case R.id.ctv_setting:  //设置
                StartActivity(SettingActivity.class);
                break;
            case R.id.iv_head:
                OneKeyLoginManager.showLogin(mContext);
                break;

        }
    }


}
