package com.android.baselibrary.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;


import com.android.baselibrary.R;
import com.android.baselibrary.interface_.CommCallBack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/8/20.
 */

public class MyBottomTabView extends RelativeLayout {

    Context mContext;
    View rootView;

    public MyBottomTabView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public MyBottomTabView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public MyBottomTabView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    public List<Integer> ico_normal;
    public List<Integer> ico_select;
    public List<String> tab_name;
    int TEXT_COLOR_NORMAL = Color.parseColor("#999999");
    int TEXT_COLOR_PRESSED = Color.parseColor("#333333");


    List<RelativeLayout> list = new ArrayList<>();
    LinearLayout ll_content;
    RelativeLayout rl_center;
    ImageView iv_center;

    CommCallBack callBack;
    int currentSelect = 0;
    boolean hasCenter = false;
    int icoCenter = R.drawable.ico_home_bottom_center_;
    public int centerIndex = -1;

    public void setTextColor(int NORMAL, int PRESSED) {
        TEXT_COLOR_NORMAL = NORMAL;
        TEXT_COLOR_PRESSED = PRESSED;
    }

    public void setData(List<String> tab_name, List<Integer> ico_normal, List<Integer> ico_select, boolean hasCenter, int icoCenter) {
        this.hasCenter = hasCenter;
        this.ico_normal = ico_normal;
        this.ico_select = ico_select;
        this.tab_name = tab_name;
        this.icoCenter = icoCenter;
        if (hasCenter) {
            centerIndex = tab_name.size() / 2;
            this.ico_normal.add(centerIndex, 0);
            this.ico_select.add(centerIndex, 0);
            this.tab_name.add(centerIndex, "");
        }
        initTab();
    }


    private void init() {
        View.inflate(mContext, R.layout.view_mybottomview, this);
        ll_content = findViewById(R.id.ll_content);
        rl_center = findViewById(R.id.rl_center);
        iv_center = findViewById(R.id.iv_center);

        if (tab_name == null) {
            tab_name = new ArrayList<>();
            tab_name.add("首页");
            tab_name.add("我的");
        }
        if (ico_normal == null) {
            ico_normal = new ArrayList<>();
            ico_normal.add(R.drawable.ico_home_main_normal);
            ico_normal.add(R.drawable.ico_home_my_normal);
        }
        if (ico_select == null) {
            ico_select = new ArrayList<>();
            ico_select.add(R.drawable.ico_home_main_select);
            ico_select.add(R.drawable.ico_home_my_select);
        }
        initTab();
    }

    private void initTab() {
        if (hasCenter) {
            rl_center.setVisibility(VISIBLE);
            iv_center.setImageResource(icoCenter);
        } else {
            rl_center.setVisibility(GONE);
        }

        list.clear();
        ll_content.removeAllViews();
        for (int i = 0; i < tab_name.size(); i++) {
            RelativeLayout relativeLayout = (RelativeLayout) View.inflate(mContext, R.layout.view_mybottomtabview_item, null);
            TextView tv_name = relativeLayout.findViewById(R.id.tv_name);
            ImageView iv_img = relativeLayout.findViewById(R.id.iv_img);
            tv_name.setText(tab_name.get(i));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
            params.weight = 1;
            relativeLayout.setLayoutParams(params);
            final int finalI = i;
            relativeLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callBack != null) {
                        callBack.onResult(finalI);
                    }
                }
            });
            list.add(relativeLayout);
            ll_content.addView(relativeLayout);
        }
        setSelect(0);
    }

    public void setOnTabSelectCallBack(CommCallBack callBack) {
        this.callBack = callBack;
    }

    public void setSelect(int index) {
        currentSelect = index;
        for (int i = 0; i < list.size(); i++) {
            RelativeLayout relativeLayout = list.get(i);
            ImageView iv_img = relativeLayout.findViewById(R.id.iv_img);
            TextView tv_name = relativeLayout.findViewById(R.id.tv_name);
            if (currentSelect == i) {
                iv_img.setImageResource(ico_select.get(i));
                tv_name.setTextColor(TEXT_COLOR_PRESSED);
                tv_name.getPaint().setFakeBoldText(true);
            } else {
                iv_img.setImageResource(ico_normal.get(i));
                tv_name.setTextColor(TEXT_COLOR_NORMAL);
                tv_name.getPaint().setFakeBoldText(false);
            }
        }
    }

    public void setHasNew(int num) {
        RelativeLayout relativeLayout = list.get(2);
        View view_new = relativeLayout.findViewById(R.id.view_new);
        if (num > 0) {
            view_new.setVisibility(VISIBLE);
        } else {
            view_new.setVisibility(GONE);
        }
    }

    public void setHasNew(int index, int num) {
        RelativeLayout relativeLayout = list.get(index);
        View view_new = relativeLayout.findViewById(R.id.view_new);
        if (num > 0) {
            view_new.setVisibility(VISIBLE);
        } else {
            view_new.setVisibility(GONE);
        }
    }
}
