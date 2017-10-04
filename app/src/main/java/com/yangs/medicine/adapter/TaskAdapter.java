package com.yangs.medicine.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.yangs.medicine.R;
import com.yangs.medicine.fragment.LazyLoadFragment;
import com.yangs.medicine.model.TaskList;

import java.util.List;

/**
 * Created by yangs on 2017/10/4 0004.
 */

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> implements View.OnClickListener {
    private Context context;
    private List<TaskList> lists;
    private OnItemClickListener onItemClickListener;

    public TaskAdapter(Context context, List<TaskList> lists) {
        this.context = context;
        this.lists = lists;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.task_adapter, null);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        holder.sdv.setImageURI(lists.get(position).getPicUrl());
        holder.name.setText(lists.get(position).getName());
        holder.time.setText(lists.get(position).getTime());
        holder.content.setText(lists.get(position).getContent());
        holder.money.setText(lists.get(position).getMoney());
        if (lists.get(position).getFinish()) {
            holder.ok.setText("已完成");
        } else {
            holder.ok.setText("我要接受");
        }
    }

    @Override
    public int getItemCount() {
        return lists.size();
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

    class ViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView sdv;
        TextView name;
        TextView time;
        TextView content;
        TextView money;
        TextView ok;

        public ViewHolder(View view) {
            super(view);
            sdv = (SimpleDraweeView) view.findViewById(R.id.task_adapter_sdv);
            name = (TextView) view.findViewById(R.id.task_adapter_name);
            time = (TextView) view.findViewById(R.id.task_adapter_time);
            content = (TextView) view.findViewById(R.id.task_adapter_content);
            money = (TextView) view.findViewById(R.id.task_adapter_money);
            ok = (TextView) view.findViewById(R.id.task_adapter_ok);
        }
    }
}
