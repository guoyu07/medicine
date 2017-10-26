package com.yangs.medicine.question;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yangs.medicine.R;
import com.yangs.medicine.activity.APPlication;
import com.yangs.medicine.activity.QuestionActivity;
import com.yangs.medicine.db.QuestionUtil;
import com.yangs.medicine.fragment.LazyLoadFragment;
import com.yangs.medicine.model.ChooseList;
import com.yangs.medicine.model.Question;
import com.yangs.medicine.model.TimuList;

/**
 * Created by yangs on 2017/10/10 0010.
 * 选择题Fragment
 * 复杂界面,后期模块化
 */

public class ChooseQuesFragment extends Fragment implements View.OnClickListener {
    private View mLay;
    private OnResultListener onResultListener;
    private static final String TAG = "ChooseQuesFragment";
    private int dialogindex;
    //问题
    public TextView tv_ques;
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
    //C选项
    private LinearLayout ll_C;
    private TextView ll_C_1;
    private LinearLayout ll_C_2;
    private LinearLayout ll_C_3;
    private TextView ll_C_4;
    //D选项
    private LinearLayout ll_D;
    private TextView ll_D_1;
    private LinearLayout ll_D_2;
    private LinearLayout ll_D_3;
    private TextView ll_D_4;
    //E选项
    private LinearLayout ll_E;
    private TextView ll_E_1;
    private LinearLayout ll_E_2;
    private LinearLayout ll_E_3;
    private TextView ll_E_4;
    //解析
    private LinearLayout ll_jiexi;
    private TextView ll_jiexi2;
    private Button bt_sub;
    private String your_answer;
    private Question question;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mLay = inflater.inflate(R.layout.choosequesfrgament, container, false);
        initView();
        return mLay;
    }

    private void initView() {
        tv_ques = (TextView) mLay.findViewById(R.id.choosequesfrag_tv_ques);
        ll_A = (LinearLayout) mLay.findViewById(R.id.choosequesfrag_ll_A);
        ll_B = (LinearLayout) mLay.findViewById(R.id.choosequesfrag_ll_B);
        ll_C = (LinearLayout) mLay.findViewById(R.id.choosequesfrag_ll_C);
        ll_D = (LinearLayout) mLay.findViewById(R.id.choosequesfrag_ll_D);
        ll_E = (LinearLayout) mLay.findViewById(R.id.choosequesfrag_ll_E);
        ll_A_1 = (TextView) mLay.findViewById(R.id.choosequesfrag_ll_A_1);
        ll_B_1 = (TextView) mLay.findViewById(R.id.choosequesfrag_ll_B_1);
        ll_C_1 = (TextView) mLay.findViewById(R.id.choosequesfrag_ll_C_1);
        ll_D_1 = (TextView) mLay.findViewById(R.id.choosequesfrag_ll_D_1);
        ll_E_1 = (TextView) mLay.findViewById(R.id.choosequesfrag_ll_E_1);
        ll_A_2 = (LinearLayout) mLay.findViewById(R.id.choosequesfrag_ll_A_2);
        ll_B_2 = (LinearLayout) mLay.findViewById(R.id.choosequesfrag_ll_B_2);
        ll_C_2 = (LinearLayout) mLay.findViewById(R.id.choosequesfrag_ll_C_2);
        ll_D_2 = (LinearLayout) mLay.findViewById(R.id.choosequesfrag_ll_D_2);
        ll_E_2 = (LinearLayout) mLay.findViewById(R.id.choosequesfrag_ll_E_2);
        ll_A_3 = (LinearLayout) mLay.findViewById(R.id.choosequesfrag_ll_A_3);
        ll_B_3 = (LinearLayout) mLay.findViewById(R.id.choosequesfrag_ll_B_3);
        ll_C_3 = (LinearLayout) mLay.findViewById(R.id.choosequesfrag_ll_C_3);
        ll_D_3 = (LinearLayout) mLay.findViewById(R.id.choosequesfrag_ll_D_3);
        ll_E_3 = (LinearLayout) mLay.findViewById(R.id.choosequesfrag_ll_E_3);
        ll_A_4 = (TextView) mLay.findViewById(R.id.choosequesfrag_ll_A_4);
        ll_B_4 = (TextView) mLay.findViewById(R.id.choosequesfrag_ll_B_4);
        ll_C_4 = (TextView) mLay.findViewById(R.id.choosequesfrag_ll_C_4);
        ll_D_4 = (TextView) mLay.findViewById(R.id.choosequesfrag_ll_D_4);
        ll_E_4 = (TextView) mLay.findViewById(R.id.choosequesfrag_ll_E_4);
        bt_sub = (Button) mLay.findViewById(R.id.choosequesfrag_bt_sub);
        ll_jiexi = (LinearLayout) mLay.findViewById(R.id.choosequesfrag_ll_jiexi);
        ll_jiexi2 = (TextView) mLay.findViewById(R.id.choosequesfrag_ll_jiexi2);
        ll_A.setOnClickListener(this);
        ll_B.setOnClickListener(this);
        ll_C.setOnClickListener(this);
        ll_D.setOnClickListener(this);
        ll_E.setOnClickListener(this);
        bt_sub.setOnClickListener(this);
        if (getArguments() != null) {
            int index = (int) getArguments().getSerializable("index");
            updateQuestion(index);
        }
        dialogindex = (int) getArguments().getSerializable("dialogIndex");
        String answer = QuestionActivity.timuLists.get(dialogindex).getAnswer();
        if (!"".equals(answer)) {
            switch (answer) {
                case "a":
                    setA();
                    break;
                case "b":
                    setB();
                    break;
                case "c":
                    setC();
                    break;
                case "d":
                    setD();
                    break;
                case "e":
                    setE();
                    break;
            }
        }
        if (QuestionActivity.timuLists.get(dialogindex).getSubmmit())
            checkOK();
    }

    private void setA() {
        ll_A_1.setBackgroundResource(R.drawable.ques_selector_blue);
        ll_A_1.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        your_answer = "A";
    }

    private void setB() {
        ll_B_1.setBackgroundResource(R.drawable.ques_selector_blue);
        ll_B_1.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        your_answer = "B";
    }

    private void setC() {
        ll_C_1.setBackgroundResource(R.drawable.ques_selector_blue);
        ll_C_1.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        your_answer = "C";
    }

    private void setD() {
        ll_D_1.setBackgroundResource(R.drawable.ques_selector_blue);
        ll_D_1.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        your_answer = "D";
    }

    private void setE() {
        ll_E_1.setBackgroundResource(R.drawable.ques_selector_blue);
        ll_E_1.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        your_answer = "E";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.choosequesfrag_ll_A:
                reset();
                setA();
                QuestionActivity.timuLists.get(dialogindex).setAnswer("a");
                break;
            case R.id.choosequesfrag_ll_B:
                reset();
                setB();
                QuestionActivity.timuLists.get(dialogindex).setAnswer("b");
                break;
            case R.id.choosequesfrag_ll_C:
                reset();
                setC();
                QuestionActivity.timuLists.get(dialogindex).setAnswer("c");
                break;
            case R.id.choosequesfrag_ll_D:
                reset();
                setD();
                QuestionActivity.timuLists.get(dialogindex).setAnswer("d");
                break;
            case R.id.choosequesfrag_ll_E:
                reset();
                setE();
                QuestionActivity.timuLists.get(dialogindex).setAnswer("e");
                break;
            case R.id.choosequesfrag_bt_sub:
                checkOK();
                break;
        }
    }

    public void updateQuestion(int index) {
        question = QuestionUtil.getQuestionByID(index + 1);
        tv_ques.setText(question.getId() + "." + question.getQuestion());
        ll_A_4.setText(question.getA());
        ll_B_4.setText(question.getB());
        ll_C_4.setText(question.getC());
        ll_D_4.setText(question.getD());
        ll_E_4.setText(question.getE());
        String ex = question.getExplains();
        ll_jiexi2.setText(TextUtils.isEmpty(ex) ? "暂无" : ex);
    }

    private void reset() {
        ll_A_1.setBackgroundResource(R.drawable.selector_white_7dp);
        ll_A_1.setTextColor(ContextCompat.getColor(getContext(), R.color.error_tv));
        ll_B_1.setBackgroundResource(R.drawable.selector_white_7dp);
        ll_B_1.setTextColor(ContextCompat.getColor(getContext(), R.color.error_tv));
        ll_C_1.setBackgroundResource(R.drawable.selector_white_7dp);
        ll_C_1.setTextColor(ContextCompat.getColor(getContext(), R.color.error_tv));
        ll_D_1.setBackgroundResource(R.drawable.selector_white_7dp);
        ll_D_1.setTextColor(ContextCompat.getColor(getContext(), R.color.error_tv));
        ll_E_1.setBackgroundResource(R.drawable.selector_white_7dp);
        ll_E_1.setTextColor(ContextCompat.getColor(getContext(), R.color.error_tv));
        your_answer = "";
    }

    public void checkOK() {
        QuestionActivity.timuLists.get(dialogindex).setSubmmit(true);
        bt_sub.setVisibility(View.GONE);
        Boolean check = your_answer.equalsIgnoreCase(question.getAnswer());
        switch (your_answer) {
            case "A":
            case "a":
                ll_A_1.setVisibility(View.GONE);
                if (check) {
                    ll_A_2.setVisibility(View.VISIBLE);
                    ll_A_3.setVisibility(View.GONE);
                } else {
                    ll_A_2.setVisibility(View.GONE);
                    ll_A_3.setVisibility(View.VISIBLE);
                }
                break;
            case "B":
            case "b":
                ll_B_1.setVisibility(View.GONE);
                if (check) {
                    ll_B_2.setVisibility(View.VISIBLE);
                    ll_B_3.setVisibility(View.GONE);
                } else {
                    ll_B_2.setVisibility(View.GONE);
                    ll_B_3.setVisibility(View.VISIBLE);
                }
                break;
            case "C":
            case "c":
                ll_C_1.setVisibility(View.GONE);
                if (check) {
                    ll_C_2.setVisibility(View.VISIBLE);
                    ll_C_3.setVisibility(View.GONE);
                } else {
                    ll_C_2.setVisibility(View.GONE);
                    ll_C_3.setVisibility(View.VISIBLE);
                }
                break;
            case "D":
            case "d":
                ll_D_1.setVisibility(View.GONE);
                if (check) {
                    ll_D_2.setVisibility(View.VISIBLE);
                    ll_D_3.setVisibility(View.GONE);
                } else {
                    ll_D_2.setVisibility(View.GONE);
                    ll_D_3.setVisibility(View.VISIBLE);
                }
                break;
            case "E":
            case "e":
                ll_E_1.setVisibility(View.GONE);
                if (check) {
                    ll_E_2.setVisibility(View.VISIBLE);
                    ll_E_3.setVisibility(View.GONE);
                } else {
                    ll_E_2.setVisibility(View.GONE);
                    ll_E_3.setVisibility(View.VISIBLE);
                }
                break;
        }
        if (!check) {
            switch (question.getAnswer()) {
                case "A":
                case "a":
                    ll_A_1.setVisibility(View.GONE);
                    ll_A_2.setVisibility(View.VISIBLE);
                    ll_A_3.setVisibility(View.GONE);
                    break;
                case "B":
                case "b":
                    ll_B_1.setVisibility(View.GONE);
                    ll_B_2.setVisibility(View.VISIBLE);
                    ll_B_3.setVisibility(View.GONE);
                    break;
                case "C":
                case "c":
                    ll_C_1.setVisibility(View.GONE);
                    ll_C_2.setVisibility(View.VISIBLE);
                    ll_C_3.setVisibility(View.GONE);
                    break;
                case "D":
                case "d":
                    ll_D_1.setVisibility(View.GONE);
                    ll_D_2.setVisibility(View.VISIBLE);
                    ll_D_3.setVisibility(View.GONE);
                    break;
                case "E":
                case "e":
                    ll_E_1.setVisibility(View.GONE);
                    ll_E_2.setVisibility(View.VISIBLE);
                    ll_E_3.setVisibility(View.GONE);
                    break;
            }
        }
        ll_jiexi.setVisibility(View.VISIBLE);
        if (getArguments() != null && onResultListener != null) {
            int dialogIndex = (int) getArguments().getSerializable("dialogIndex");
            if (check)
                onResultListener.onResult(dialogIndex, 1);
            else
                onResultListener.onResult(dialogIndex, 0);
        }
    }

    public void setOnResultListener(OnResultListener onResultListener) {
        this.onResultListener = onResultListener;
    }

    public interface OnResultListener {
        void onResult(int dialogIndex, int status);
    }
}
