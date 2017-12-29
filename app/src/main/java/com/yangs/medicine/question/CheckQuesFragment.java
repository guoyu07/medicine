package com.yangs.medicine.question;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yangs.medicine.R;
import com.yangs.medicine.activity.APPlication;
import com.yangs.medicine.activity.QuestionActivity;
import com.yangs.medicine.db.QuestionUtil;
import com.yangs.medicine.fragment.LazyLoadFragment;
import com.yangs.medicine.model.Question;
import com.yangs.medicine.source.QuestionSource;

/**
 * Created by yangs on 2017/10/10 0010.
 * 判断题Fragment
 */

public class CheckQuesFragment extends Fragment implements View.OnClickListener {
    private View mLay;
    private TextView tv_ques;
    private int dialogIndex;
    //A选项
    private LinearLayout ll_A;
    private TextView ll_A_1;
    private LinearLayout ll_A_2;
    private LinearLayout ll_A_3;
    private TextView ll_A_4;
    //B选项
    private LinearLayout ll_B;
    private TextView ll_B_1;
    private LinearLayout ll_B_2;
    private LinearLayout ll_B_3;
    private TextView ll_B_4;
    //解析
    private LinearLayout ll_jiexi;
    private TextView ll_jiexi2;
    private OnResultListener onResultListener;
    private Button bt_sub;
    private Question question;
    private String your_answer = "";
    private String type;
    private Button bt_error;
    private Handler handler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mLay = inflater.inflate(R.layout.checkquesfrgament, container, false);
        initView();
        return mLay;
    }

    private void initView() {
        handler = new Handler();
        tv_ques = (TextView) mLay.findViewById(R.id.checkquesfrag_tv_ques);
        ll_A = (LinearLayout) mLay.findViewById(R.id.checkquesfrag_ll_A);
        ll_B = (LinearLayout) mLay.findViewById(R.id.checkquesfrag_ll_B);
        ll_A_1 = (TextView) mLay.findViewById(R.id.checkquesfrag_ll_A_1);
        ll_B_1 = (TextView) mLay.findViewById(R.id.checkquesfrag_ll_B_1);
        ll_A_2 = (LinearLayout) mLay.findViewById(R.id.checkquesfrag_ll_A_2);
        ll_B_2 = (LinearLayout) mLay.findViewById(R.id.checkquesfrag_ll_B_2);
        ll_A_3 = (LinearLayout) mLay.findViewById(R.id.checkquesfrag_ll_A_3);
        ll_B_3 = (LinearLayout) mLay.findViewById(R.id.checkquesfrag_ll_B_3);
        ll_A_4 = (TextView) mLay.findViewById(R.id.checkquesfrag_ll_A_4);
        ll_B_4 = (TextView) mLay.findViewById(R.id.checkquesfrag_ll_B_4);
        ll_jiexi = (LinearLayout) mLay.findViewById(R.id.checkquesfrag_ll_jiexi);
        ll_jiexi2 = (TextView) mLay.findViewById(R.id.checkquesfrag_ll_jiexi2);
        bt_sub = (Button) mLay.findViewById(R.id.checkquesfrag_bt_sub);
        bt_error = (Button) mLay.findViewById(R.id.checkquesfrag_bt_error);
        bt_error.setOnClickListener(this);
        ll_A.setOnClickListener(this);
        ll_B.setOnClickListener(this);
        bt_sub.setOnClickListener(this);
        if (getArguments() != null) {
            int index = (int) getArguments().getSerializable("index");
            type = (String) getArguments().getSerializable("type");
            if (type.equals("error"))
                question = QuestionUtil.getQuestionByID(index + 1, QuestionSource.ERROR_TABLE_NAME);
            else
                question = QuestionUtil.getQuestionByID(index + 1, QuestionSource.QUESTION_TABLE_NAME);
            tv_ques.setText(question.getId() + "." + question.getQuestion());
        }
        dialogIndex = (int) getArguments().getSerializable("dialogIndex");
        String answer = QuestionActivity.timuLists.get(dialogIndex).getAnswer();
        if (answer == null || answer.equals("")) {
            String t_answewr = question.getYourAnswer();
            if (!TextUtils.isEmpty(t_answewr)) {
                answer = t_answewr;
                QuestionActivity.timuLists.get(dialogIndex).setSubmmit(true);
            }
        }
        if ("对".equals(answer)) {
            setAClick();
        } else if ("错".equals(answer)) {
            setBClick();
        }
        if (QuestionActivity.timuLists.get(dialogIndex).getSubmmit())
            checkOK(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.checkquesfrag_ll_A:
                reset();
                setAClick();
                break;
            case R.id.checkquesfrag_ll_B:
                reset();
                setBClick();
                break;
            case R.id.checkquesfrag_bt_sub:
                if (TextUtils.isEmpty(your_answer))
                    APPlication.showToast("要自己先做了才能查看答案哦!", 0);
                else
                    checkOK(false);
                break;
            case R.id.checkquesfrag_bt_error:
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
                                APPlication.questionSource.addQuestionError(question.getRealID()
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
                break;
        }
    }

    private void reset() {
        ll_A_1.setBackgroundResource(R.drawable.selector_white_7dp);
        ll_A_1.setTextColor(ContextCompat.getColor(getContext(), R.color.error_tv));
        ll_B_1.setBackgroundResource(R.drawable.selector_white_7dp);
        ll_B_1.setTextColor(ContextCompat.getColor(getContext(), R.color.error_tv));
        your_answer = "";
    }

    private void setAClick() {
        ll_A_1.setBackgroundResource(R.drawable.ques_selector_blue);
        ll_A_1.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        your_answer = "对";
        QuestionActivity.timuLists.get(dialogIndex).setAnswer("对");
    }

    private void setBClick() {
        ll_B_1.setBackgroundResource(R.drawable.ques_selector_blue);
        ll_B_1.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        your_answer = "错";
        QuestionActivity.timuLists.get(dialogIndex).setAnswer("错");
    }

    private void setATrue() {
        ll_A_1.setVisibility(View.GONE);
        ll_A_2.setVisibility(View.VISIBLE);
        ll_A_3.setVisibility(View.GONE);
    }

    private void setAFalse() {
        ll_A_1.setVisibility(View.GONE);
        ll_A_2.setVisibility(View.GONE);
        ll_A_3.setVisibility(View.VISIBLE);
    }

    private void setBTrue() {
        ll_B_1.setVisibility(View.GONE);
        ll_B_2.setVisibility(View.VISIBLE);
        ll_B_3.setVisibility(View.GONE);
    }

    private void setBFalse() {
        ll_B_1.setVisibility(View.GONE);
        ll_B_2.setVisibility(View.GONE);
        ll_B_3.setVisibility(View.VISIBLE);
    }

    public void checkOK(final Boolean isRedo) {
        bt_sub.setVisibility(View.GONE);
        final Boolean check = your_answer.equals(question.getAnswer());
        QuestionActivity.timuLists.get(dialogIndex).setSubmmit(true);
        if (check) {
            switch (your_answer) {
                case "对":
                    setATrue();
                    break;
                case "错":
                    setBTrue();
                    break;
            }
        } else {
            switch (question.getAnswer()) {
                case "对":
                    setATrue();
                    setBFalse();
                    break;
                case "错":
                    setAFalse();
                    setBTrue();
                    break;
            }
        }
        String ex = question.getExplains();
        ll_jiexi2.setText(TextUtils.isEmpty(ex) ? "暂无" : ex);
        ll_jiexi.setVisibility(View.VISIBLE);
        if (getArguments() != null && onResultListener != null) {
            dialogIndex = (int) getArguments().getSerializable("dialogIndex");
            if (check)
                onResultListener.onResult(dialogIndex, 1);
            else
                onResultListener.onResult(dialogIndex, 0);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!isRedo && !APPlication.DEBUG) {
                    String result = check ? "对" : "错";
                    APPlication.questionSource.uploadRecord(
                            APPlication.user, "做题", question.getRealID() + ""
                            , result, your_answer, question.getSP(), question.getCha());
                }
            }
        }).start();
    }

    public void setOnResultListener(OnResultListener onResultListener) {
        this.onResultListener = onResultListener;
    }

    public interface OnResultListener {
        void onResult(int dialogIndex, int status);
    }
}
