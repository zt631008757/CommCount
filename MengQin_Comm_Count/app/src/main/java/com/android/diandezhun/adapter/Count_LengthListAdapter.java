package com.android.diandezhun.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.baselibrary.interface_.CommCallBack;
import com.android.diandezhun.R;

import java.util.List;

/**
 * Created by Administrator on 2018/8/22.
 */

public class Count_LengthListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context mContext;
    List<String> list;
    CommCallBack callBack;

    public Count_LengthListAdapter(Context mContext, CommCallBack callBack) {
        this.mContext = mContext;
        this.callBack = callBack;
    }

    public void setData(List<String> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        ViewGroup viewGroup = (ViewGroup) mInflater.inflate(R.layout.item_count_lengthlist, null, false);
        ContentViewHolder holder = new ContentViewHolder(viewGroup);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ContentViewHolder viewHolder = (ContentViewHolder) holder;
        final String info = list.get(position);
        //数据绑定逻辑
        viewHolder.tv_text.setText(info);

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

        public ContentViewHolder(View itemView) {
            super(itemView);
            tv_text = itemView.findViewById(R.id.tv_text);
        }
    }
}
