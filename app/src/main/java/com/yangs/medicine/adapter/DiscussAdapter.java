package com.yangs.medicine.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.yangs.medicine.R;
import com.yangs.medicine.model.DiscussList;

import java.util.List;

/**
 * Created by yangs on 2017/10/29 0029.
 */

public class DiscussAdapter extends RecyclerView.Adapter<DiscussAdapter.ViewHolder> {
    private List<DiscussList> lists;
    private Context context;

    public DiscussAdapter(List<DiscussList> lists, Context context) {
        this.lists = lists;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.discuss_adapter, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.sdv.setImageURI(lists.get(position).getImgUrl());
        holder.tv_user.setText(lists.get(position).getUser());
        holder.tv_time.setText(lists.get(position).getTime());
        holder.tv_star.setText(lists.get(position).getStar() + "");
        holder.tv_content.setText(lists.get(position).getContent());
        holder.v.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView sdv;
        TextView tv_user;
        TextView tv_time;
        ImageView iv_thumb;
        TextView tv_star;
        TextView tv_content;
        View v;

        ViewHolder(View view) {
            super(view);
            sdv = (SimpleDraweeView) view.findViewById(R.id.discuss_adapter_sdv);
            tv_user = (TextView) view.findViewById(R.id.discuss_adapter_tv_user);
            tv_time = (TextView) view.findViewById(R.id.discuss_adapter_tv_time);
            iv_thumb = (ImageView) view.findViewById(R.id.discuss_adapter_iv_thumb);
            tv_star = (TextView) view.findViewById(R.id.discuss_adapter_tv_star);
            tv_content = (TextView) view.findViewById(R.id.discuss_adapter_tv_content);
            v = view.findViewById(R.id.discuss_adapter_v);
        }
    }
}
