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
import com.yangs.medicine.model.ChooseList;
import com.yangs.medicine.model.TimuList;

/**
 * Created by yangs on 2017/10/10 0010.
 * 选择题Fragment
 * 复杂界面,后期模块化
 */

public class ChooseQuesFragment extends LazyLoadFragment implements View.OnClickListener {
    private View mLay;
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

    @Override
    protected int setContentView() {
        return R.layout.choosequesfrgament;
    }

    @Override
    protected void lazyLoad() {
        if (isInit) {
            if (!isLoad) {
                initView();
            }
        }

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
        ll_jiexi = (LinearLayout) mLay.findViewById(R.id.choosequesfrag_ll_jiexi);
        ll_jiexi2 = (TextView) mLay.findViewById(R.id.choosequesfrag_ll_jiexi2);
        ll_A.setOnClickListener(this);
        ll_B.setOnClickListener(this);
        ll_C.setOnClickListener(this);
        ll_D.setOnClickListener(this);
        ll_E.setOnClickListener(this);
        if (getArguments() != null) {
            TimuList timuList = (TimuList) getArguments().getSerializable("question");
            updateQuestion(timuList);
        }
    }

    private int b = 0;
    private int d = 0;
    private int a = 0;
    private int c = 0;
    private int e = 0;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.choosequesfrag_ll_A:
                reset();
                ll_A_1.setBackgroundResource(R.drawable.ques_selector_blue);
                ll_A_1.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                a = 1;
                break;
            case R.id.choosequesfrag_ll_B:
                reset();
                ll_B_1.setBackgroundResource(R.drawable.ques_selector_blue);
                ll_B_1.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                b = 1;
                break;
            case R.id.choosequesfrag_ll_C:
                reset();
                ll_C_1.setBackgroundResource(R.drawable.ques_selector_blue);
                ll_C_1.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                c = 1;
                break;
            case R.id.choosequesfrag_ll_D:
                reset();
                ll_D_1.setBackgroundResource(R.drawable.ques_selector_blue);
                ll_D_1.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                d = 1;
                break;
            case R.id.choosequesfrag_ll_E:
                reset();
                ll_E_1.setBackgroundResource(R.drawable.ques_selector_blue);
                ll_E_1.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                e = 1;
                break;
            case R.id.questionactivity_head_ok:
                checkOK();
                break;

        }
    }

    public void updateQuestion(TimuList timuList) {
        tv_ques.setText(timuList.getIndex() + ".问题");
        ll_A_4.setText("无");
        ll_B_4.setText("无");
        ll_C_4.setText("无");
        ll_D_4.setText("无");
        ll_E_4.setText("无");
        ll_jiexi2.setText("无");
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
        b = 0;
        d = 0;
        a = 0;
        c = 0;
        e = 0;
    }

    public void checkOK() {
        ll_B_1.setVisibility(View.GONE);
        ll_B_2.setVisibility(View.VISIBLE);
        ll_B_3.setVisibility(View.GONE);
        if (a == 1) {
            ll_A_1.setVisibility(View.GONE);
            ll_A_2.setVisibility(View.GONE);
            ll_A_3.setVisibility(View.VISIBLE);
        }
        if (e == 1) {
            ll_E_1.setVisibility(View.GONE);
            ll_E_2.setVisibility(View.GONE);
            ll_E_3.setVisibility(View.VISIBLE);
        }
        if (c == 1) {
            ll_C_1.setVisibility(View.GONE);
            ll_C_2.setVisibility(View.GONE);
            ll_C_3.setVisibility(View.VISIBLE);
        }
        if (d == 1) {
            ll_D_1.setVisibility(View.GONE);
            ll_D_2.setVisibility(View.GONE);
            ll_D_3.setVisibility(View.VISIBLE);
        }
        ll_jiexi.setVisibility(View.VISIBLE);
    }
}
