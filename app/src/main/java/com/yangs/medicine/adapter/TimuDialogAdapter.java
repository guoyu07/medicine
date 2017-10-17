package com.yangs.medicine.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yangs.medicine.R;
import com.yangs.medicine.activity.APPlication;
import com.yangs.medicine.model.TimuList;
import com.yangs.medicine.model.TimuDialogList;

import java.util.List;

/**
 * Created by yangs on 2017/10/12 0012.
 */

public class TimuDialogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<TimuDialogList> timuList;
    private Context context;

    public TimuDialogAdapter(List<TimuDialogList> timuList, Context context) {
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
            ViewHolder v = (ViewHolder) holder;
            List<TimuList> list = timuList.get(position).getLists();
            for (int i = 0; i < list.size(); i++) {
                switch (i) {
                    case 0:
                        setTimu(v.v1, list.get(i));
                        break;
                    case 1:
                        setTimu(v.v2, list.get(i));
                        break;
                    case 2:
                        setTimu(v.v3, list.get(i));
                        break;
                    case 3:
                        setTimu(v.v4, list.get(i));
                        break;
                    case 4:
                        setTimu(v.v5, list.get(i));
                        break;
                    case 5:
                        setTimu(v.v6, list.get(i));
                        break;
                    case 6:
                        setTimu(v.v7, list.get(i));
                        break;
                    case 7:
                        setTimu(v.v8, list.get(i));
                        break;
                }
            }
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

    public void setTimu(TextView tv, final TimuList tdl) {
        tv.setVisibility(View.VISIBLE);
        tv.setText(tdl.getIndex() + "");
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                APPlication.showToast(tdl.getIndex() + "", 0);
            }
        });
        switch (tdl.getStatus()) {
            case "未做":
                tv.setBackgroundResource(R.drawable.timudialog_adapter_lay_gray);
                tv.setTextColor(ContextCompat.getColor(context, R.color.error_tv));
                break;
            case "对":
                tv.setBackgroundResource(R.drawable.timudialog_adapter_lay_blue);
                tv.setTextColor(ContextCompat.getColor(context, R.color.me_white));
                break;
            case "错":
                tv.setBackgroundResource(R.drawable.timudialog_adapter_lay_red);
                tv.setTextColor(ContextCompat.getColor(context, R.color.me_white));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return timuList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView v1;
        TextView v2;
        TextView v3;
        TextView v4;
        TextView v5;
        TextView v6;
        TextView v7;
        TextView v8;

        ViewHolder(View view) {
            super(view);
            v1 = (TextView) view.findViewById(R.id.timudialog_adapter_v1);
            v2 = (TextView) view.findViewById(R.id.timudialog_adapter_v2);
            v3 = (TextView) view.findViewById(R.id.timudialog_adapter_v3);
            v4 = (TextView) view.findViewById(R.id.timudialog_adapter_v4);
            v5 = (TextView) view.findViewById(R.id.timudialog_adapter_v5);
            v6 = (TextView) view.findViewById(R.id.timudialog_adapter_v6);
            v7 = (TextView) view.findViewById(R.id.timudialog_adapter_v7);
            v8 = (TextView) view.findViewById(R.id.timudialog_adapter_v8);
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
