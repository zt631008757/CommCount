package com.android.diandezhun.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.android.diandezhun.R;

public class CommCount_BottomBntView extends LinearLayout {

    Context context;

    public CommCount_BottomBntView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView(attrs);
    }

    public CommCount_BottomBntView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView(attrs);
    }

    ImageView iv_img;
    TextView tv_text;

    private void initView(AttributeSet attrs) {
        View.inflate(context, R.layout.view_commcount_bottombtn, this);
        iv_img = findViewById(R.id.iv_img);
        tv_text = findViewById(R.id.tv_text);

        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CommCount_BottomBntView);

            String content = typedArray.getString(R.styleable.CommCount_BottomBntView_cbb_text);
            tv_text.setText(content);

            int resouce = typedArray.getResourceId(R.styleable.CommCount_BottomBntView_cbb_img,0);
            iv_img.setImageResource(resouce);
        }
    }
}
