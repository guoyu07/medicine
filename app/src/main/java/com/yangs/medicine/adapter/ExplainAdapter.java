package com.yangs.medicine.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yangs.medicine.R;
import com.yangs.medicine.model.BlankList;
import com.yangs.medicine.model.ExplainList;

import java.util.List;

/**
 * Created by yangs on 2017/10/10 0010.
 */

public class ExplainAdapter extends RecyclerView.Adapter<ExplainAdapter.ViewHolder> implements View.OnClickListener {
    private List<ExplainList> lists;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public ExplainAdapter(List<ExplainList> lists, Context context) {
        this.lists = lists;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.explain_adapter, null);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        holder.v.setVisibility(View.VISIBLE);
        String src = lists.get(position).getIndex() + "." + lists.get(position).getName() +
                ":&nbsp;";
        if (lists.get(position).getClick()) {
            src = src + "&nbsp;&nbsp;<font><small>" + lists.get(position).getExplain() + "</small></font>";
        }
        holder.tv.setText(Html.fromHtml(src));
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
            tv = (TextView) view.findViewById(R.id.explain_adapter_ques);
            v = view.findViewById(R.id.explain_adapter_v);
        }
    }
}
