package com.yangs.medicine.fragment;

import com.yangs.medicine.R;

/**
 * Created by yangs on 2017/9/24 0024.
 */

public class QuestionFragment extends LazyLoadFragment {
    @Override
    protected int setContentView() {
        return R.layout.questionfrag_layout;
    }

    @Override
    protected void lazyLoad() {
        if (isInit) {
            if (!isLoad) {

            }
        }

    }
}
