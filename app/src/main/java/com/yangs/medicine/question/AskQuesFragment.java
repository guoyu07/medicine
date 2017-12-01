package com.yangs.medicine.question;

import android.os.Bundle;
import android.os.Handler;
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
import com.yangs.medicine.adapter.AskAdapter;
import com.yangs.medicine.db.QuestionUtil;
import com.yangs.medicine.fragment.LazyLoadFragment;
import com.yangs.medicine.model.ExplainList;
import com.yangs.medicine.model.Question;
import com.yangs.medicine.source.QuestionSource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangs on 2017/10/10 0010.
 * 问答题Fragment
 */

public class AskQuesFragment extends Fragment implements AskAdapter.OnItemClickListener, AskAdapter.OnBtItemClickListener {
    private View mLay;
    private RecyclerView recyclerView;
    private AskAdapter askAdapter;
    private List<ExplainList> lists;
    private OnResultListener onResultListener;
    private int dialogIndex;
    private Question question;
    private String type;
    private Handler handler;

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
        type = (String) getArguments().getSerializable("type");
        if (type.equals("error"))
            question = QuestionUtil.getQuestionByID(index + 1, QuestionSource.ERROR_TABLE_NAME);
        else
            question = QuestionUtil.getQuestionByID(index + 1, QuestionSource.QUESTION_TABLE_NAME);
        ExplainList explainList = new ExplainList();
        explainList.setIndex(index + 1);
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
        if (question.getIsInError().equals("true"))
            explainList.setAddError(true);
        else
            explainList.setAddError(false);
        lists.add(explainList);
        if (!TextUtils.isEmpty(lists.get(0).getAnswer()))
            lists.get(0).setClick(true);
        else {
            if ("1".equals(QuestionActivity.timuLists.get(dialogIndex).getAnswer())) {
                lists.get(0).setClick(true);
            } else if ("0".equals(QuestionActivity.timuLists.get(dialogIndex).getAnswer())) {
                lists.get(0).setClick(false);
            }
            askAdapter.notifyDataSetChanged();
        }
    }

    private void initView() {
        handler = new Handler();
        recyclerView = (RecyclerView) mLay.findViewById(R.id.askfrag_rv);
        lists = new ArrayList<>();
        askAdapter = new AskAdapter(lists, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(askAdapter);
        askAdapter.setOnItemClickListener(this);
        askAdapter.setOnBtItemClickListener(this);
    }

    @Override
    public void onItemClick(View v, int position) {
        dialogIndex = (int) getArguments().getSerializable("dialogIndex");
        Boolean click = lists.get(position).getClick();
        if (click) {
            lists.get(position).setClick(false);
            QuestionActivity.timuLists.get(dialogIndex).setAnswer("0");
        } else {
            lists.get(position).setClick(true);
            QuestionActivity.timuLists.get(dialogIndex).setAnswer("1");
        }
        askAdapter.notifyDataSetChanged();
        if (getArguments() != null && onResultListener != null) {
            onResultListener.onResult(dialogIndex, click ? 0 : 1);
        }
        final ExplainList explainList = lists.get(position);
        new Thread(new Runnable() {
            @Override
            public void run() {
                APPlication.questionSource.uploadRecord(
                        APPlication.user, "做题", explainList.getRealID() + ""
                        , "", "click", explainList.getSP(), explainList.getCha());

            }
        }).start();
    }

    public void setOnResultListener(OnResultListener onResultListener) {
        this.onResultListener = onResultListener;
    }

    @Override
    public void onBtItemClick(final int position) {
        final ExplainList explainList = lists.get(position);
        if (explainList.getAddError()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    APPlication.questionSource.removeError(APPlication.user, explainList.getRealID()
                            , new QuestionSource.OnResponseCodeResultListener() {
                                @Override
                                public void onResponseResult(int code, String response) {
                                    if (code == -1)
                                        return;
                                    if (response.equals("失败"))
                                        return;
                                    lists.get(position).setAddError(false);
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            askAdapter.notifyDataSetChanged();
                                        }
                                    });
                                }
                            });
                }
            }).start();
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String s = APPlication.questionSource.uploadRecord(
                            APPlication.user, "做题", explainList.getRealID() + ""
                            , "错", "错", explainList.getSP(), explainList.getCha());
                    if (s.equals("成功")) {
                        lists.get(position).setAddError(true);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                askAdapter.notifyDataSetChanged();
                            }
                        });
                    } else {
                        APPlication.showToast("加入错题集发生了错误,请反馈!", 1);
                    }
                }
            }).start();
        }
    }

    public interface OnResultListener {
        void onResult(int dialogIndex, int status);
    }
}
