package com.yangs.medicine.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.yangs.medicine.R;
import com.yangs.medicine.model.ExplainList;

import java.util.List;

/**
 * Created by yangs on 2017/10/10 0010.
 */

public class AskAdapter extends RecyclerView.Adapter<AskAdapter.ViewHolder> implements View.OnClickListener {
    private List<ExplainList> lists;
    private Context context;
    private OnItemClickListener onItemClickListener;
    private OnBtItemClickListener onBtItemClickListener;

    public AskAdapter(List<ExplainList> lists, Context context) {
        this.lists = lists;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.ask_adapter, null);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.itemView.setTag(position);
        holder.v.setVisibility(View.VISIBLE);
        holder.ques.setText(lists.get(position).getIndex() + "." + lists.get(position).getName());
        if (lists.get(position).getClick()) {
            holder.ans.setVisibility(View.VISIBLE);
            holder.ans.setText("答:  " + lists.get(position).getExplain());
            holder.bt.setVisibility(View.VISIBLE);
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
        } else {
            holder.bt.setVisibility(View.GONE);
            holder.ans.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    @Override
    public void onClick(View v) {
        if (onItemClickListener != null)
            onItemClickListener.onItemClick(v, (int) v.getTag());
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnBtItemClickListener(OnBtItemClickListener onBtItemClickListener) {
        this.onBtItemClickListener = onBtItemClickListener;
    }


    public interface OnItemClickListener {
        public void onItemClick(View v, int position);
    }

    public interface OnBtItemClickListener {
        void onBtItemClick(int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView ques;
        TextView ans;
        Button bt;
        View v;

        ViewHolder(View view) {
            super(view);
            ques = (TextView) view.findViewById(R.id.ask_adapter_ques);
            ans = (TextView) view.findViewById(R.id.ask_adapter_ans);
            bt = (Button) view.findViewById(R.id.ask_adapter_bt);
            v = view.findViewById(R.id.blank_adapter_v);
        }
    }
}
