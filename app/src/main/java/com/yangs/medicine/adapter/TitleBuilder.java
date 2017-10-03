package com.yangs.medicine.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yangs.medicine.R;

/**
 * Created by yangs on 2017/9/25 0025.
 */

public class TitleBuilder {
    private View view;
    private ImageView iv_left;
    private TextView tv_center;
    private ImageView iv_right;
    private Button bt_right;

    public TitleBuilder(Activity activity) {
        view = activity.findViewById(R.id.titlebar);
        iv_left = (ImageView) view.findViewById(R.id.titlebar_iv_left);
        tv_center = (TextView) view.findViewById(R.id.titlebar_tv_center);
        iv_right = (ImageView) view.findViewById(R.id.titlebar_iv_right);
        bt_right = (Button) view.findViewById(R.id.titlebar_bt_right);
    }

    public TitleBuilder(View v) {
        view = v.findViewById(R.id.titlebar);
        iv_left = (ImageView) view.findViewById(R.id.titlebar_iv_left);
        tv_center = (TextView) view.findViewById(R.id.titlebar_tv_center);
        iv_right = (ImageView) view.findViewById(R.id.titlebar_iv_right);
        bt_right = (Button) view.findViewById(R.id.titlebar_bt_right);
    }

    public TitleBuilder setTitleText(String s) {
        tv_center.setText(s);
        return this;
    }

    public TitleBuilder setRightIv(int id) {
        iv_right.setVisibility(View.VISIBLE);
        bt_right.setVisibility(View.GONE);
        iv_right.setBackgroundResource(id);
        return this;
    }

    public TitleBuilder setRightBtText(String s) {
        bt_right.setVisibility(View.VISIBLE);
        iv_right.setVisibility(View.GONE);
        bt_right.setText(s);
        return this;
    }

    public TitleBuilder setLeftVisible(int i) {
        iv_left.setVisibility(i);
        return this;
    }

    public TitleBuilder setLeftOnclickListener(View.OnClickListener onclickListener) {
        iv_left.setOnClickListener(onclickListener);
        return this;
    }

    public TitleBuilder setTitleOnClickListener(View.OnClickListener onClickListener) {
        tv_center.setOnClickListener(onClickListener);
        return this;
    }

    public TitleBuilder setRightIvOnclickListener(View.OnClickListener onclickListener) {
        iv_right.setOnClickListener(onclickListener);
        return this;
    }

    public TitleBuilder setRightBtOnClickListener(View.OnClickListener onClickListener) {
        bt_right.setOnClickListener(onClickListener);
        return this;
    }

}
