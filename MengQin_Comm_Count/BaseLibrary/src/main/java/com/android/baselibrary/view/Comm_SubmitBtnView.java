package com.android.baselibrary.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.baselibrary.R;


public class Comm_SubmitBtnView extends RelativeLayout {
    public Comm_SubmitBtnView(Context context) {
        super(context);
        this.context = context;
        initView(null);
    }

    public Comm_SubmitBtnView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView(attrs);
    }

    public Comm_SubmitBtnView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView(attrs);
    }

    Context context;
    public TextView tv_btn;

    private void initView(AttributeSet attrs) {
        View.inflate(context, R.layout.view_comm_submit_btn, this);
        tv_btn = findViewById(R.id.tv_btn);

        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Comm_SubmitBtnView);

            String text = typedArray.getString(R.styleable.Comm_SubmitBtnView_csb_text);
            tv_btn.setText(text);
            int csb_textSize = typedArray.getInteger(R.styleable.Comm_SubmitBtnView_csb_textSize,14);
            tv_btn.setTextSize(csb_textSize);

            int csb_bg  = typedArray.getResourceId(R.styleable.Comm_SubmitBtnView_csb_bg,-1);
            if(csb_bg!=-1)
            {
                tv_btn.setBackgroundResource(csb_bg);
            }
            typedArray.recycle();
        }
    }

    public void setText(String text)
    {
        tv_btn.setText(text);
    }

    public String getText()
    {
        return tv_btn.getText().toString();
    }
}
