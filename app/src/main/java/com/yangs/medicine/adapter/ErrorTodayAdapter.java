package com.yangs.medicine.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yangs.medicine.R;
import com.yangs.medicine.model.ErrorTodayList;

import java.util.List;

/**
 * Created by yangs on 2017/10/3 0003.
 */

public class ErrorTodayAdapter extends RecyclerView.Adapter<ErrorTodayAdapter.ViewHolder> implements View.OnClickListener {
    private List<ErrorTodayList> lists;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public ErrorTodayAdapter(List<ErrorTodayList> lists, Context context) {
        this.lists = lists;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.errortoday_adapter, null);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        ErrorTodayList errorTodayList = lists.get(position);
        if (errorTodayList.getType().equals("big")) {
            holder.tv_2.setVisibility(View.GONE);
            holder.tv_1.setVisibility(View.VISIBLE);
            holder.tv_3.setVisibility(View.VISIBLE);
            holder.v.setVisibility(View.VISIBLE);
            holder.iv.setVisibility(View.VISIBLE);
            holder.tv_1.setText(errorTodayList.getName());
            holder.tv_3.setText(errorTodayList.getCount() + "");
            if (errorTodayList.getClick())
                holder.iv.setImageResource(R.drawable.ic_keyboard_arrow_down_gray_24dp);
            else
                holder.iv.setImageResource(R.drawable.ic_keyboard_arrow_right_white_24dp);
        } else if (errorTodayList.getType().equals("small")) {
            holder.tv_2.setVisibility(View.VISIBLE);
            holder.tv_1.setVisibility(View.GONE);
            holder.tv_3.setVisibility(View.GONE);
            holder.v.setVisibility(View.GONE);
            holder.iv.setVisibility(View.GONE);
            holder.tv_2.setText(errorTodayList.getName());
        }
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
        public void onItemClickListener(View view, int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_1;
        TextView tv_2;
        TextView tv_3;
        View v;
        ImageView iv;

        public ViewHolder(View view) {
            super(view);
            tv_1 = (TextView) view.findViewById(R.id.errortoday_adapter_tv_1);
            tv_2 = (TextView) view.findViewById(R.id.errortoday_adapter_tv_2);
            tv_3 = (TextView) view.findViewById(R.id.errortoday_adapter_tv_3);
            v = view.findViewById(R.id.errortoday_adapter_v);
            iv = (ImageView) view.findViewById(R.id.errortoday_adapter_iv);
        }
    }
}
