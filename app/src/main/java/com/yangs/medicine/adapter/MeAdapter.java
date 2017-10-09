package com.yangs.medicine.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yangs.medicine.R;
import com.yangs.medicine.model.MeList;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by yangs on 2017/9/26 0026.
 */

public class MeAdapter extends RecyclerView.Adapter<MeAdapter.MyViewHolder> implements View.OnClickListener {
    private Context context;
    private List<MeList> list;
    private OnItemClickListener onItemClickListener;

    public MeAdapter(Context context, List<MeList> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.me_adapter, null);
        view.setOnClickListener(this);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.itemView.setTag(position);
        holder.iv.setBackgroundResource(list.get(position).getIcon());
        holder.tv.setText(list.get(position).getName());
        if (position == (list.size() - 1))
            holder.v.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return list.size();
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
        void onItemClick(View view, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView iv;
        TextView tv;
        View v;

        public MyViewHolder(View view) {
            super(view);
            iv = (ImageView) view.findViewById(R.id.me_adapter_iv);
            tv = (TextView) view.findViewById(R.id.me_adapter_tv);
            v = view.findViewById(R.id.me_adapter_v);
        }
    }
}
