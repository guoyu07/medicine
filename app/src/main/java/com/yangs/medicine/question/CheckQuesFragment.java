package com.yangs.medicine.question;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yangs.medicine.R;
import com.yangs.medicine.activity.QuestionActivity;
import com.yangs.medicine.db.QuestionUtil;
import com.yangs.medicine.fragment.LazyLoadFragment;
import com.yangs.medicine.model.Question;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mLay = inflater.inflate(R.layout.checkquesfrgament, container, false);
        initView();
        return mLay;
    }

    private void initView() {
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
        ll_A.setOnClickListener(this);
        ll_B.setOnClickListener(this);
        bt_sub.setOnClickListener(this);
        if (getArguments() != null) {
            int index = (int) getArguments().getSerializable("index");
            question = QuestionUtil.getQuestionByID(index + 1);
            tv_ques.setText(question.getId() + "." + question.getQuestion());
        }
        dialogIndex = (int) getArguments().getSerializable("dialogIndex");
        String answer = QuestionActivity.timuLists.get(dialogIndex).getAnswer();
        if ("对".equals(answer)) {
            setAClick();
        } else if ("错".equals(answer)) {
            setBClick();
        }
        if (QuestionActivity.timuLists.get(dialogIndex).getSubmmit())
            checkOK();
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
                checkOK();
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

    public void checkOK() {
        bt_sub.setVisibility(View.GONE);
        Boolean check = your_answer.equals(question.getAnswer());
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
    }

    public void setOnResultListener(OnResultListener onResultListener) {
        this.onResultListener = onResultListener;
    }

    public interface OnResultListener {
        void onResult(int dialogIndex, int status);
    }
}
