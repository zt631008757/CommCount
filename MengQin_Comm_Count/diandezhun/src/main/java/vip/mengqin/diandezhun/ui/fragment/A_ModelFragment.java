package vip.mengqin.diandezhun.ui.fragment;

import com.android.baselibrary.interface_.OkHttpCallBack;
import com.android.baselibrary.responce.BaseResponce;
import com.android.baselibrary.tool.CommLoading;
import com.android.baselibrary.tool.CommToast;
import com.android.baselibrary.ui.BaseFragment;
import com.android.baselibrary.view.MultiStateView;

import butterknife.BindView;
import vip.mengqin.diandezhun.R;

public class A_ModelFragment extends BaseFragment {

    @BindView(R.id.multiplestatusView)
    MultiStateView multiplestatusView;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_model;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    private void getData()
    {
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

    

}
