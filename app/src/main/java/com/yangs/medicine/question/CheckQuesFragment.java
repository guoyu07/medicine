package com.yangs.medicine.question;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yangs.medicine.R;
import com.yangs.medicine.fragment.LazyLoadFragment;

/**
 * Created by yangs on 2017/10/10 0010.
 * 判断题Fragment
 */

public class CheckQuesFragment extends Fragment implements View.OnClickListener {
    private View mLay;
    private TextView tv_ques;
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
        ll_A.setOnClickListener(this);
        ll_B.setOnClickListener(this);
        if (getArguments() != null) {
            int index = (int) getArguments().getSerializable("index");
            tv_ques.setText(index + 1 + ".判断题");
        }
    }

    private int a = 0;
    private int b = 0;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.checkquesfrag_ll_A:
                reset();
                ll_A_1.setBackgroundResource(R.drawable.ques_selector_blue);
                ll_A_1.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                a = 1;
                break;
            case R.id.checkquesfrag_ll_B:
                reset();
                ll_B_1.setBackgroundResource(R.drawable.ques_selector_blue);
                ll_B_1.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                b = 1;
                break;
        }
    }

    private void reset() {
        ll_A_1.setBackgroundResource(R.drawable.selector_white_7dp);
        ll_A_1.setTextColor(ContextCompat.getColor(getContext(), R.color.error_tv));
        ll_B_1.setBackgroundResource(R.drawable.selector_white_7dp);
        ll_B_1.setTextColor(ContextCompat.getColor(getContext(), R.color.error_tv));
        a = 0;
        b = 0;
    }

    public void checkOK() {
        if (b == 1) {
            ll_B_1.setVisibility(View.GONE);
            ll_B_2.setVisibility(View.VISIBLE);
            ll_B_3.setVisibility(View.GONE);
        } else if (a == 1) {
            ll_A_1.setVisibility(View.GONE);
            ll_A_2.setVisibility(View.GONE);
            ll_A_3.setVisibility(View.VISIBLE);
        }
        ll_jiexi.setVisibility(View.VISIBLE);
    }
}
