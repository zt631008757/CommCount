package com.android.diandezhun.ui.fragment;

import android.content.Intent;
import android.database.Cursor;
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
import com.android.baselibrary.interface_.OkHttpCallBack;
import com.android.baselibrary.responce.BaseResponce;
import com.android.baselibrary.tool.CommToast;
import com.android.baselibrary.tool.Log;
import com.android.baselibrary.ui.BaseFragment;
import com.android.baselibrary.view.MultiStateView;
import com.android.diandezhun.R;
import com.android.diandezhun.adapter.CommCount_Record_HistoryAdapter;
import com.android.diandezhun.bean.CommCount_Type;
import com.android.diandezhun.bean.Count_DetailInfo;
import com.android.diandezhun.bean.Count_FormRecord;
import com.android.diandezhun.bean.History_FillterInfo;
import com.android.diandezhun.event.Event_Count_LocalData_CountDataChange;
import com.android.diandezhun.event.Event_Count_LocalData_TypeListChange;
import com.android.diandezhun.manager.API_Manager;
import com.android.diandezhun.manager.SqlHelper;
import com.android.diandezhun.ui.activity.CommCount_AddFormActivity;
import com.android.diandezhun.ui.activity.CommCount_History_FillterActivity;
import com.android.diandezhun.ui.activity.CommCount_Record_CountTypeManageActivity;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class CommCount_Record_HistoryFragment extends BaseFragment {

    @BindView(R.id.multiplestatusView)
    MultiStateView multiplestatusView;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.smartrefreshlayout)
    SmartRefreshLayout smartrefreshlayout;
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.ll_bottom_manage)
    LinearLayout ll_bottom_manage;
    @BindView(R.id.iv_type_manage)
    ImageView iv_type_manage;
    @BindView(R.id.iv_quanxuan)
    ImageView iv_quanxuan;


    CommCount_Record_HistoryAdapter adapter;
    boolean isShowManage = false;  //显示管理界面
    List<Count_DetailInfo> list;
    List<CommCount_Type> showList = new ArrayList<>();
    String currentCountType;

    int currentTabIndex = 0;

    History_FillterInfo fillterInfo;   //筛选条件

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_commcount_record_history;
    }

    @Override
    protected boolean isRegistEventbus() {
        return true;
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
        adapter = new CommCount_Record_HistoryAdapter(mContext, callBack);
        recyclerview.setAdapter(adapter);

        smartrefreshlayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getData();
            }
        });
        smartrefreshlayout.setEnableLoadMore(false);
        setShowManage(false);

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
//                currentTabIndex = (int) tab.getTag();
                Log.i("currentTabIndex:" + currentTabIndex);
                currentCountType = tab.getText().toString();
                getData();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    CommCallBack callBack = new CommCallBack() {
        @Override
        public void onResult(Object obj) {
            Pair<String, Object> pair = (Pair<String, Object>) obj;
            if ("select".equals(pair.first)) {
                showIsAllSelect();
            } else if ("itemClick".equals(pair.first)) {
                //列表点击
                Count_DetailInfo info = (Count_DetailInfo) pair.second;

            }
        }
    };

    @Override
    protected void initData() {
        getTypeList();
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
        getData();
//        adapter.setShowManage(isShowManage);
    }

    public void toFillter() {
        Intent intent = new Intent(mContext, CommCount_History_FillterActivity.class);
        intent.putExtra("fillterInfo", fillterInfo);
        startActivityForResult(intent, 100);
    }

    private void getData() {
        //刷新列表数据
        if (TextUtils.isEmpty(currentCountType)) return;
        Log.i("刷新列表数据");
        list = getLocalData(currentCountType, fillterInfo, false);
        adapter.setData(list, isShowManage);
        if (adapter.getItemCount() == 0) {
            multiplestatusView.setViewState(MultiStateView.ViewState.EMPTY);
        } else {
            multiplestatusView.setViewState(MultiStateView.ViewState.CONTENT);
        }

        smartrefreshlayout.finishRefresh();
        smartrefreshlayout.finishLoadMore();
        showIsAllSelect();
    }

    //根据类型 查本地列表
    private List<Count_DetailInfo> getLocalData(String countType, History_FillterInfo fillterInfo, boolean isSelect) {

        String query = "";
        if (fillterInfo != null) {
            query += "countType = '" + countType + "' and editTime > " + fillterInfo.startTime + " and editTime < +" + fillterInfo.endTime + " and inAndOutType = " + fillterInfo.inAndOutType;
        } else {
            query += "countType = '" + countType + "'";
        }
        if (isSelect) {
            query += " and isSelect = '1'";
        }
        List<Count_DetailInfo> localDataList = LitePal.where(query).order("editTime desc").find(Count_DetailInfo.class);
//        if (fillterInfo != null) {
//            localDataList = LitePal.where("countType = ? and editTime > ? and editTime < ? and inAndOutType = ?", countType, fillterInfo.startTime + "", fillterInfo.endTime + "", fillterInfo.inAndOutType + "").order("editTime desc").find(Count_DetailInfo.class);
//        } else {
//            localDataList = LitePal.where("countType = ?", countType).order("editTime desc").find(Count_DetailInfo.class);
//        }
        return localDataList;
    }


    //获取模板
    private void template() {
        Log.i("接口获取分类");
//        int prePosion = currentTabIndex;
//        Log.i("prePosion:" + prePosion + "  tabs.getTabCount():" + tabs.getTabCount());
//        if (prePosion > tabs.getTabCount() - 1) prePosion = 0;
//        tabs.selectTab(tabs.getTabAt(prePosion));
        API_Manager.template(mContext, new OkHttpCallBack<BaseResponce<List<CommCount_Type>>>() {
            @Override
            public void onSuccess(BaseResponce<List<CommCount_Type>> baseResponce) {
                Log.i("分类存本地");
                List<CommCount_Type> typeList = baseResponce.getData();
                for (int i = 0; i < typeList.size(); i++) {
                    typeList.get(i).sort = i;
                    typeList.get(i).save();
                }
                getTypeList();
            }

            @Override
            public void onFailure(BaseResponce<List<CommCount_Type>> baseResponce) {
            }
        });
    }

    private void getTypeList() {
        List<CommCount_Type> typeList = LitePal.findAll(CommCount_Type.class);
        if (typeList != null && typeList.size() > 0) {
            tabs.removeAllTabs();
//            for (CommCount_Type commCount_type : typeList) {
//                if (commCount_type.isHide == false) {
//                    showList.add(commCount_type);
//                    tabs.addTab(tabs.newTab().setText(commCount_type.title));
//                }
//            }

            showList = LitePal.where("isHide=?", "0").order("sort").find(CommCount_Type.class);
            Log.i("showList.size:" + showList.size());
            for (CommCount_Type commCountType : showList) {
                tabs.addTab(tabs.newTab().setText(commCountType.title));
            }
//            Cursor cursor = LitePal.getDatabase().query("CommCount_Type", new String[]{"title"}, "ishide=?", new String[]{"false"}, null, null, "sort");
//            while (cursor.moveToNext()) {//遍历数据表中的数据
//                String title = cursor.getString(0);
//                tabs.addTab(tabs.newTab().setText(title) );
//                Log.i("title:" + title);
//            }
//            cursor.close();
            Log.i("本地获取分类");
        } else {
            template();
        }
    }

    @OnClick({R.id.tv_delete, R.id.tv_collect, R.id.iv_type_manage, R.id.ll_quanxuan})
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
                collect();
                break;
            case R.id.iv_type_manage:  //管理头部
                StartActivity(CommCount_Record_CountTypeManageActivity.class);
                break;
            case R.id.ll_quanxuan:   //全选
                if (isAllSelect()) {
                    for (Count_DetailInfo count_detailInfo : list) {
                        count_detailInfo.isSelect = 0;
                        count_detailInfo.save();
                    }
                } else {
                    for (Count_DetailInfo count_detailInfo : list) {
                        count_detailInfo.isSelect = 1;
                        count_detailInfo.save();
                    }
                }
                adapter.notifyDataSetChanged();
                showIsAllSelect();
                break;
        }
    }

    private boolean isAllSelect() {
        boolean isAllSelect = true;
        for (Count_DetailInfo count_detailInfo : list) {
            if (count_detailInfo.isSelect == 0) {
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

    //删除
    private void delete() {
        for (CommCount_Type commCountType : showList) {
            List<Count_DetailInfo> list = getLocalData(commCountType.title, fillterInfo, true);
            if (list != null && list.size() > 0) {
                for (Count_DetailInfo count_detailInfo : list) {
                    count_detailInfo.delete();
                }
            }
        }
//        for (Count_DetailInfo count_detailInfo : list) {
//            if (count_detailInfo.isSelect == 1) {
//                count_detailInfo.delete();
//            }
//        }
        getData();
    }

    //汇总
    private void collect() {
        List<String> companyList = new ArrayList<>();
        Map<String, List<Count_DetailInfo>> map = new HashMap<>();
        for (CommCount_Type commCountType : showList) {
            List<Count_DetailInfo> list = getLocalData(commCountType.title, fillterInfo, true);
            if (list != null && list.size() > 0) {
                map.put(commCountType.title, list);
                for (Count_DetailInfo count_detailInfo : list) {
                    if (!TextUtils.isEmpty(count_detailInfo.company) && !companyList.contains(count_detailInfo.company)) {
                        companyList.add(count_detailInfo.company);
                    }
                }
            }
        }

        if (map.size() > 0) {
            Count_FormRecord count_formRecord = new Count_FormRecord();
            count_formRecord.countDetailList = new Gson().toJson(map);
            //公司信息
            count_formRecord.company = new Gson().toJson(companyList);

            Intent intent = new Intent(mContext, CommCount_AddFormActivity.class);
            intent.putExtra("formRecordInfo", count_formRecord);
            intent.putExtra("isAdd", true);
            startActivity(intent);
        } else {
            CommToast.showToast(mContext, "至少选择一条记录");
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Event_Count_LocalData_CountDataChange event) {
        for (int i = 0; i < tabs.getTabCount(); i++) {
            TabLayout.Tab tab = tabs.getTabAt(i);
            String tabName = tab.getText().toString();
            if (tabName.equals(event.countType)) {
                tabs.selectTab(tab);
            }
        }
//        getData();
    }

    //头部类型变化
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Event_Count_LocalData_TypeListChange event) {
        getTypeList();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 200:
                fillterInfo = (History_FillterInfo) data.getSerializableExtra("fillterInfo");
                getData();
                break;
        }
    }
}
