package com.android.diandezhun.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.baselibrary.interface_.CommCallBack;
import com.android.baselibrary.util.DateUtil;
import com.android.baselibrary.util.GlideUtil;
import com.android.diandezhun.R;
import com.android.diandezhun.bean.Count_DetailInfo;

import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * 历史记录
 */

public class CommCount_Record_HistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context mContext;
    List<Count_DetailInfo> list;
    CommCallBack callBack;
    boolean isShowManage = false;  //显示管理界面

    public CommCount_Record_HistoryAdapter(Context mContext, CommCallBack callBack) {
        this.mContext = mContext;
        this.callBack = callBack;
    }

    public void setData(List<Count_DetailInfo> list, boolean isShowManage) {
        this.list = list;
        this.isShowManage = isShowManage;
        notifyDataSetChanged();
    }

    public void setShowManage(boolean showManage) {
        isShowManage = showManage;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        ViewGroup viewGroup = (ViewGroup) mInflater.inflate(R.layout.item_count_history, null, false);
        ContentViewHolder holder = new ContentViewHolder(viewGroup);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ContentViewHolder viewHolder = (ContentViewHolder) holder;
        final Count_DetailInfo info = list.get(position);
        //数据绑定逻辑
        viewHolder.tv_countnum.setText(info.count + "根" + (info.length == 0 ? "" : ("/" + info.length + "米")));
        viewHolder.tv_length.setText(info.length == 0 ? "" : ("共" + info.length + "米"));
        viewHolder.iv_type.setImageResource(info.inAndOutType == 0 ? R.drawable.ico_count_type_out : R.drawable.ico_count_type_in);
        GlideUtil.displayImage(mContext, info.filePath, viewHolder.iv_img, R.drawable.ico_default_img_fang);

        //公司
        viewHolder.tv_company.setText(info.company);
        viewHolder.ll_company.setVisibility(TextUtils.isEmpty(info.company) ? GONE : VISIBLE);
        //备注
        viewHolder.tv_remark.setText(info.remark);
        viewHolder.ll_remark.setVisibility(TextUtils.isEmpty(info.remark) ? GONE : VISIBLE);
        //时间
        viewHolder.tv_time.setText(DateUtil.getDateFromTimeLine(info.editTime, "HH:mm"));
        viewHolder.ll_time.setVisibility(info.editTime == 0 ? GONE : VISIBLE);

        //分组
        if (position == 0 || !DateUtil.isSameDay(list.get(position - 1).editTime, info.editTime)) {
            //不是同一天，显示日期
            viewHolder.ll_group_time.setVisibility(VISIBLE);
            viewHolder.tv_group_time.setText(DateUtil.getTimestampString(info.editTime));

        } else {
            viewHolder.ll_group_time.setVisibility(GONE);
        }

        //管理模块
        if (isShowManage) {
            viewHolder.iv_group_select.setVisibility(View.INVISIBLE);
            viewHolder.iv_item_select.setVisibility(VISIBLE);
        } else {
            viewHolder.iv_group_select.setVisibility(GONE);
            viewHolder.iv_item_select.setVisibility(GONE);
        }
        if (info.isSelect == 1) {
            viewHolder.iv_item_select.setImageResource(R.drawable.ico_select);
        } else {
            viewHolder.iv_item_select.setImageResource(R.drawable.ico_unselect);
        }

        //选择框点击
        viewHolder.iv_item_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                info.isSelect = Math.abs(info.isSelect - 1);
                info.save();
                notifyDataSetChanged();
                if (callBack != null) {
                    Pair<String, Object> pair = new Pair<>("select", "");
                    callBack.onResult(pair);
                }
            }
        });


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callBack != null) {
                    Pair<String, Object> pair = new Pair<>("itemClick", info);
                    callBack.onResult(pair);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }


    class ContentViewHolder extends RecyclerView.ViewHolder {
        //列表
        TextView tv_countnum, tv_length, tv_company, tv_remark, tv_time;
        ImageView iv_img, iv_type, iv_item_select;
        LinearLayout ll_company, ll_remark, ll_time;

        //组
        LinearLayout ll_group_time;
        ImageView iv_group_select;
        TextView tv_group_time;


        public ContentViewHolder(View itemView) {
            super(itemView);
            iv_img = itemView.findViewById(R.id.iv_img);
            iv_type = itemView.findViewById(R.id.iv_type);
            tv_countnum = itemView.findViewById(R.id.tv_countnum);
            tv_length = itemView.findViewById(R.id.tv_length);
            tv_company = itemView.findViewById(R.id.tv_company);
            tv_remark = itemView.findViewById(R.id.tv_remark);
            tv_time = itemView.findViewById(R.id.tv_time);
            ll_company = itemView.findViewById(R.id.ll_company);
            ll_remark = itemView.findViewById(R.id.ll_remark);
            ll_time = itemView.findViewById(R.id.ll_time);
            iv_item_select = itemView.findViewById(R.id.iv_item_select);

            ll_group_time = itemView.findViewById(R.id.ll_group_time);
            iv_group_select = itemView.findViewById(R.id.iv_group_select);
            tv_group_time = itemView.findViewById(R.id.tv_group_time);

        }
    }
}
