package com.yangs.medicine.util;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.yangs.medicine.R;

/**
 * Created by yangs on 2017/10/8 0008.
 */

public class FitStatusBar {
    public static void addStatusBarView(Activity activity) {
        View view = new View(activity);
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        int statusBarHeight = activity.getResources().getDimensionPixelSize(resourceId);
        view.setBackgroundResource(R.drawable.bg_topic_top);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                statusBarHeight);
        ViewGroup decorView = (ViewGroup) activity.findViewById(android.R.id.content);
        decorView.addView(view, params);
    }
}
