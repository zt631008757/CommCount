package com.android.diandezhun.ui.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.baselibrary.dialog.Comm_DialogFragment;
import com.android.baselibrary.interface_.CommCallBack;
import com.android.baselibrary.ui.BaseFragment;
import com.android.baselibrary.view.MultiStateView;
import com.android.diandezhun.R;
import com.android.diandezhun.adapter.CommCount_Record_FormAdapter;
import com.android.diandezhun.adapter.CommCount_Record_HistoryAdapter;
import com.android.diandezhun.bean.Count_DetailInfo;
import com.android.diandezhun.bean.Count_FormRecord;
import com.android.diandezhun.ui.activity.CommCount_AddFormActivity;
import com.google.gson.Gson;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import org.litepal.LitePal;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class CommCount_Record_FormFragment extends BaseFragment {

    @BindView(R.id.multiplestatusView)
    MultiStateView multiplestatusView;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.smartrefreshlayout)
    SmartRefreshLayout smartrefreshlayout;
    @BindView(R.id.ll_bottom_manage)
    LinearLayout ll_bottom_manage;
    @BindView(R.id.iv_quanxuan)
    ImageView iv_quanxuan;

    CommCount_Record_FormAdapter adapter;
    boolean isShowManage = false;  //显示管理界面
    List<Count_FormRecord> list;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_commcount_record_form;
    }

    @Override
    protected void initView() {

        multiplestatusView.setViewState(MultiStateView.ViewState.LOADING);
        multiplestatusView.setOnRetryListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                multiplestatusView.setViewState(MultiStateView.ViewState.LOADING);
                getData();
            }
        });

        recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new CommCount_Record_FormAdapter(mContext, callBack);
        recyclerview.setAdapter(adapter);

        smartrefreshlayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getData();
            }
        });
        smartrefreshlayout.setEnableLoadMore(false);
        setShowManage(false);

    }

    CommCallBack callBack = new CommCallBack() {
        @Override
        public void onResult(Object obj) {
            Pair<String, Object> pair = (Pair<String, Object>) obj;
            if ("select".equals(pair.first)) {
                showIsAllSelect();
            } else if ("itemClick".equals(pair.first)) {
                //列表点击
                Count_FormRecord count_formRecord = (Count_FormRecord) pair.second;
                Intent intent = new Intent(mContext, CommCount_AddFormActivity.class);
                intent.putExtra("formRecordInfo", count_formRecord);
                startActivity(intent);

            }
        }
    };

    @Override
    protected void initData() {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mContext != null) {
            getData();
        }
    }

    public void setShowManage(boolean ShowManage) {
        isShowManage = ShowManage;
        if (isShowManage) {
            ll_bottom_manage.setVisibility(View.VISIBLE);
        } else {
            ll_bottom_manage.setVisibility(View.GONE);
        }
//        adapter.setShowManage(isShowManage);
        getData();
    }

    private void getData() {
        list = LitePal.order("editTime desc").find(Count_FormRecord.class);

        adapter.setData(list,isShowManage);
        if (adapter.getItemCount() == 0) {
            multiplestatusView.setViewState(MultiStateView.ViewState.EMPTY);
        } else {
            multiplestatusView.setViewState(MultiStateView.ViewState.CONTENT);
        }
        showIsAllSelect();
    }

    @OnClick({R.id.tv_delete, R.id.tv_collect, R.id.ll_quanxuan})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_delete:   //删除
                Comm_DialogFragment.showCommDialog(getFragmentManager(), "", "确定删除吗", "确定", "取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        delete();
                    }
                }, null);
                break;
            case R.id.tv_collect:   //汇总
                break;

            case R.id.ll_quanxuan:   //全选
                if (isAllSelect()) {
                    for (Count_FormRecord count_formRecord : list) {
                        count_formRecord.isSelect = 0;
                        count_formRecord.save();
                    }
                } else {
                    for (Count_FormRecord count_formRecord : list) {
                        count_formRecord.isSelect = 1;
                        count_formRecord.save();
                    }
                }
                adapter.notifyDataSetChanged();
                showIsAllSelect();
                break;
        }
    }

    //删除
    private void delete() {
        for (Count_FormRecord count_formRecord : list) {
            if (count_formRecord.isSelect==1) {
                count_formRecord.delete();
            }
        }
        getData();
    }


    private boolean isAllSelect() {
        boolean isAllSelect = true;
        for (Count_FormRecord count_formRecord : list) {
            if (count_formRecord.isSelect == 0) {
                isAllSelect = false;
            }
        }
        return isAllSelect;
    }

    private void showIsAllSelect() {
        if (isAllSelect()) {
            iv_quanxuan.setImageResource(R.drawable.ico_select);
        } else {
            iv_quanxuan.setImageResource(R.drawable.ico_unselect);
        }
    }


}
