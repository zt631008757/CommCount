package vip.mengqin.diandezhun.ui.activity;

import com.android.baselibrary.view.Comm_HeadView;
import com.android.baselibrary.view.MultiStateView;

import butterknife.BindView;
import vip.mengqin.diandezhun.R;

/**
 * 关于我们
 */
public class AboutUsActivity extends BaseActivity {

    @BindView(R.id.multiplestatusView)
    MultiStateView multiplestatusView;
    @BindView(R.id.comm_title)
    Comm_HeadView comm_title;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_model;
    }

    @Override
    protected void initView() {
        comm_title.setTitle("关于我们");
    }

    @Override
    protected void initData() {

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
//                CommToast.showToast(mContext, baseResponce.message);
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

}
