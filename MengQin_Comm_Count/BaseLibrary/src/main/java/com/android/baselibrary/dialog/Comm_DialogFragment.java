package com.android.baselibrary.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.android.baselibrary.R;
import com.android.baselibrary.util.AnimUtil;
import com.android.baselibrary.util.Util;

import java.util.Map;

/**
 * Created by Administrator on 2018/8/29.
 */

public class Comm_DialogFragment extends BaseDialogFragment  {

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_comm;
    }

    @Override
    protected void initData() {

    }

    TextView tv_title, content_text, pop_ok, pop_cancel, pop_comit;

    View.OnClickListener okClickListener;
    View.OnClickListener cancelClickListener;
    String title, btnOkStr, btnCancelStr, msg;

    public static BaseDialogFragment showCommDialog(FragmentManager fragmentManager,String title, String msg, String btnOkStr, String btnCancelStr, View.OnClickListener okClickListener, View.OnClickListener cancelClickListener) {
        Comm_DialogFragment dialogFragment = new Comm_DialogFragment();
        dialogFragment.setData(title,msg,btnOkStr,btnCancelStr);
        dialogFragment.setCallBack(okClickListener, cancelClickListener);
        dialogFragment.show(fragmentManager, "");
        return dialogFragment;
    }

    private void setData(String title, String msg, String btnOkStr, String btnCancelStr) {
        this.title = title;
        this.btnOkStr = btnOkStr;
        this.btnCancelStr = btnCancelStr;
        this.msg = msg;
    }

    private void setCallBack(View.OnClickListener okClickListener, View.OnClickListener cancelClickListener) {
        this.cancelClickListener = cancelClickListener;
        this.okClickListener = okClickListener;
    }

    @Override
    protected void initView() {
        tv_title = mRootView.findViewById(R.id.tv_title); //标题
        content_text = mRootView.findViewById(R.id.content_text);  //提示文字
        pop_ok = mRootView.findViewById(R.id.pop_ok);  //单个按钮文字
        pop_cancel = mRootView.findViewById(R.id.pop_cancel);  //两个按钮 左边取消按钮
        pop_comit = mRootView.findViewById(R.id.pop_comit);  //两个按钮 左边取消按钮

        content_text.setMovementMethod(ScrollingMovementMethod.getInstance());
        bindData();
    }

    @Override
    public void setData(Map<String, Object> params) {

    }

    protected void bindData() {
        if (!TextUtils.isEmpty(title)) {
            tv_title.setVisibility(View.VISIBLE);
            tv_title.setText(title);
        } else {
            tv_title.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(msg)) {
            content_text.setText(msg);
        }
        if (!TextUtils.isEmpty(btnOkStr) && TextUtils.isEmpty(btnCancelStr))    //只有一个按钮
        {
            pop_ok.setVisibility(View.VISIBLE);
            pop_cancel.setVisibility(View.GONE);
            pop_comit.setVisibility(View.GONE);
            pop_ok.setText(btnOkStr);
        } else if (!TextUtils.isEmpty(btnOkStr) && !TextUtils.isEmpty(btnCancelStr))   //有两个按钮
        {
            pop_ok.setVisibility(View.GONE);
            pop_cancel.setVisibility(View.VISIBLE);
            pop_comit.setVisibility(View.VISIBLE);
            pop_cancel.setText(btnCancelStr);
            pop_comit.setText(btnOkStr);
        }

        pop_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissWithAnim();
                if (okClickListener != null)
                    okClickListener.onClick(null);
            }
        });
        pop_comit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.unallowQuickClick(pop_comit);
                dismissWithAnim();
                if (okClickListener != null)
                    okClickListener.onClick(null);
            }
        });
        pop_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissWithAnim();
                if (cancelClickListener != null)
                    cancelClickListener.onClick(null);
            }
        });
    }

}
