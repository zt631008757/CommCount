package com.android.diandezhun.dialog;

import android.view.View;
import androidx.fragment.app.FragmentManager;
import com.android.baselibrary.dialog.BaseDialogFragment;
import com.android.diandezhun.R;

import java.util.Map;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/8/29.
 */

public class Model_DialogFragment extends BaseDialogFragment {


    @Override
    protected int getLayoutId() {
        return R.layout.dialog_model;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected int getShowDirection() {
        return BaseDialogFragment.Direction_FromBottom;
    }

    @Override
    public void setData(Map<String, Object> params) {
        if (params != null) {
        }
    }

    public static BaseDialogFragment showDialog(FragmentManager fragmentManager, Map<String, Object> params) {
        Model_DialogFragment dialogFragment = new Model_DialogFragment();
        dialogFragment.setData(params);
        dialogFragment.show(fragmentManager, "");
        return dialogFragment;
    }


//    @OnClick({R.id.ll_share_wx })
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.ll_share_wx:
//
//                break;
//
//        }
//    }


}
