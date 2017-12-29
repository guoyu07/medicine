package com.yangs.medicine.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yangs.medicine.R;
import com.yangs.medicine.activity.APPlication;
import com.yangs.medicine.model.BlankList;

import java.util.List;

/**
 * Created by yangs on 2017/10/10 0010.
 */

public class BlankAdapter extends RecyclerView.Adapter<BlankAdapter.ViewHolder> implements View.OnClickListener {
    private List<BlankList> lists;
    private Context context;
    private OnItemClickListener onItemClickListener;
    private OnBtItemClickListener onBtItemClickListener;
    private OnBtErrorClickListener onBtErrorClickListener;

    public BlankAdapter(List<BlankList> lists, Context context) {
        this.lists = lists;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.blank_adapter, null);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.itemView.setTag(position);
        holder.v.setVisibility(View.VISIBLE);
        String src = lists.get(position).getQuestion().replace("$",
                "&nbsp;&nbsp;(&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;)&nbsp;&nbsp;");
        if (lists.get(position).getClick()) {
            holder.bt.setVisibility(View.VISIBLE);
            holder.bt_error.setVisibility(View.VISIBLE);
            String[] answer = lists.get(position).getAnswer().split(";");
            for (String s : answer) {
                src = src.replaceFirst("\\(&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\\)",
                        "(<font color=\'#ea6662\'>" + s + "</font>)");
            }
            if (lists.get(position).getAddError()) {
                holder.bt.setBackgroundResource(R.drawable.choose_bt_lay_red);
                holder.bt.setTextColor(ContextCompat.getColor(context, R.color.white));
                holder.bt.setText("已加入错题集");
            } else {
                holder.bt.setBackgroundResource(R.drawable.choose_bt_lay);
                holder.bt.setTextColor(ContextCompat.getColor(context, R.color.error_tv));
                holder.bt.setText("加入错题集");
            }
            holder.bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onBtItemClickListener != null) {
                        onBtItemClickListener.onBtItemClick(position);
                    }
                }
            });
            holder.bt_error.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onBtErrorClickListener != null)
                        onBtErrorClickListener.onBtError(position);
                }
            });
        } else {
            holder.bt.setVisibility(View.GONE);
            holder.bt_error.setVisibility(View.GONE);
        }
        holder.tv.setText(Html.fromHtml(lists.get(position).getIndex() + ". " + src));
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    @Override
    public void onClick(View v) {
        if (onItemClickListener != null)
            onItemClickListener.onItemClick((int) v.getTag());
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnBtItemClickListener(OnBtItemClickListener onBtItemClickListener) {
        this.onBtItemClickListener = onBtItemClickListener;
    }

    public void setOnBtErrorClickListener(OnBtErrorClickListener onBtErrorClickListener) {
        this.onBtErrorClickListener = onBtErrorClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClick(int position);
    }

    public interface OnBtItemClickListener {
        void onBtItemClick(int position);
    }

    public interface OnBtErrorClickListener {
        void onBtError(int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv;
        Button bt;
        Button bt_error;
        View v;

        ViewHolder(View view) {
            super(view);
            tv = (TextView) view.findViewById(R.id.blank_adapter_ques);
            v = view.findViewById(R.id.blank_adapter_v);
            bt = (Button) view.findViewById(R.id.blank_adapter_bt);
            bt_error = (Button) view.findViewById(R.id.blank_adapter_bt_error);
        }
    }
}
