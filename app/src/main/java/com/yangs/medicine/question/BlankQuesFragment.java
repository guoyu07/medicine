package com.yangs.medicine.question;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yangs.medicine.R;
import com.yangs.medicine.adapter.BlankAdapter;
import com.yangs.medicine.fragment.LazyLoadFragment;
import com.yangs.medicine.model.BlankList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangs on 2017/10/10 0010.
 * 填空题Fragment
 */

public class BlankQuesFragment extends LazyLoadFragment implements BlankAdapter.OnItemClickListener {
    private View mLay;
    private RecyclerView recyclerView;
    private BlankAdapter blankAdapter;
    private List<BlankList> lists;

    @Override
    protected int setContentView() {
        return R.layout.blankfrag_layout;
    }

    @Override
    protected void lazyLoad() {
        if (isInit) {
            if (!isLoad) {
                initView();
                initData();
            }
        }

    }

    private void initData() {
        lists.clear();
        BlankList blankList = new BlankList();
        blankList.setClick(false);
        blankList.setIndex(21);
        blankList.setQuestion("健康定义从原来医学的生物学模式转变成$,$,$模式");
        blankList.setAnswer("生物;心理;社会");
        lists.add(blankList);
        blankList = new BlankList();
        blankList.setClick(false);
        blankList.setIndex(22);
        blankList.setQuestion("健康定义从原来医学的生物学模式转变成$模式");
        blankList.setAnswer("生物-心理-社会");
        lists.add(blankList);
        blankList = new BlankList();
        blankList.setClick(false);
        blankList.setIndex(23);
        blankList.setQuestion("健康定义从原来医学的生物学模式转变成$模式");
        blankList.setAnswer("生物-心理-社会");
        lists.add(blankList);
        blankList = new BlankList();
        blankList.setClick(false);
        blankList.setIndex(24);
        blankList.setQuestion("健康定义从原来医学的生物学模式转变成$模式");
        blankList.setAnswer("生物-心理-社会");
        lists.add(blankList);
        blankList = new BlankList();
        blankList.setClick(false);
        blankList.setIndex(25);
        blankList.setQuestion("健康定义从原来医学的生物学模式转变成$模式");
        blankList.setAnswer("生物-心理-社会");
        lists.add(blankList);
        blankList = new BlankList();
        blankList.setClick(false);
        blankList.setIndex(26);
        blankList.setQuestion("健康定义从原来医学的生物学模式转变成$模式");
        blankList.setAnswer("生物-心理-社会");
        lists.add(blankList);
        blankList = new BlankList();
        blankList.setClick(false);
        blankList.setIndex(27);
        blankList.setQuestion("健康定义从原来医学的生物学模式转变成$模式");
        blankList.setAnswer("生物-心理-社会");
        lists.add(blankList);
        blankAdapter.notifyDataSetChanged();
    }

    private void initView() {
        mLay = getContentView();
        recyclerView = (RecyclerView) mLay.findViewById(R.id.blankfrag_rv);
        lists = new ArrayList<>();
        blankAdapter = new BlankAdapter(lists, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(blankAdapter);
        blankAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(View v, int position) {
        lists.get(position).setClick(true);
        blankAdapter.notifyDataSetChanged();
    }
}
