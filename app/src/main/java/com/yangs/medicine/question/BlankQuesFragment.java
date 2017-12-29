package com.yangs.medicine.question;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

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

public class BlankQuesFragment extends Fragment implements BlankAdapter.OnItemClickListener, BlankAdapter.OnBtErrorClickListener, BlankAdapter.OnBtItemClickListener {
    private View mLay;
    private RecyclerView recyclerView;
    private BlankAdapter blankAdapter;
    private List<BlankList> lists;
    private OnResultListener onResultListener;
    private int dialogIndex;
    private int start;
    private int end;
    private String type;
    private Handler handler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mLay = inflater.inflate(R.layout.blankfrag_layout, container, false);
        initView();
        initData();
        return mLay;
    }

    private void initData() {
        handler = new Handler();
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
            blankList.setYourAnswer(question.getYourAnswer());
            if (question.getIsInError().equals("true"))
                blankList.setAddError(true);
            else
                blankList.setAddError(false);
            lists.add(blankList);
        }
        blankAdapter.notifyDataSetChanged();
        for (int i = 0; i <= (end - start); i++) {
            int real = i + (int) getArguments().getSerializable("dialogIndex");
            if (!TextUtils.isEmpty(lists.get(i).getYourAnswer()))
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
        blankAdapter.setOnBtErrorClickListener(this);
        blankAdapter.setOnBtItemClickListener(this);
    }

    @Override
    public void onItemClick(int position) {
        dialogIndex = (int) getArguments().getSerializable("dialogIndex") + position;
        Boolean click = lists.get(position).getClick();
        final BlankList question = lists.get(position);
        if (click) {
            lists.get(position).setClick(false);
            QuestionActivity.timuLists.get(dialogIndex).setAnswer("0");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    APPlication.questionSource.uploadRecord(
                            APPlication.user, "做题", question.getRealID() + ""
                            , "", "", question.getSP(), question.getCha());

                }
            }).start();
        } else {
            lists.get(position).setClick(true);
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

    @Override
    public void onBtError(int position) {
        final BlankList explainList = lists.get(position);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.errordialog_layout, null);
        final ImageView iv_close = (ImageView) view.findViewById(R.id.errordialog_iv_close);
        final CheckBox cb_1 = (CheckBox) view.findViewById(R.id.errordialog_cb_1);
        final CheckBox cb_2 = (CheckBox) view.findViewById(R.id.errordialog_cb_2);
        final CheckBox cb_3 = (CheckBox) view.findViewById(R.id.errordialog_cb_3);
        final EditText et_content = (EditText) view.findViewById(R.id.errordialog_et_content);
        final ProgressBar pb = (ProgressBar) view.findViewById(R.id.errordialog_pb);
        final Button bt_sub = (Button) view.findViewById(R.id.errordialog_bt_sub);
        final Dialog errordialog = new AlertDialog.Builder(getContext()).setCancelable(false).setView(view)
                .create();
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errordialog.dismiss();
            }
        });
        bt_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String s_qq = (cb_1.isChecked() ? cb_1.getText().toString() : "")
                        + ";" + (cb_2.isChecked() ? cb_2.getText().toString() : "")
                        + ";" + (cb_3.isChecked() ? cb_3.getText().toString() : "");
                final String s_content = et_content.getText().toString().trim();
                pb.setVisibility(View.VISIBLE);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        APPlication.questionSource.addQuestionError(explainList.getRealID()
                                , s_qq, s_content, APPlication.user, new QuestionSource.OnResponseCodeResultListener() {
                                    @Override
                                    public void onResponseResult(final int code, final String response) {
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                pb.setVisibility(View.GONE);
                                                if (code == -1) {
                                                    APPlication.showToast("网络出错", 0);
                                                    return;
                                                }
                                                if (response.equals("成功")) {
                                                    APPlication.showToast("提交成功", 0);
                                                    errordialog.dismiss();
                                                } else {
                                                    APPlication.showToast("提交失败,请通过我的-" +
                                                            "反馈意见来报告错误", 1);
                                                }
                                            }
                                        });
                                    }
                                });
                    }
                }).start();
            }
        });
        errordialog.show();
    }

    @Override
    public void onBtItemClick(final int position) {
        final BlankList explainList = lists.get(position);
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
                                            blankAdapter.notifyDataSetChanged();
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
                                blankAdapter.notifyDataSetChanged();
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
