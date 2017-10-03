package com.yangs.medicine.fragment;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yangs.medicine.R;
import com.yangs.medicine.adapter.ErrorTodayAdapter;
import com.yangs.medicine.model.ErrorTodayList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangs on 2017/10/3 0003.
 */

public class ErrorTodayFragment extends LazyLoadFragment implements ErrorTodayAdapter.OnItemClickListener {
    private View mLay;
    private RecyclerView mrecyclerView;
    private List<ErrorTodayList> list;
    private List<ErrorTodayList> tmpList;
    private ErrorTodayAdapter errorTodayAdapter;

    @Override
    protected int setContentView() {
        return R.layout.errortodayfrag_layout;
    }

    @Override
    protected void lazyLoad() {
        if (isInit) {
            if (!isLoad) {
                initData();
                mLay = getContentView();
                mrecyclerView = (RecyclerView) mLay.findViewById(R.id.errortoday_frag_rv);
                errorTodayAdapter = new ErrorTodayAdapter(list, getActivity());
                errorTodayAdapter.setOnItemClickListener(this);
                mrecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                mrecyclerView.setAdapter(errorTodayAdapter);
                mrecyclerView.addItemDecoration(new DividerItemDecoration(
                        getActivity(), DividerItemDecoration.VERTICAL));
            }
        }
    }

    private void initData() {
        list = new ArrayList<>();
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
            list.removeAll(tmpList);
        } else {
            list.get(position).setClick(true);
            tmpList = new ArrayList<>();
            ErrorTodayList errorTodayList = new ErrorTodayList();
            errorTodayList.setName("休克时的补液原则是()");
            errorTodayList.setType("small");
            tmpList.add(errorTodayList);
            errorTodayList = new ErrorTodayList();
            errorTodayList.setName("DNA双螺旋结构的维持主要靠核苷酸间的磷酸");
            errorTodayList.setType("small");
            tmpList.add(errorTodayList);
            list.addAll(position + 1, tmpList);
        }
        errorTodayAdapter.notifyDataSetChanged();
    }
}
