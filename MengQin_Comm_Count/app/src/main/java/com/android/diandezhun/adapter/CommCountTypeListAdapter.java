package com.android.diandezhun.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.baselibrary.interface_.CommCallBack;
import com.android.baselibrary.util.GlideUtil;
import com.android.diandezhun.R;
import com.android.diandezhun.bean.CommCount_Type;

import java.util.List;

/**
 * Created by Administrator on 2018/8/22.
 */

public class CommCountTypeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context mContext;
    List<CommCount_Type> list;
    CommCallBack callBack;

    public CommCountTypeListAdapter(Context mContext, CommCallBack callBack) {
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
        ViewGroup viewGroup = (ViewGroup) mInflater.inflate(R.layout.item_commcount_type, null, false);
        ContentViewHolder holder = new ContentViewHolder(viewGroup);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ContentViewHolder viewHolder = (ContentViewHolder) holder;
        final CommCount_Type info = list.get(position);
        //数据绑定逻辑
        GlideUtil.displayImage(mContext, info.thumb, viewHolder.iv_img,-1);
        viewHolder.tv_text.setText(info.title);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callBack != null) {
                    callBack.onResult(info);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class ContentViewHolder extends RecyclerView.ViewHolder {
        TextView tv_text;
        RelativeLayout rl_content;
        ImageView iv_img;

        public ContentViewHolder(View itemView) {
            super(itemView);
            tv_text = itemView.findViewById(R.id.tv_text);
            rl_content = itemView.findViewById(R.id.rl_content);
            iv_img= itemView.findViewById(R.id.iv_img);
        }
    }
}
