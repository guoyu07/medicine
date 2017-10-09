package com.yangs.medicine.fragment;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.yangs.medicine.R;
import com.yangs.medicine.activity.APPlication;
import com.yangs.medicine.adapter.ErrorTodayAdapter;
import com.yangs.medicine.model.ErrorTodayList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yangs on 2017/10/3 0003.
 */

public class ErrorTodayFragment extends LazyLoadFragment implements ErrorTodayAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    private View mLay;
    private RecyclerView mrecyclerView;
    private TextView empty_tv;
    private List<ErrorTodayList> list;
    private Map<String, List<ErrorTodayList>> map;
    private ErrorTodayAdapter errorTodayAdapter;
    private SwipeRefreshLayout srl;
    private Handler mHandler;
    private int i = 0;
    private String type;

    @Override
    protected int setContentView() {
        return R.layout.errortodayfrag_layout;
    }

    @Override
    protected void lazyLoad() {
        if (isInit) {
            if (!isLoad) {
                list = new ArrayList<>();
                map = new HashMap<>();
                initHandler();
                mLay = getContentView();
                mrecyclerView = (RecyclerView) mLay.findViewById(R.id.errortoday_rv);
                empty_tv = (TextView) mLay.findViewById(R.id.errortoday_tv);
                srl = (SwipeRefreshLayout) mLay.findViewById(R.id.errortoday_srl);
                srl.setColorSchemeColors(Color.CYAN, Color.GREEN, ContextCompat.getColor(getContext(),
                        R.color.colorPrimary));
                errorTodayAdapter = new ErrorTodayAdapter(list, getActivity(), type);
                errorTodayAdapter.setOnItemClickListener(ErrorTodayFragment.this);
                mrecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                mrecyclerView.setAdapter(errorTodayAdapter);
//                mrecyclerView.addItemDecoration(new DividerItemDecoration(
//                        getActivity(), DividerItemDecoration.VERTICAL));
                srl.setOnRefreshListener(this);
                empty_tv.setVisibility(View.GONE);
                srl.post(new Runnable() {
                    @Override
                    public void run() {
                        srl.setRefreshing(true);
                        onRefresh();
                    }
                });
            }
        }
    }

    public void setType(String type) {
        this.type = type;
    }

    private void initHandler() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        break;
                }
            }
        };
    }

    private void initData() {
        list.clear();
        map.clear();
        ErrorTodayList errorTodayList = new ErrorTodayList();
        errorTodayList.setName("生理学");
        errorTodayList.setClick(false);
        errorTodayList.setCount(2);
        errorTodayList.setType("big");
        list.add(errorTodayList);
        errorTodayList = new ErrorTodayList();
        errorTodayList.setName("病理学");
        errorTodayList.setClick(false);
        errorTodayList.setCount(3);
        errorTodayList.setType("big");
        list.add(errorTodayList);
        errorTodayList = new ErrorTodayList();
        errorTodayList.setName("生物化学");
        errorTodayList.setClick(false);
        errorTodayList.setCount(2);
        errorTodayList.setType("big");
        list.add(errorTodayList);
    }

    @Override
    public void onItemClickListener(View view, int position) {
        if (list.get(position).getType().equals("small"))
            return;
        if (list.get(position).getClick()) {
            list.get(position).setClick(false);
            list.removeAll(map.get(list.get(position).getName()));
        } else {
            list.get(position).setClick(true);
            List<ErrorTodayList> tmpList = new ArrayList<>();
            ErrorTodayList errorTodayList = new ErrorTodayList();
            errorTodayList.setName(position + " 休克时的补液原则是()");
            errorTodayList.setType("small");
            tmpList.add(errorTodayList);
            errorTodayList = new ErrorTodayList();
            errorTodayList.setName("DNA 双螺旋结构的维持主要靠核苷酸间的磷酸二酯键");
            errorTodayList.setType("small");
            tmpList.add(errorTodayList);
            map.put(list.get(position).getName(), tmpList);
            list.addAll(position + 1, map.get(list.get(position).getName()));
        }
        errorTodayAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                srl.setRefreshing(false);
                if (i == 2) {
                    empty_tv.setVisibility(View.VISIBLE);
                    mrecyclerView.setVisibility(View.GONE);
                    i = 0;
                } else if (i == 0) {
                    i = 2;
                    empty_tv.setVisibility(View.GONE);
                    mrecyclerView.setVisibility(View.VISIBLE);
                    initData();
                    errorTodayAdapter.notifyDataSetChanged();
                }
            }
        }, 2000);
    }
}
