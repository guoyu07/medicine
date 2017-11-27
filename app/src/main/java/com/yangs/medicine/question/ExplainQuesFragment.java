package com.yangs.medicine.question;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yangs.medicine.R;
import com.yangs.medicine.activity.APPlication;
import com.yangs.medicine.activity.QuestionActivity;
import com.yangs.medicine.adapter.ExplainAdapter;
import com.yangs.medicine.db.QuestionUtil;
import com.yangs.medicine.fragment.LazyLoadFragment;
import com.yangs.medicine.model.ExplainList;
import com.yangs.medicine.model.Question;
import com.yangs.medicine.source.QuestionSource;

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
    private String type;

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
        type = (String) getArguments().getSerializable("type");
        for (int i = start; i <= end; i++) {
            Question question;
            if (type.equals("error"))
                question = QuestionUtil.getQuestionByID(i + 1, QuestionSource.ERROR_TABLE_NAME);
            else
                question = QuestionUtil.getQuestionByID(i + 1, QuestionSource.QUESTION_TABLE_NAME);
            ExplainList explainList = new ExplainList();
            explainList.setIndex(i + 1);
            explainList.setCha(question.getCha());
            explainList.setSP(question.getSP());
            explainList.setAnswer(question.getYourAnswer());
            explainList.setName(question.getQuestion());
            explainList.setRealID(question.getRealID());
            explainList.setExplain(question.getAnswer());
            if (TextUtils.isEmpty(question.getYourAnswer()))
                explainList.setClick(false);
            else
                explainList.setClick(true);
            lists.add(explainList);
        }
        explainAdapter.notifyDataSetChanged();
        for (int i = 0; i <= (end - start); i++) {
            int real = i + (int) getArguments().getSerializable("dialogIndex");
            if (!TextUtils.isEmpty(lists.get(i).getAnswer()))
                lists.get(i).setClick(true);
            else {
                if ("1".equals(QuestionActivity.timuLists.get(real).getAnswer())) {
                    lists.get(i).setClick(true);
                } else {
                    lists.get(i).setClick(false);
                }
                explainAdapter.notifyDataSetChanged();
            }
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
            final ExplainList explainList = lists.get(position);
            QuestionActivity.timuLists.get(dialogIndex).setAnswer("1");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    APPlication.questionSource.uploadRecord(
                            APPlication.user, "做题", explainList.getRealID() + ""
                            , "", "click", explainList.getSP(), explainList.getCha());

                }
            }).start();
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
