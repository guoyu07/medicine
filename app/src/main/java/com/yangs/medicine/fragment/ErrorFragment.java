package com.yangs.medicine.fragment;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;

import com.yangs.medicine.R;
import com.yangs.medicine.adapter.TitleBuilder;

/**
 * Created by yangs on 2017/9/23 0023.
 * 错题集
 */

public class ErrorFragment extends LazyLoadFragment implements View.OnClickListener {
    private View mLay;
    private TitleBuilder titleBuilder;
    private Button bt_1;
    private Button bt_2;
    private View v_1;
    private View v_2;

    @Override
    protected int setContentView() {
        return R.layout.errorfrag_layout;
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
        mLay = getContentView();
        titleBuilder = new TitleBuilder(mLay);
        titleBuilder.setLeftVisible(View.GONE).setTitleText("错题集");
        bt_1 = (Button) mLay.findViewById(R.id.error_bt_1);
        bt_2 = (Button) mLay.findViewById(R.id.error_bt_2);
        v_1 = mLay.findViewById(R.id.error_v_1);
        v_2 = mLay.findViewById(R.id.error_v_2);
        bt_1.setOnClickListener(this);
        bt_2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.error_bt_1:
                v_1.setVisibility(View.VISIBLE);
                v_2.setVisibility(View.INVISIBLE);
                bt_1.setTextColor(ContextCompat.getColor(getActivity(), R.color.error_white1));
                bt_2.setTextColor(ContextCompat.getColor(getActivity(), R.color.error_white2));
                break;
            case R.id.error_bt_2:
                v_1.setVisibility(View.INVISIBLE);
                v_2.setVisibility(View.VISIBLE);
                bt_1.setTextColor(ContextCompat.getColor(getActivity(), R.color.error_white2));
                bt_2.setTextColor(ContextCompat.getColor(getActivity(), R.color.error_white1));
                break;
        }
    }
}
