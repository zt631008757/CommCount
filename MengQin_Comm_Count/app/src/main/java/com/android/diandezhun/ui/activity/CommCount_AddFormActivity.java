package com.android.diandezhun.ui.activity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.baselibrary.interface_.OkHttpCallBack;
import com.android.baselibrary.responce.BaseResponce;
import com.android.baselibrary.tool.CommLoading;
import com.android.baselibrary.tool.CommToast;
import com.android.baselibrary.util.DateUtil;
import com.android.baselibrary.util.ImageUtil;
import com.android.baselibrary.util.Util;
import com.android.baselibrary.util.ZXingUtil;
import com.android.baselibrary.view.Comm_HeadView;
import com.android.baselibrary.view.Comm_SubmitBtnView;
import com.android.baselibrary.view.MultiStateView;
import com.android.diandezhun.R;
import com.android.diandezhun.bean.Count_DetailInfo;
import com.android.diandezhun.bean.Count_FormRecord;
import com.android.diandezhun.manager.API_Manager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.luck.picture.lib.tools.BitmapUtils;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class CommCount_AddFormActivity extends BaseActivity {

    @BindView(R.id.multiplestatusView)
    MultiStateView multiplestatusView;
    @BindView(R.id.comm_title)
    Comm_HeadView comm_title;
    @BindView(R.id.ll_content)
    LinearLayout ll_content;
    @BindView(R.id.ll_head)
    LinearLayout ll_head;
    @BindView(R.id.tv_time)
    TextView tv_time;
    @BindView(R.id.tv_company)
    TextView tv_company;
    @BindView(R.id.btn_save)
    Comm_SubmitBtnView btn_save;
    @BindView(R.id.ll_logo)
    LinearLayout ll_logo;
    @BindView(R.id.iv_marker)
    ImageView iv_marker;
    @BindView(R.id.ll_form_content)
    LinearLayout ll_form_content;






    Map<String, List<Count_DetailInfo>> map;
    Count_FormRecord formRecordInfo;
    boolean isAdd = false;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_commcount_addform;
    }

    @Override
    protected void initView() {
        comm_title.setTitle("出库单");

        Bitmap erweima = ZXingUtil.generateBitmap("https://www.pgyer.com/diandezhun", Util.dip2px(mContext, 80), Util.dip2px(mContext, 80));
        iv_marker.setImageBitmap(erweima);
    }

    @Override
    protected View getImmersionTitleBar() {
        return ll_head;
    }

    @Override
    protected void initData() {
        isAdd = getIntent().getBooleanExtra("isAdd", false);
        formRecordInfo = (Count_FormRecord) getIntent().getSerializableExtra("formRecordInfo");

        map = new Gson().fromJson(formRecordInfo.countDetailList, new TypeToken<Map<String, List<Count_DetailInfo>>>() {
        }.getType());

        for (String title : map.keySet()) {
            View view = createView(title, (List<Count_DetailInfo>) (map.get(title)));
            ll_form_content.addView(view);
        }

        tv_time.setText(DateUtil.getDateFromTimeLine(System.currentTimeMillis(), "yyyy-MM-dd"));
        List<String> companyList = new Gson().fromJson(formRecordInfo.company, new TypeToken<List<String>>() {
        }.getType());
        tv_company.setText(companyList.toString());

        if (isAdd) {
            //未保存，就是保存到本地
            btn_save.setText("保存");
        } else {
            //已保存，就是分享
            btn_save.setText("分享");
        }
    }

    private View createView(String type, List<Count_DetailInfo> list) {
        View view = View.inflate(mContext, R.layout.item_commcount_addform_content1, null);
        LinearLayout ll_content = view.findViewById(R.id.ll_content);
        TextView tv_type = view.findViewById(R.id.tv_type);
        int countTotal = 0;
        double lengthTotal = 0;
        for (Count_DetailInfo count_detailInfo : list) {
            double num1 = count_detailInfo.length;
            int num2 = count_detailInfo.count;
            double num3 = count_detailInfo.length * count_detailInfo.count;
            ll_content.addView(createSubView(num1 + "", num2 + "", num3 + "", false));

            countTotal += num2;
            lengthTotal += num3;
        }

        tv_type.setText(type);
        //合计
        ll_content.addView(createSubView("合计", countTotal + "", lengthTotal + "", true));
        return view;
    }

    private View createSubView(String num1, String num2, String num3, boolean isOrangeColor) {
        View view = View.inflate(mContext, R.layout.item_commcount_addform_content, null);
        TextView tv_length = view.findViewById(R.id.tv_length);
        TextView tv_num = view.findViewById(R.id.tv_num);
        TextView tv_subtotal = view.findViewById(R.id.tv_subtotal);

        tv_length.setText(num1);
        tv_num.setText(num2);
        tv_subtotal.setText(num3);
        if (isOrangeColor) {
            tv_num.setTextColor(Color.parseColor("#ff7902"));
            tv_subtotal.setTextColor(Color.parseColor("#ff7902"));
        }

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Util.dip2px(mContext, 40));
        layoutParams.topMargin = 1;
        view.setLayoutParams(layoutParams);
        return view;
    }

    @OnClick({R.id.btn_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_save:
                if (isAdd) {
                    save();
                } else {
                    share();
                }
                break;
        }

    }

    //保存
    private void save() {
        formRecordInfo.save();
        finish();
    }

    //分享
    private void share() {
        CommLoading.showLoading(mContext);
        ll_logo.setVisibility(View.VISIBLE);
        ll_logo.postDelayed(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = loadBitmapFromView(ll_content);
                ImageUtil.saveImageToGallery(mContext, bitmap);

                CommToast.showToast(mContext,"已保存到相册");
                ll_logo.setVisibility(View.GONE);
                CommLoading.dismissLoading();
            }
        },100);
    }

    private void layoutView(View v, int width, int height) {
        // 整个View的大小 参数是左上角 和右下角的坐标
        v.layout(0, 0, width, height);
        int measuredWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        int measuredHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.AT_MOST);
        /** 当然，measure完后，并不会实际改变View的尺寸，需要调用View.layout方法去进行布局。
         * 按示例调用layout函数后，View的大小将会变成你想要设置成的大小。
         */
        v.measure(measuredWidth, measuredHeight);
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
    }

    private Bitmap loadBitmapFromView(View v) {
        int w = v.getWidth();
        int h = v.getHeight();
        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);

        c.drawColor(Color.WHITE);
        /** 如果不设置canvas画布为白色，则生成透明 */
        v.layout(0, 0, w, h);
        v.draw(c);
        return bmp;
    }

}
