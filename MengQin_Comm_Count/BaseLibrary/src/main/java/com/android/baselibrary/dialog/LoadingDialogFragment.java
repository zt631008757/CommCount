package com.android.baselibrary.dialog;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import com.android.baselibrary.R;

import java.util.Map;

/**
 * Created by Administrator on 2018/8/29.
 */

public class LoadingDialogFragment extends BaseDialogFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_commloading;
    }

    @Override
    protected void initData() {

    }

    TextView tv_text;
    String text;

    @Override
    protected void initView() {
        tv_text = mRootView.findViewById(R.id.tv_text);
        tv_text.setText(text);

        if (!TextUtils.isEmpty(text)) {
            tv_text.setVisibility(View.VISIBLE);
            tv_text.setText(text);
        } else {
            tv_text.setVisibility(View.GONE);
        }
    }

    @Override
    public void setData(Map<String, Object> params) {
        if (params != null) {
            {
                this.text = (String) params.get("text");
            }
        }
    }

    public static BaseDialogFragment showDialog(FragmentManager fragmentManager, Map<String, Object> params) {
        LoadingDialogFragment dialogFragment = new LoadingDialogFragment();
        dialogFragment.setData(params);
        dialogFragment.show(fragmentManager, "");
        return dialogFragment;
    }


}
