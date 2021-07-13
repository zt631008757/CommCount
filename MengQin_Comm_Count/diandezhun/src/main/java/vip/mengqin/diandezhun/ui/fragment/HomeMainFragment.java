package vip.mengqin.diandezhun.ui.fragment;

import android.view.View;
import android.widget.LinearLayout;

import androidx.lifecycle.LifecycleOwner;

import com.android.baselibrary.interface_.OkHttpCallBack;
import com.android.baselibrary.responce.BaseResponce;
import com.android.baselibrary.ui.BaseFragment;
import com.gyf.immersionbar.ImmersionBar;
import com.youth.banner.Banner;
import com.youth.banner.indicator.CircleIndicator;

import java.util.List;

import butterknife.BindView;
import vip.mengqin.diandezhun.R;
import vip.mengqin.diandezhun.adapter.MyBannerImageAdapter;
import vip.mengqin.diandezhun.bean.BannerInfo;
import vip.mengqin.diandezhun.manager.API_Manager;

public class HomeMainFragment extends BaseFragment {

    @BindView(R.id.ll_head)
    LinearLayout ll_head;
    @BindView(R.id.banner)
    Banner banner;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home_main;
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
        if (mContext != null) {
            getBannerList();    //banner
        }
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

//    @OnClick({R.id.tv_sendcode})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.tv_sendcode:
//                break;
//        }
//    }

    //获取banner
    private void getBannerList() {
        API_Manager.getBannerList(mContext, new OkHttpCallBack<BaseResponce<List<BannerInfo>>>() {
            @Override
            public void onSuccess(BaseResponce<List<BannerInfo>> baseResponce) {
                banner.setAdapter(new MyBannerImageAdapter(mContext, baseResponce.getData(), null))
                        .addBannerLifecycleObserver((LifecycleOwner) mContext)//添加生命周期观察者
                        .setIndicator(new CircleIndicator(mContext));
                if (banner.getAdapter().getItemCount() == 0) {
                    banner.setVisibility(View.GONE);
                } else {
                    banner.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(BaseResponce<List<BannerInfo>> baseResponce) {
            }
        });
    }

}
