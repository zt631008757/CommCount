package com.android.diandezhun.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.baselibrary.interface_.CommCallBack;
import com.android.diandezhun.R;
import com.android.diandezhun.bean.CommCount_Type;

import java.util.List;

/**
 * Created by Administrator on 2018/8/22.
 */

public class CommCount_Record_TypeManageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context mContext;
    List<CommCount_Type> list;
    CommCallBack callBack;

    public CommCount_Record_TypeManageAdapter(Context mContext, CommCallBack callBack) {
        this.mContext = mContext;
        this.callBack = callBack;
    }

    public void setData(List<CommCount_Type> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        ViewGroup viewGroup = (ViewGroup) mInflater.inflate(R.layout.item_commcount_record_typemanage, null, false);
        ContentViewHolder holder = new ContentViewHolder(viewGroup);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ContentViewHolder viewHolder = (ContentViewHolder) holder;
        final CommCount_Type info = list.get(position);
        //数据绑定逻辑
        viewHolder.tv_type.setText(info.title);
        if (info.isHide==1) {
            viewHolder.iv_img.setVisibility(View.GONE);
        } else {
            viewHolder.iv_img.setVisibility(View.VISIBLE);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                info.isHide =Math.abs(info.isHide - 1);
                notifyDataSetChanged();

//                if (callBack != null) {
//                    callBack.onResult(info);
//                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }


    class ContentViewHolder extends RecyclerView.ViewHolder {
        TextView tv_type;
        ImageView iv_img;

        public ContentViewHolder(View itemView) {
            super(itemView);
            tv_type = itemView.findViewById(R.id.tv_type);
            iv_img = itemView.findViewById(R.id.iv_img);
        }
    }
}
