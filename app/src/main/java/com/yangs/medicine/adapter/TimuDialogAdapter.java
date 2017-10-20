package com.yangs.medicine.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yangs.medicine.R;
import com.yangs.medicine.model.TimuList;

import java.util.List;

/**
 * Created by yangs on 2017/10/12 0012.
 */

public class TimuDialogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<TimuList> timuList;
    private Context context;
    private TimuOnClickListener timuOnClickListener;

    public TimuDialogAdapter(List<TimuList> timuList, Context context) {
        this.timuList = timuList;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if ("".equals(timuList.get(position).getType())) {
            return 0;           //题
        } else {
            if (timuList.get(position).getType().contains("章"))
                return 1;       //章节
            else
                return 2;       //题型
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View view = LayoutInflater.from(context).inflate(R.layout.timudialog_adapter, parent, false);
            return new ViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.timudialog_adapter_head, parent, false);
            return new HeadViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            final TimuList timu = timuList.get(position);
            final ViewHolder holder1 = (ViewHolder) holder;
            holder1.tv.setText(timu.getIndex() + "");
            switch (timu.getStatus()) {
                case "未做":
                    holder1.tv.setBackgroundResource(R.drawable.timudialog_adapter_lay_gray);
                    holder1.tv.setTextColor(ContextCompat.getColor(context, R.color.error_tv));
                    break;
                case "对":
                    holder1.tv.setBackgroundResource(R.drawable.timudialog_adapter_lay_blue);
                    holder1.tv.setTextColor(ContextCompat.getColor(context, R.color.me_white));
                    break;
                case "错":
                    holder1.tv.setBackgroundResource(R.drawable.timudialog_adapter_lay_red);
                    holder1.tv.setTextColor(ContextCompat.getColor(context, R.color.me_white));
                    break;
            }
            holder1.tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    timuOnClickListener.timuOnClick(timu.getIndex());
                }
            });
        } else if (holder instanceof HeadViewHolder) {
            HeadViewHolder holder1 = (HeadViewHolder) holder;
            String s = timuList.get(position).getType();
            if (s.contains("章")) {
                holder1.head_tv_left.setVisibility(View.VISIBLE);
                holder1.head_tv_left.setText(s);
            } else {
                holder1.head_tv_center.setVisibility(View.VISIBLE);
                holder1.head_tv_center.setText(s);
            }
        }
    }

    public void setTimuOnClickListener(TimuOnClickListener timuOnClickListener) {
        this.timuOnClickListener = timuOnClickListener;
    }

    public interface TimuOnClickListener {
        void timuOnClick(int index);
    }

    @Override
    public int getItemCount() {
        return timuList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv;

        ViewHolder(View view) {
            super(view);
            tv = (TextView) view.findViewById(R.id.timudialog_adapter_tv);
        }
    }

    class HeadViewHolder extends RecyclerView.ViewHolder {
        TextView head_tv_left;
        TextView head_tv_center;

        HeadViewHolder(View view) {
            super(view);
            head_tv_left = (TextView) view.findViewById(R.id.timudialog_adapter_head_left);
            head_tv_center = (TextView) view.findViewById(R.id.timudialog_adapter_head_center);
        }
    }
}
