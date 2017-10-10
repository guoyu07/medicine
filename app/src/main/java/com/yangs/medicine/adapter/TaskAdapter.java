package com.yangs.medicine.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.yangs.medicine.R;
import com.yangs.medicine.activity.APPlication;
import com.yangs.medicine.model.TaskList;

import java.util.List;

/**
 * Created by yangs on 2017/10/4 0004.
 */

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> implements View.OnClickListener {
    private Context context;
    private List<TaskList> lists;
    private OnItemClickListener onItemClickListener;
    private String type;

    public TaskAdapter(Context context, List<TaskList> lists, String type) {
        this.context = context;
        this.lists = lists;
        this.type = type;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.task_adapter, null);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.itemView.setTag(position);
        holder.sdv.setImageURI(lists.get(position).getPicUrl());
        holder.name.setText(lists.get(position).getName());
        holder.time.setText(lists.get(position).getTime());
        holder.content.setText(lists.get(position).getContent());
        holder.money.setText(lists.get(position).getMoney());
        holder.v.setVisibility(View.VISIBLE);
        if ("我发布的".equals(type)) {
            if (lists.get(position).getFinish()) {
                holder.ok.setText("已被接受");
                holder.ok.setBackgroundResource(R.drawable.task_selector_blue);
                holder.ok.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
            } else {
                holder.ok.setText("未被接受");
                holder.ok.setBackgroundResource(R.drawable.task_selector_red);
                holder.ok.setTextColor(ContextCompat.getColor(context, R.color.red));
            }
        } else if ("我接受的".equals(type)) {
            if (lists.get(position).getFinish()) {
                holder.ok.setText("已完成");
                holder.ok.setBackgroundResource(R.drawable.task_selector_blue);
                holder.ok.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
            } else {
                holder.ok.setText("未完成");
                holder.ok.setBackgroundResource(R.drawable.task_selector_red);
                holder.ok.setTextColor(ContextCompat.getColor(context, R.color.red));
            }
        } else {
            if (lists.get(position).getFinish()) {
                holder.ok.setText("已完成");
            } else {
                holder.ok.setText("我要接受");
            }
        }
        holder.ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                APPlication.showToast(lists.get(position).getName(), 0);
            }
        });
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
        View v;

        public ViewHolder(View view) {
            super(view);
            sdv = (SimpleDraweeView) view.findViewById(R.id.task_adapter_sdv);
            name = (TextView) view.findViewById(R.id.task_adapter_name);
            time = (TextView) view.findViewById(R.id.task_adapter_time);
            content = (TextView) view.findViewById(R.id.task_adapter_content);
            money = (TextView) view.findViewById(R.id.task_adapter_money);
            ok = (TextView) view.findViewById(R.id.task_adapter_ok);
            v = view.findViewById(R.id.task_adapter_v);
        }
    }
}
