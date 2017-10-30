package com.yangs.medicine.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.yangs.medicine.R;
import com.yangs.medicine.model.DiscussList;

import java.util.List;

/**
 * Created by yangs on 2017/10/29 0029.
 */

public class DiscussAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<DiscussList> lists;
    private Context context;
    private OnThumbUpListener onThumbUpListener;

    public DiscussAdapter(List<DiscussList> lists, Context context) {
        this.lists = lists;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View view = LayoutInflater.from(context).inflate(R.layout.discuss_adapter, null);
            return new ViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.discuss_adapter_header, null);
            return new HeaderHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        String type = lists.get(position).getType();
        if (type.equals(""))
            return 0;
        else
            return 1;
    }

    public void addAll(List<DiscussList> list) {
        int lastIndex = this.lists.size();
        if (this.lists.addAll(list)) {
            notifyItemRangeInserted(lastIndex, this.lists.size());
        }
    }

    public void clear() {
        lists.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder holder1 = (ViewHolder) holder;
            holder1.sdv.setImageURI(lists.get(position).getImgUrl());
            holder1.tv_user.setText(lists.get(position).getUser());
            holder1.tv_time.setText(lists.get(position).getTime());
            holder1.tv_star.setText(lists.get(position).getStar() + "");
            holder1.tv_content.setText(lists.get(position).getContent());
            holder1.iv_thumb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onThumbUpListener != null)
                        onThumbUpListener.onThumbUp(holder.getAdapterPosition());
                }
            });
            if (lists.get(position).getIsYouStar().equals("0")) {
                holder1.iv_thumb.setBackgroundResource(R.drawable.ic_thumb_up);
                holder1.tv_star.setTextColor(ContextCompat.getColor(context, R.color.error_tv));
            } else {
                holder1.iv_thumb.setBackgroundResource(R.drawable.ic_thumb_up_star);
                holder1.tv_star.setTextColor(ContextCompat.getColor(context, R.color.blue2));
            }
            if (position != lists.size() - 1)
                holder1.v.setVisibility(View.VISIBLE);
            else
                holder1.v.setVisibility(View.INVISIBLE);
        } else {
            HeaderHolder holder1 = (HeaderHolder) holder;
            holder1.header_tv.setText("当前共有 " + lists.get(position).getStar() + " 条评论");
        }
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public void setOnThumbUpListener(OnThumbUpListener onThumbUpListener) {
        this.onThumbUpListener = onThumbUpListener;
    }

    class HeaderHolder extends RecyclerView.ViewHolder {
        TextView header_tv;

        HeaderHolder(View view) {
            super(view);
            header_tv = (TextView) view.findViewById(R.id.discuss_adapter_header_tv);
        }
    }

    public interface OnThumbUpListener {
        public void onThumbUp(int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView sdv;
        TextView tv_user;
        TextView tv_time;
        ImageView iv_thumb;
        TextView tv_star;
        TextView tv_content;
        View v;

        ViewHolder(View view) {
            super(view);
            sdv = (SimpleDraweeView) view.findViewById(R.id.discuss_adapter_sdv);
            tv_user = (TextView) view.findViewById(R.id.discuss_adapter_tv_user);
            tv_time = (TextView) view.findViewById(R.id.discuss_adapter_tv_time);
            iv_thumb = (ImageView) view.findViewById(R.id.discuss_adapter_iv_thumb);
            tv_star = (TextView) view.findViewById(R.id.discuss_adapter_tv_star);
            tv_content = (TextView) view.findViewById(R.id.discuss_adapter_tv_content);
            v = view.findViewById(R.id.discuss_adapter_v);
        }
    }
}
