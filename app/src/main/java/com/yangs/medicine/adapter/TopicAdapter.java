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
        if (list.get(position).getType().equals("big")) {
            if (position != list.size() - 1 && list.get(position + 1).getType().equals("small")) {
                holder.v1.setVisibility(View.GONE);
                holder.v2.setVisibility(View.VISIBLE);
            } else {
                holder.v1.setVisibility(View.VISIBLE);
            }
            holder.v2.setVisibility(View.GONE);
            holder.ada_1_tv_name.setVisibility(View.VISIBLE);
            holder.ada_1_iv.setVisibility(View.VISIBLE);
            holder.ada_2_tv_name.setVisibility(View.GONE);
            holder.ada_2_v.setVisibility(View.GONE);
            holder.ada_2_tv_number.setVisibility(View.GONE);
            holder.ada_1_tv_name.setText(list.get(position).getIndex() + "   " + list.get(position).getName());
            if (list.get(position).getLock().equals("lock")) {
                holder.ada_1_tv_share.setVisibility(View.VISIBLE);
                holder.ada_1_iv.setBackgroundResource(R.drawable.icon_suo);
                if (list.get(position).getOperation().contains("qq"))
                    holder.ada_1_tv_share.setText("分享QQ群 解锁4-7");
                else if (list.get(position).getOperation().contains("wechat"))
                    holder.ada_1_tv_share.setText("分享朋友圈 解锁8-10");
                else
                    holder.ada_1_tv_share.setText("未定义解锁方式");
            } else {
                holder.ada_1_tv_share.setVisibility(View.INVISIBLE);
                if (list.get(position).getClick())
                    holder.ada_1_iv.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_gray_24dp);
                else
                    holder.ada_1_iv.setBackgroundResource(R.drawable.ic_keyboard_arrow_right_white_24dp);
            }
        } else {
            if (position != list.size() - 1 && list.get(position + 1).getType().equals("big")) {
                holder.v1.setVisibility(View.VISIBLE);
                holder.v2.setVisibility(View.GONE);
            } else {
                holder.v1.setVisibility(View.GONE);
                holder.v2.setVisibility(View.VISIBLE);
            }
            holder.ada_1_tv_name.setVisibility(View.GONE);
            holder.ada_1_iv.setVisibility(View.GONE);
            holder.ada_1_tv_share.setVisibility(View.GONE);
            holder.ada_2_tv_name.setVisibility(View.VISIBLE);
            holder.ada_2_v.setVisibility(View.VISIBLE);
            holder.ada_2_tv_number.setVisibility(View.VISIBLE);
            holder.ada_2_tv_name.setText(list.get(position).getIndex() + " " + list.get(position).getName());
            holder.ada_2_tv_number.setText(list.get(position).getNumber());
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
        TextView ada_1_tv_name;
        TextView ada_1_tv_share;
        ImageView ada_1_iv;
        TextView ada_2_tv_name;
        View ada_2_v;
        TextView ada_2_tv_number;
        View v1;
        View v2;

        public MyViewHolder(View view) {
            super(view);
            ada_1_tv_name = (TextView) view.findViewById(R.id.topic_adapter_1_tv_name);
            ada_1_tv_share = (TextView) view.findViewById(R.id.topic_adapter_1_tv_share);
            ada_1_iv = (ImageView) view.findViewById(R.id.topic_adapter_1_iv);
            ada_2_tv_name = (TextView) view.findViewById(R.id.topic_adapter_2_tv_name);
            ada_2_v = view.findViewById(R.id.topic_adapter_2_v);
            ada_2_tv_number = (TextView) view.findViewById(R.id.topic_adapter_2_tv_number);
            v1 = view.findViewById(R.id.topic_adapter_v1);
            v2 = view.findViewById(R.id.topic_adapter_v2);
        }
    }
}
