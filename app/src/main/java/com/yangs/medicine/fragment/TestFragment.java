package com.yangs.medicine.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yangs.medicine.R;
import com.yangs.medicine.activity.APPlication;

/**
 * Created by yangs on 2017/10/18 0018.
 */

public class TestFragment extends Fragment {
    private TextView tv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        tv = new TextView(getContext());
        Bundle bundle = getArguments();
        tv.setText(bundle.getString("start"));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        );
        tv.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        tv.setTextSize(22);
        tv.setLayoutParams(lp);
        tv.setGravity(Gravity.CENTER);
        return tv;
    }

    public void setText(String s) {
        if (tv != null)
            tv.setText(s);
        else
            APPlication.showToast("tv is null", 0);
    }
}
