package com.yangs.medicine.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        holder.v.setVisibility(View.VISIBLE);
        String src = lists.get(position).getQuestion().replace("$",
                "&nbsp;&nbsp;(&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;)&nbsp;&nbsp;");
        if (lists.get(position).getClick()) {
            String[] answer = lists.get(position).getAnswer().split(";");
            for (String s : answer) {
                src = src.replaceFirst("\\(&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\\)",
                        "(<font color=\'#ea6662\'>" + s + "</font>)");
            }
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
            onItemClickListener.onItemClick(v, (int) v.getTag());
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View v, int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv;
        View v;

        ViewHolder(View view) {
            super(view);
            tv = (TextView) view.findViewById(R.id.blank_adapter_ques);
            v = view.findViewById(R.id.blank_adapter_v);
        }
    }
}
