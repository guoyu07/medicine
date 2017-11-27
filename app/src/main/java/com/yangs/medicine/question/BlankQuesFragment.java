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
import com.yangs.medicine.adapter.BlankAdapter;
import com.yangs.medicine.db.QuestionUtil;
import com.yangs.medicine.fragment.LazyLoadFragment;
import com.yangs.medicine.model.BlankList;
import com.yangs.medicine.model.Question;
import com.yangs.medicine.source.QuestionSource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangs on 2017/10/10 0010.
 * 填空题Fragment
 */

public class BlankQuesFragment extends Fragment implements BlankAdapter.OnItemClickListener {
    private View mLay;
    private RecyclerView recyclerView;
    private BlankAdapter blankAdapter;
    private List<BlankList> lists;
    private OnResultListener onResultListener;
    private int dialogIndex;
    private int start;
    private int end;
    private String type;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mLay = inflater.inflate(R.layout.blankfrag_layout, container, false);
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
            BlankList blankList = new BlankList();
            if (TextUtils.isEmpty(question.getYourAnswer()))
                blankList.setClick(false);
            else
                blankList.setClick(true);
            blankList.setIndex(i + 1);
            blankList.setCha(question.getCha());
            blankList.setSP(question.getSP());
            blankList.setRealID(question.getRealID());
            blankList.setQuestion(question.getQuestion());
            blankList.setAnswer(question.getAnswer());
            lists.add(blankList);
        }
        blankAdapter.notifyDataSetChanged();
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
            }
            blankAdapter.notifyDataSetChanged();
        }
    }

    private void initView() {
        recyclerView = (RecyclerView) mLay.findViewById(R.id.blankfrag_rv);
        lists = new ArrayList<>();
        blankAdapter = new BlankAdapter(lists, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(blankAdapter);
        blankAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(int position) {
        dialogIndex = (int) getArguments().getSerializable("dialogIndex") + position;
        Boolean click = lists.get(position).getClick();
        if (click) {
            lists.get(position).setClick(false);
            QuestionActivity.timuLists.get(dialogIndex).setAnswer("0");
        } else {
            lists.get(position).setClick(true);
            final BlankList question = lists.get(position);
            QuestionActivity.timuLists.get(dialogIndex).setAnswer("1");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    APPlication.questionSource.uploadRecord(
                            APPlication.user, "做题", question.getRealID() + ""
                            , "", "click", question.getSP(), question.getCha());

                }
            }).start();
        }
        blankAdapter.notifyDataSetChanged();
        onResultListener.onResult(dialogIndex, click ? 0 : 1);
    }

    public void setOnResultListener(OnResultListener onResultListener) {
        this.onResultListener = onResultListener;
    }

    public interface OnResultListener {
        void onResult(int dialogIndex, int status);
    }
}
