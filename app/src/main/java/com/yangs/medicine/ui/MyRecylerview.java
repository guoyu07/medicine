package com.yangs.medicine.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by yangs on 2017/10/29 0029.
 */

public class MyRecylerview extends RecyclerView {
    public MyRecylerview(Context context) {
        super(context);
    }

    public MyRecylerview(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRecylerview(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthSpec, expandSpec);
    }
}
