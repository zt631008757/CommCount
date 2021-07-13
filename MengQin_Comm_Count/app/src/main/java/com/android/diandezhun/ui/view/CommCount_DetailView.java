package com.android.diandezhun.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.android.baselibrary.util.DateUtil;
import com.android.diandezhun.R;
import com.android.diandezhun.bean.Count_DetailInfo;

public class CommCount_DetailView extends LinearLayout {

    Context context;

    public CommCount_DetailView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView(attrs);
    }

    public CommCount_DetailView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView(attrs);
    }

    Count_DetailInfo info;

    LinearLayout ll_company, ll_remark, ll_time, ll_all_length;
    TextView tv_count, tv_length, tv_all_length, tv_company, tv_remark, tv_time;

    private void initView(AttributeSet attrs) {
        View.inflate(context, R.layout.view_commcount_detail, this);
        ll_company = findViewById(R.id.ll_company);
        ll_remark = findViewById(R.id.ll_remark);
        ll_time = findViewById(R.id.ll_time);
        ll_all_length = findViewById(R.id.ll_all_length);

        tv_count = findViewById(R.id.tv_count);
        tv_length = findViewById(R.id.tv_length);
        tv_all_length = findViewById(R.id.tv_all_length);
        tv_company = findViewById(R.id.tv_company);
        tv_remark = findViewById(R.id.tv_remark);
        tv_time = findViewById(R.id.tv_time);

    }

    public void setData(Count_DetailInfo info) {
        tv_count.setText(info.count + "");
        if (info.length == 0) {
            tv_length.setText("根");
            ll_all_length.setVisibility(GONE);
        } else {
            tv_length.setText("根/" + info.length + "米");
            ll_all_length.setVisibility(VISIBLE);
            tv_all_length.setText((info.length * info.count) + "米");
        }

        //公司
        tv_company.setText(info.company);
        ll_company.setVisibility(TextUtils.isEmpty(info.company) ? GONE : VISIBLE);
        //备注
        tv_remark.setText(info.remark);
        ll_remark.setVisibility(TextUtils.isEmpty(info.remark) ? GONE : VISIBLE);
        //时间
        if (info.editTime != 0) {
            tv_time.setText(DateUtil.getDateFromTimeLine(info.editTime));
            ll_time.setVisibility(VISIBLE);
        } else {
            ll_time.setVisibility(GONE);
        }

    }
}
