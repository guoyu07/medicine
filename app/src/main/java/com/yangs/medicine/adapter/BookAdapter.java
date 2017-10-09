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
 * Created by yangs on 2017/10/4 0004.
 */

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> implements View.OnClickListener {
    private List<BookList> lists;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public BookAdapter(Context context, List<BookList> lists) {
        this.context = context;
        this.lists = lists;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.book_adapter, null);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        holder.sdv.setImageURI(lists.get(position).getPicUrl());
        holder.tv_name.setText(lists.get(position).getName());
        holder.tv_time.setText(lists.get(position).getTime());
        holder.tv_summary.setText(lists.get(position).getSummary());
        holder.tv_count.setText(lists.get(position).getCount());
        holder.v.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onClick(View v) {
        if (onItemClickListener != null)
            onItemClickListener.onItemClickListener(v, (int) v.getTag());
    }

    public interface OnItemClickListener {
        public void onItemClickListener(View v, int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView sdv;
        TextView tv_name;
        TextView tv_time;
        TextView tv_summary;
        TextView tv_count;
        View v;

        public ViewHolder(View view) {
            super(view);
            sdv = (SimpleDraweeView) view.findViewById(R.id.book_adapter_sdv);
            tv_name = (TextView) view.findViewById(R.id.book_adapter_name);
            tv_time = (TextView) view.findViewById(R.id.book_adapter_time);
            tv_summary = (TextView) view.findViewById(R.id.book_adapter_summary);
            tv_count = (TextView) view.findViewById(R.id.book_adapter_count);
            v = view.findViewById(R.id.book_adapter_v);
        }
    }
}
