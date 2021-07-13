package com.android.diandezhun.ui.activity;

import com.android.baselibrary.interface_.OkHttpCallBack;
import com.android.baselibrary.responce.BaseResponce;
import com.android.baselibrary.tool.CommLoading;
import com.android.baselibrary.tool.CommToast;
import com.android.baselibrary.view.Comm_HeadView;
import com.android.baselibrary.view.MultiStateView;
import com.android.diandezhun.R;
import com.android.diandezhun.manager.API_Manager;

import butterknife.BindView;

public class A_ModelActivity extends BaseActivity{

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
        comm_title.setTitle("");
    }

    @Override
    protected void initData() {

    }

    private void getData()
    {
        API_Manager.sendcode(mContext, "", new OkHttpCallBack<BaseResponce<String>>() {
            @Override
            public void onSuccess(BaseResponce<String> baseResponce) {
                CommToast.showToast(mContext, "发送成功");
            }

            @Override
            public void onFailure(BaseResponce<String> baseResponce) {
                CommLoading.dismissLoading();
                CommToast.showToast(mContext, baseResponce.message);
            }
        });
    }

//    @OnClick({R.id.tv_sendcode})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.tv_sendcode:
//                break;
//        }
//    }

}
