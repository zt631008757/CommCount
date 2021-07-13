package com.android.diandezhun.dialog;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.baselibrary.dialog.BaseDialogFragment;
import com.android.baselibrary.interface_.CommCallBack;
import com.android.baselibrary.util.Util;
import com.android.baselibrary.view.Comm_EditView;
import com.android.baselibrary.view.GridSpacingItemDecoration;
import com.android.diandezhun.R;
import com.android.diandezhun.adapter.Count_LengthListAdapter;
import com.android.diandezhun.bean.Count_CompanyInfo;
import com.android.diandezhun.bean.Count_DetailInfo;
import com.android.diandezhun.bean.Count_LengthInfo;
import com.android.diandezhun.ui.view.WordWrapView;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/8/29.
 */

public class Count_AddDetails_DialogFragment extends BaseDialogFragment {

    @BindView(R.id.recyclerview_length)
    RecyclerView recyclerview_length;
    @BindView(R.id.et_length)
    EditText et_length;
    @BindView(R.id.et_company)
    Comm_EditView et_company;
    @BindView(R.id.wordwrapview)
    WordWrapView wordwrapview;
    @BindView(R.id.tv_count)
    TextView tv_count;
    @BindView(R.id.tv_all_length)
    TextView tv_all_length;
    @BindView(R.id.et_remark)
    Comm_EditView et_remark;


    Count_DetailInfo countDetailInfo;
    Count_LengthListAdapter adapter_length;

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_adddetails;
    }

    @Override
    protected void initData() {
        //获取历史长度
        List<Count_LengthInfo> allLength = LitePal.findAll(Count_LengthInfo.class);
        List<String> list_length = new ArrayList<>();
        for (Count_LengthInfo lengthInfo : allLength) {
            list_length.add(lengthInfo.length);
        }
        adapter_length.setData(list_length);

        //获取历史公司名
        List<Count_CompanyInfo> allCompany = LitePal.findAll(Count_CompanyInfo.class);
        for (Count_CompanyInfo companyInfo : allCompany) {
            View view = View.inflate(mContext, R.layout.item_count_company, null);
            TextView tv_text = view.findViewById(R.id.tv_text);
            tv_text.setText(companyInfo.company);
            tv_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callBack_company.onResult(companyInfo.company);
                }
            });
            wordwrapview.addView(view);
        }

        //初始化数据
        if (countDetailInfo != null) {
            tv_count.setText(countDetailInfo.count + "");
            et_length.setText(countDetailInfo.length + "");
            if (!TextUtils.isEmpty(countDetailInfo.remark)) {
                et_remark.setText(countDetailInfo.remark);
            }
            if (!TextUtils.isEmpty(countDetailInfo.company)) {
                et_company.setText(countDetailInfo.company);
            }
        }
    }

    @Override
    protected void initView() {
        mContext.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        recyclerview_length.setLayoutManager(new GridLayoutManager(mContext, 5));
        recyclerview_length.addItemDecoration(new GridSpacingItemDecoration(5, Util.dip2px(mContext, 10), false));
        adapter_length = new Count_LengthListAdapter(mContext, callBack_length);
        recyclerview_length.setAdapter(adapter_length);

        et_length.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                jisuanLength();
            }
        });
    }

    //计算总长度
    public void jisuanLength() {
        double length = 0.0;
        int count = 0;
        try {
            length = Double.parseDouble(et_length.getText().toString());
            count = Integer.parseInt(tv_count.getText().toString());
        } catch (Exception e) {

        }
        double allLength = length * count;

        tv_all_length.setText(allLength + "");
    }

    CommCallBack callBack_length = new CommCallBack() {
        @Override
        public void onResult(Object obj) {
            String length = (String) obj;
            et_length.setText(length);
            et_length.setSelection(et_length.getText().length());
        }
    };

    CommCallBack callBack_company = new CommCallBack() {
        @Override
        public void onResult(Object obj) {
            String company = (String) obj;
            et_company.setText(company);
        }
    };

    @Override
    protected int getShowDirection() {
        return BaseDialogFragment.Direction_FromBottom;
    }

    @Override
    public void setData(Map<String, Object> params) {
        if (params != null) {
            countDetailInfo = (Count_DetailInfo) params.get("countDetailInfo");
        }
    }

    public static BaseDialogFragment showDialog(FragmentManager fragmentManager, Map<String, Object> params) {
        Count_AddDetails_DialogFragment dialogFragment = new Count_AddDetails_DialogFragment();
        dialogFragment.setData(params);
        dialogFragment.show(fragmentManager, "");
        return dialogFragment;
    }

    @OnClick({R.id.csb_ok, R.id.tv_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.csb_ok:
                save();
                break;
            case R.id.tv_cancel:
                dismissWithAnim();
                break;
        }
    }

    public void save() {
        //先保存历史长度
        try {
            double length = Double.parseDouble(et_length.getText().toString());
            Count_LengthInfo lengthInfo = new Count_LengthInfo(et_length.getText().toString());
            lengthInfo.saveOrUpdate();
            countDetailInfo.length = length;
        } catch (Exception e) {

        }

        //保存历史公司
        String company = et_company.getText();
        if (!TextUtils.isEmpty(company)) {
            Count_CompanyInfo companyInfo = new Count_CompanyInfo(company);
            companyInfo.saveOrUpdate();
            countDetailInfo.company = company;
        }
        //备注信息
        String remark = et_remark.getText();
        if (!TextUtils.isEmpty(remark)) {
            countDetailInfo.remark = remark;
        }

        //保存信息
        countDetailInfo.editTime = System.currentTimeMillis();

        if (callBack != null) {
            callBack.onResult(countDetailInfo);
        }
    }
}
