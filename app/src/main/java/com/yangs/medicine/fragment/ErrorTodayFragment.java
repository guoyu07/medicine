package com.yangs.medicine.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yangs.medicine.R;

/**
 * Created by yangs on 2017/10/3 0003.
 */

public class ErrorTodayFragment extends LazyLoadFragment {
    private View mLay;
    private RecyclerView mrecyclerView;

    @Override
    protected int setContentView() {
        return R.layout.errortodayfrag_layout;
    }

    @Override
    protected void lazyLoad() {
        if (isInit) {
            if (!isLoad) {
                mLay = getContentView();
                mrecyclerView = (RecyclerView) mLay.findViewById(R.id.errortoday_frag_rv);
            }
        }
    }
}
