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
import com.yangs.medicine.activity.QuestionActivity;
import com.yangs.medicine.adapter.ExplainAdapter;
import com.yangs.medicine.db.QuestionUtil;
import com.yangs.medicine.fragment.LazyLoadFragment;
import com.yangs.medicine.model.ExplainList;
import com.yangs.medicine.model.Question;

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
    private OnResultListener onResultListener;
    private int start;
    private int end;
    private int dialogIndex;
    private Question question;

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
        start = (int) getArguments().getSerializable("start");
        end = (int) getArguments().getSerializable("end");
        for (int i = start; i <= end; i++) {
            question = QuestionUtil.getQuestionByID(i + 1);
            ExplainList explainList = new ExplainList();
            explainList.setIndex(i + 1);
            explainList.setName(question.getQuestion());
            explainList.setExplain(question.getAnswer());
            explainList.setClick(false);
            lists.add(explainList);
        }
        explainAdapter.notifyDataSetChanged();
        for (int i = 0; i <= (end - start); i++) {
            int real = i + (int) getArguments().getSerializable("dialogIndex");
            if ("1".equals(QuestionActivity.timuLists.get(real).getAnswer())) {
                lists.get(i).setClick(true);
            } else {
                lists.get(i).setClick(false);
            }
            explainAdapter.notifyDataSetChanged();
        }
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
        Boolean click = lists.get(position).getClick();
        dialogIndex = (int) getArguments().getSerializable("dialogIndex") + position;
        if (click) {
            lists.get(position).setClick(false);
            QuestionActivity.timuLists.get(dialogIndex).setAnswer("0");
        } else {
            lists.get(position).setClick(true);
            QuestionActivity.timuLists.get(dialogIndex).setAnswer("1");
        }
        explainAdapter.notifyDataSetChanged();
        if (getArguments() != null && onResultListener != null) {
            dialogIndex = (int) getArguments().getSerializable("dialogIndex") + position;
            onResultListener.onResult(dialogIndex, click ? 0 : 1);
        }
    }

    public void setOnResultListener(OnResultListener onResultListener) {
        this.onResultListener = onResultListener;
    }

    public interface OnResultListener {
        void onResult(int dialogIndex, int status);
    }
}
