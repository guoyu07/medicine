package com.yangs.medicine.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yangs.medicine.R;
import com.yangs.medicine.model.TopicList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangs on 2017/9/24 0024.
 */

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.MyViewHolder> implements View.OnClickListener {
    private Context context;
    private List<TopicList> list;
    private OnItemClickListener onItemClickListener;

    public TopicAdapter(List<TopicList> list, Context context) {
        this.list = list;
        this.context = context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.topic_adapter, parent, false);
        view.setOnClickListener(this);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.itemView.setTag(position);
        holder.tv_1.setText(list.get(position).getIndex());
        holder.tv_2.setText(list.get(position).getName());
        if (list.get(position).getLock().equals("lock")) {
            holder.iv.setBackgroundResource(R.drawable.ic_lock_outline_white_24dp);
            holder.tv_3.setVisibility(View.VISIBLE);
            if (list.get(position).getOperation().contains("qq"))
                holder.tv_3.setText("分享QQ群 解锁4-7");
            else if (list.get(position).getOperation().contains("wechat"))
                holder.tv_3.setText("分享朋友圈 解锁8-10");
            else
                holder.tv_3.setText("未定义解锁方式");
        } else {
            holder.iv.setBackgroundResource(R.drawable.ic_keyboard_arrow_right_white_24dp);
            holder.tv_3.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onClick(View v) {
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_1;
        TextView tv_2;
        TextView tv_3;
        ImageView iv;

        public MyViewHolder(View view) {
            super(view);
            tv_1 = (TextView) view.findViewById(R.id.topic_adapter_tv_1);
            tv_2 = (TextView) view.findViewById(R.id.topic_adapter_tv_2);
            tv_3 = (TextView) view.findViewById(R.id.topic_adapter_tv_3);
            iv = (ImageView) view.findViewById(R.id.topic_adapter_iv);
        }
    }
}
