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
import com.yangs.medicine.adapter.ExplainAdapter;
import com.yangs.medicine.fragment.LazyLoadFragment;
import com.yangs.medicine.model.ExplainList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangs on 2017/10/10 0010.
 * 名词解释Fragment
 */

public class ExplainQuesFragment extends Fragment implements ExplainAdapter.OnItemClickListener {
    private View mLay;
    private RecyclerView recyclerView;
    private List<ExplainList> lists;
    private ExplainAdapter explainAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mLay = inflater.inflate(R.layout.explainquesfrgament, container, false);
        initView();
        initData();
        return mLay;
    }

    private void initData() {
        lists.clear();
        if (getArguments() == null)
            return;
        int start = (int) getArguments().getSerializable("start");
        int end = (int) getArguments().getSerializable("end");
        for (int i = start; i <= end; i++) {
            ExplainList explainList = new ExplainList();
            explainList.setIndex(i + 1);
            explainList.setName("肠肝循环");
            explainList.setExplain("物质（如胆盐等）从肠道吸收后经门静脉进入肝脏，随肝脏分泌胆汁排入肠内补再次吸收，如此周而复始的循环过程。");
            explainList.setClick(false);
            lists.add(explainList);
        }
        explainAdapter.notifyDataSetChanged();
    }

    private void initView() {
        recyclerView = (RecyclerView) mLay.findViewById(R.id.explainfrag_rv);
        lists = new ArrayList<>();
        explainAdapter = new ExplainAdapter(lists, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(explainAdapter);
        explainAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(View v, int position) {
        lists.get(position).setClick(true);
        explainAdapter.notifyDataSetChanged();
    }
}
