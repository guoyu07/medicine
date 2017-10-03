package com.yangs.medicine.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yangs.medicine.R;
import com.yangs.medicine.model.ErrorTodayList;

import java.util.List;

/**
 * Created by yangs on 2017/10/3 0003.
 */

public class ErrorTodayAdapter extends RecyclerView.Adapter<ErrorTodayAdapter.ViewHolder> {
    private List<ErrorTodayList> lists;
    private Context context;

    public ErrorTodayAdapter(List<ErrorTodayList> lists, Context context) {
        this.lists = lists;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.)
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View view) {
            super(view);
        }
    }
}
