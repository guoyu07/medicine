package com.yangs.medicine.fragment;

import android.view.View;

import com.yangs.medicine.R;
import com.yangs.medicine.adapter.TitleBuilder;

/**
 * Created by yangs on 2017/9/23 0023.
 * 教材
 */

public class BookFragment extends LazyLoadFragment {
    private View mLay;
    private TitleBuilder titleBuilder;

    @Override
    protected int setContentView() {
        return R.layout.bookfrag_layout;
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
        titleBuilder.setLeftVisible(View.GONE).setTitleText("教材");
    }
}
