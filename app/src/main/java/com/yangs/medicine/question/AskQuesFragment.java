package com.yangs.medicine.question;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yangs.medicine.R;
import com.yangs.medicine.adapter.AskAdapter;
import com.yangs.medicine.fragment.LazyLoadFragment;
import com.yangs.medicine.model.ExplainList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangs on 2017/10/10 0010.
 * 问答题Fragment
 */

public class AskQuesFragment extends Fragment implements AskAdapter.OnItemClickListener {
    private View mLay;
    private RecyclerView recyclerView;
    private AskAdapter askAdapter;
    private List<ExplainList> lists;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mLay = inflater.inflate(R.layout.askfrag_layout, container, false);
        initView();
        initData();
        return mLay;
    }

    private void initData() {
        lists.clear();
        if (getArguments() == null)
            return;
        int index = (int) getArguments().getSerializable("index");
        ExplainList explainList = new ExplainList();
        explainList.setIndex(index + 1);
        explainList.setName("磺胺药与甲氧苄啶合用为什么能增强疗效");
        explainList.setExplain("磺胺药作用于二氢叶酸合成酶,干扰合成叶酸的第一步,甲氧苄啶作用于叶酸合成代谢的第二步,选择性抑制二氢叶酸还原酶的作用,二者合用可使细菌的叶酸代谢受到双重阻断.协同抗菌作用较单药增强。");
        explainList.setClick(false);
        lists.add(explainList);
    }

    private void initView() {
        recyclerView = (RecyclerView) mLay.findViewById(R.id.askfrag_rv);
        lists = new ArrayList<>();
        askAdapter = new AskAdapter(lists, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(askAdapter);
        askAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(View v, int position) {
        lists.get(position).setClick(true);
        askAdapter.notifyDataSetChanged();
    }
}
