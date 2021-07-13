package com.android.baselibrary.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.android.baselibrary.R;
import com.android.baselibrary.interface_.CommCallBack;
import com.android.baselibrary.util.AnimUtil;
import com.android.baselibrary.util.Util;

/**
 * Created by Administrator on 2018/8/29.
 */

public class Comm_Dialog extends BaseDialog implements View.OnClickListener {
    public Comm_Dialog(@NonNull Context context) {
        super(context);
    }

    TextView tv_title, content_text, pop_ok, pop_cancel, pop_comit;

    View.OnClickListener okClickListener;
    View.OnClickListener cancelClickListener;
    String title, btnOkStr, btnCancelStr, msg;

    public static Dialog showCommDialog(Context context, String title, String msg,  String btnOkStr, String btnCancelStr, View.OnClickListener okClickListener, View.OnClickListener cancelClickListener) {
        Comm_Dialog dialog = new Comm_Dialog(context);
        dialog.setData(title, msg, btnOkStr, btnCancelStr);
        dialog.setCallBack(okClickListener, cancelClickListener);
        dialog.show();
        return dialog;
    }


    private void setData(String title, String msg,   String btnOkStr, String btnCancelStr) {
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
    protected int getLayoutId() {
        return R.layout.dialog_comm;
    }

    @Override
    protected void initView() {
        AnimUtil.fadeIn(view_bg);
        AnimUtil.fadeIn(ll_content);

        tv_title = findViewById(R.id.tv_title); //标题
        content_text = findViewById(R.id.content_text);  //提示文字
        pop_ok = findViewById(R.id.pop_ok);  //单个按钮文字
        pop_cancel = findViewById(R.id.pop_cancel);  //两个按钮 左边取消按钮
        pop_comit = findViewById(R.id.pop_comit);  //两个按钮 左边取消按钮

        content_text.setMovementMethod(ScrollingMovementMethod.getInstance());

        bindData();
    }

    protected void bindData()
    {
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
    }

}
