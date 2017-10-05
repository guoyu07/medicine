package com.yangs.medicine.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.yangs.medicine.R;
import com.yangs.medicine.model.BookList;

import java.util.List;

/**
 * Created by yangs on 2017/10/5 0005.
 */

public class BookMeAdapter extends RecyclerView.Adapter<BookMeAdapter.ViewHolder> implements View.OnClickListener {
    private List<BookList> lists;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public BookMeAdapter(List<BookList> lists, Context context) {
        this.lists = lists;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.bookme_adapter, null);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        holder.sdv.setImageURI(lists.get(position).getPicUrl());
        holder.tv.setText(lists.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    @Override
    public void onClick(View v) {
        if (onItemClickListener != null)
            onItemClickListener.onItemClickListener(v, (int) v.getTag());
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClickListener(View v, int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView sdv;
        TextView tv;

        public ViewHolder(View view) {
            super(view);
            sdv = (SimpleDraweeView) view.findViewById(R.id.bookme_adapter_sdv);
            tv = (TextView) view.findViewById(R.id.bookme_adapter_tv);
        }
    }
}
