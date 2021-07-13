package com.android.baselibrary.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.baselibrary.R;

/**
 * 标题控件
 */
public class Comm_HeadView extends RelativeLayout {
    public Comm_HeadView(Context context) {
        super(context);
        this.context = context;
        initView(null);
    }

    public Comm_HeadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView(attrs);
    }

    public Comm_HeadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView(attrs);
    }

    Context context;
    ImageView public_title_left_img, public_title_right_img;
    TextView tv_title, tv_right_text;

    private void initView(AttributeSet attrs) {
        View.inflate(context, R.layout.view_comm_headview, this);
        public_title_left_img = findViewById(R.id.public_title_left_img);
        tv_title = findViewById(R.id.tv_title);
        public_title_right_img = findViewById(R.id.public_title_right_img);
        tv_right_text = findViewById(R.id.tv_right_text);

        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Comm_HeadView);

            int csb_textSize = typedArray.getInteger(R.styleable.Comm_HeadView_ctv_title_TextColor, 14);
            tv_title.setTextSize(csb_textSize);
            tv_title.setTextColor(typedArray.getColor(R.styleable.Comm_HeadView_ctv_title_TextColor, 0xffffff));
            tv_right_text.setTextColor(typedArray.getColor(R.styleable.Comm_HeadView_ctv_right_text_TextColor, 0xffffff));

            typedArray.recycle();
        }
    }


    public void setTitle(String text) {
        tv_title.setText(text);
    }

    public void setLeftImg(int resouce) {
        public_title_left_img.setImageResource(resouce);
    }

    public void setRightImg(int resouce, View.OnClickListener listener) {
        public_title_right_img.setVisibility(VISIBLE);
        public_title_right_img.setImageResource(resouce);
        public_title_right_img.setOnClickListener(listener);
    }

    public void setRightText(String text, View.OnClickListener listener) {
        tv_right_text.setVisibility(VISIBLE);
        tv_right_text.setText(text);
        tv_right_text.setOnClickListener(listener);
    }

    public void setLeftImgVisible(int visible) {
        public_title_left_img.setVisibility(visible);
    }

    public void setLeftClickFinish(Activity activity)
    {
        public_title_left_img.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
    }
}
