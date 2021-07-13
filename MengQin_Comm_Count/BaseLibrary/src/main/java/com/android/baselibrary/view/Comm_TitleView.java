package com.android.baselibrary.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.android.baselibrary.R;

/**
 * Created by Administrator on 2018/9/20.
 */

public class Comm_TitleView extends LinearLayout {
    public Comm_TitleView(Context context) {
        super(context);
        this.context = context;
        initView(null);
    }

    public Comm_TitleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView(attrs);
    }

    public Comm_TitleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView(attrs);
    }

    Context context;
    public TextView tv_title, tv_content;
    public ImageView iv_img, iv_img1,iv_arr;
    View view_line;

    private void initView(AttributeSet attrs) {
        View.inflate(context, R.layout.view_comm_titleview, this);
        tv_title = findViewById(R.id.tv_title);
        iv_img = findViewById(R.id.iv_img);
        view_line = findViewById(R.id.view_line);
        tv_content = findViewById(R.id.tv_content);
        iv_img1 = findViewById(R.id.iv_img1);
        iv_arr= findViewById(R.id.iv_arr);

        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Comm_TitleView);
            //标题
            String text = typedArray.getString(R.styleable.Comm_TitleView_tv_titleName);
            tv_title.setText(text);
            //图片
            int imgSrc = typedArray.getResourceId(R.styleable.Comm_TitleView_tv_imgSrc, -1);
            if (imgSrc == -1) {
                iv_img.setVisibility(GONE);
            } else {
                iv_img.setVisibility(VISIBLE);
                iv_img.setImageResource(imgSrc);
            }

            //线
            boolean isShowLine = typedArray.getBoolean(R.styleable.Comm_TitleView_tv_showLine, false);
            view_line.setVisibility(isShowLine ? VISIBLE : GONE);
            //内容
            String content = typedArray.getString(R.styleable.Comm_TitleView_tv_content);
            tv_content.setText(content);
            //箭头
            boolean tv_showArr = typedArray.getBoolean(R.styleable.Comm_TitleView_tv_showArr, true);
            iv_arr.setVisibility(tv_showArr ? VISIBLE : GONE);

            typedArray.recycle();
        }
    }

    //设置标题
    public void setTv_title(String text) {
        tv_title.setText(text);
    }

    //设置内容
    public void setContent(String text) {
        tv_content.setText(text);
    }
}
