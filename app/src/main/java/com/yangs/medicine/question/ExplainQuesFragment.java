package com.yangs.medicine.question;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

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

public class ExplainQuesFragment extends LazyLoadFragment implements ExplainAdapter.OnItemClickListener {
    private View mLay;
    private RecyclerView recyclerView;
    private List<ExplainList> lists;
    private ExplainAdapter explainAdapter;

    @Override
    protected int setContentView() {
        return R.layout.explainquesfrgament;
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
        ExplainList explainList = new ExplainList();
        explainList.setIndex(1);
        explainList.setName("肠肝循环");
        explainList.setExplain("物质（如胆盐等）从肠道吸收后经门静脉进入肝脏，随肝脏分泌胆汁排入肠内补再次吸收，如此周而复始的循环过程。");
        explainList.setClick(false);
        lists.add(explainList);
        explainList = new ExplainList();
        explainList.setIndex(2);
        explainList.setName("一级动力学消除");
        explainList.setExplain("药物消除的特点是药物消除速率与血药浓度成正比，药物消除按一定比例进行，是恒比消除，当血药浓度高时，单位时间里药物消除量大，血药浓度降低后，单位时间里药物消除减少。");
        explainList.setClick(false);
        lists.add(explainList);
        explainList = new ExplainList();
        explainList.setIndex(3);
        explainList.setName("血浆半衰期");
        explainList.setExplain("指血浆中药物浓度下降一半所需的时间，其长短可以反映药物消除的速度。");
        explainList.setClick(false);
        lists.add(explainList);
    }

    private void initView() {
        mLay = getContentView();
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
