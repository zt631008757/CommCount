package com.android.diandezhun.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;

import com.android.baselibrary.interface_.CommCallBack;
import com.android.baselibrary.util.AnimUtil;
import com.android.baselibrary.util.StatusBarUtil_Dialog;
import com.android.baselibrary.util.Util;
import com.android.baselibrary.view.Comm_SubmitBtnView;
import com.android.diandezhun.R;
import com.android.diandezhun.constant.Constants;

/**
 * Created by Administrator on 2018/8/29.
 */

public class XieYi_Dialog extends Dialog implements View.OnClickListener {
    public XieYi_Dialog(@NonNull Context context) {
        super(context, R.style.myDialog);
    }

    public XieYi_Dialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected XieYi_Dialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    LinearLayout ll_content;
    View view_bg;
    TextView tv_text;

    CommCallBack callBack;
    Comm_SubmitBtnView csb_ok;
    TextView csb_cancel;
    TextView tv_title;

    //设置输入回调
    public void setIntputCallBack(CommCallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_xieyi);
        initView();
        StatusBarUtil_Dialog.setImmersiveStatusBar(this, false);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    private void initView() {
        ll_content = (LinearLayout) findViewById(R.id.ll_content);
        view_bg = findViewById(R.id.view_bg);
        view_bg.setOnClickListener(this);
        ll_content.setOnClickListener(this);
        AnimUtil.fadeIn(view_bg);
        AnimUtil.fadeIn(ll_content);

        csb_ok = findViewById(R.id.csb_ok);
        csb_cancel = findViewById(R.id.csb_cancel);

        tv_text = findViewById(R.id.tv_text);
        tv_title = findViewById(R.id.tv_title);

        tv_title.setText("欢迎使用"+ getContext().getString(R.string.app_name));
        String text = Constants.xieyi_tips;
        tv_text.setText(getTextSpan(text));
        tv_text.setMovementMethod(LinkMovementMethod.getInstance());

        csb_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.closeApp();
            }
        });
        csb_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callBack != null) {
                    callBack.onResult(null);
                }
            }
        });
    }

    public SpannableStringBuilder getTextSpan(String content) {
        SpannableStringBuilder ssb = new SpannableStringBuilder(content);

        ClickableSpan clickableSpan1 = new ClickableSpan() {
            public void onClick(View view) {
                //点击超链接时调用
//                Intent intent = new Intent(getContext(), YingSiActivity.class);
//                intent.putExtra("type", 1);
//                getContext().startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);//当传入true时超链接下会有一条下划线
            }
        };
        int start1 = content.indexOf("《");
        int end1 = content.indexOf("》") + 1;
        //设置超链接文本的颜色
        ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#6499FD")), start1, end1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.setSpan(clickableSpan1, start1, end1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        ClickableSpan clickableSpan2 = new ClickableSpan() {
            public void onClick(View view) {
//                Intent intent = new Intent(getContext(), YingSiActivity.class);
//                intent.putExtra("type", 0);
//                getContext().startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);//当传入true时超链接下会有一条下划线
            }
        };
        int start2 = content.lastIndexOf("《");
        int end2 = content.lastIndexOf("》") + 1;
        //设置超链接文本的颜色
        ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#6499FD")), start2, end2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.setSpan(clickableSpan2, start2, end2, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        return ssb;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_bg:
//                dismissWithAnim();
                break;
            case R.id.ll_content:
                //不处理，只消耗点击事件
                break;
        }
    }

    public void dismissWithAnim() {
        if (view_bg.getAnimation() != null) return;
        AnimUtil.fadeOut(ll_content, null);
        AnimUtil.fadeOut(view_bg, new CommCallBack() {
            @Override
            public void onResult(Object obj) {
                dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
//        dismissWithAnim();
    }
}
