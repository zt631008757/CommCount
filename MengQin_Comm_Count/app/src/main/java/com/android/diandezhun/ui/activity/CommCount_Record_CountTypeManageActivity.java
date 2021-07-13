package com.android.diandezhun.ui.activity;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.baselibrary.interface_.OkHttpCallBack;
import com.android.baselibrary.responce.BaseResponce;
import com.android.baselibrary.tool.CommLoading;
import com.android.baselibrary.tool.CommToast;
import com.android.baselibrary.tool.Log;
import com.android.baselibrary.view.Comm_HeadView;
import com.android.baselibrary.view.MultiStateView;
import com.android.diandezhun.R;
import com.android.diandezhun.adapter.CommCount_Record_HistoryAdapter;
import com.android.diandezhun.adapter.CommCount_Record_TypeManageAdapter;
import com.android.diandezhun.bean.CommCount_Type;
import com.android.diandezhun.event.Event_Count_LocalData_TypeListChange;
import com.android.diandezhun.manager.API_Manager;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;

public class CommCount_Record_CountTypeManageActivity extends BaseActivity {

    @BindView(R.id.multiplestatusView)
    MultiStateView multiplestatusView;
    @BindView(R.id.comm_title)
    Comm_HeadView comm_title;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.smartrefreshlayout)
    SmartRefreshLayout smartrefreshlayout;

    CommCount_Record_TypeManageAdapter adapter;
    List<CommCount_Type> list;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_onlylist;
    }

    @Override
    protected void initView() {
        comm_title.setTitle("类型管理");
        comm_title.setRightText("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).sort = i;
                    list.get(i).save();
                }
                EventBus.getDefault().post(new Event_Count_LocalData_TypeListChange());
                finish();
            }
        });

        recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new CommCount_Record_TypeManageAdapter(mContext, null);
        recyclerview.setAdapter(adapter);

        smartrefreshlayout.setEnableLoadMore(false);
        smartrefreshlayout.setEnableRefresh(false);

        //为RecycleView绑定触摸事件
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                //首先回调的方法 返回int表示是否监听该方向
                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;//拖拽
                int swipeFlags = ItemTouchHelper.LEFT;//侧滑删除
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                //滑动事件
                Collections.swap(list, viewHolder.getAdapterPosition(), target.getAdapterPosition());
                adapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return false;
            }

            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                super.onSelectedChanged(viewHolder, actionState);
//                if (hasMoved) {
//                    sort();
//                    hasMoved = false;
//                }
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                //侧滑事件

            }

            @Override
            public boolean isLongPressDragEnabled() {
                //是否可拖拽
                return true;
            }
        });
        helper.attachToRecyclerView(recyclerview);

    }

    @Override
    protected void initData() {
        getDate();
    }

    //获取模板
    private void getDate() {
        list = LitePal.order("sort").find(CommCount_Type.class);
        adapter.setData(list);
    }

//    @OnClick({R.id.tv_sendcode})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.tv_sendcode:
//                break;
//        }
//    }

}
