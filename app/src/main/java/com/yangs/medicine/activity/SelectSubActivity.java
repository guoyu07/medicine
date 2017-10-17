package com.yangs.medicine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yangs.medicine.R;

/**
 * Created by yangs on 2017/10/17 0017.
 */

public class SelectSubActivity extends BaseActivity implements View.OnClickListener {
    private TextView selectsubactivity_grade_tv1;
    private TextView selectsubactivity_grade_tv2;
    private TextView selectsubactivity_grade_tv3;
    private TextView selectsubactivity_grade_tv4;
    private View selectsubactivity_grade_v1;
    private View selectsubactivity_grade_v2;
    private View selectsubactivity_grade_v3;
    private TextView selectsubactivity_sub_tv1;
    private TextView selectsubactivity_sub_tv2;
    private TextView selectsubactivity_sub_tv3;
    private TextView selectsubactivity_sub_tv4;
    private TextView selectsubactivity_sub_tv5;
    private TextView selectsubactivity_sub_tv6;
    private Button selectsubactivity_bt;
    private String grade;
    private String sub;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectsubactivity_layout);
        initView();
    }

    private void initView() {
        selectsubactivity_grade_tv1 = (TextView) findViewById(R.id.selectsubactivity_grade_tv1);
        selectsubactivity_grade_tv2 = (TextView) findViewById(R.id.selectsubactivity_grade_tv2);
        selectsubactivity_grade_tv3 = (TextView) findViewById(R.id.selectsubactivity_grade_tv3);
        selectsubactivity_grade_tv4 = (TextView) findViewById(R.id.selectsubactivity_grade_tv4);
        selectsubactivity_sub_tv1 = (TextView) findViewById(R.id.selectsubactivity_sub_tv1);
        selectsubactivity_sub_tv2 = (TextView) findViewById(R.id.selectsubactivity_sub_tv2);
        selectsubactivity_sub_tv3 = (TextView) findViewById(R.id.selectsubactivity_sub_tv3);
        selectsubactivity_sub_tv4 = (TextView) findViewById(R.id.selectsubactivity_sub_tv4);
        selectsubactivity_sub_tv5 = (TextView) findViewById(R.id.selectsubactivity_sub_tv5);
        selectsubactivity_sub_tv6 = (TextView) findViewById(R.id.selectsubactivity_sub_tv6);
        selectsubactivity_grade_v1 = findViewById(R.id.selectsubactivity_grade_v1);
        selectsubactivity_grade_v2 = findViewById(R.id.selectsubactivity_grade_v2);
        selectsubactivity_grade_v3 = findViewById(R.id.selectsubactivity_grade_v3);
        selectsubactivity_bt = (Button) findViewById(R.id.selectsubactivity_bt);
        selectsubactivity_grade_tv1.setOnClickListener(this);
        selectsubactivity_grade_tv2.setOnClickListener(this);
        selectsubactivity_grade_tv3.setOnClickListener(this);
        selectsubactivity_grade_tv4.setOnClickListener(this);
        selectsubactivity_sub_tv1.setOnClickListener(this);
        selectsubactivity_sub_tv2.setOnClickListener(this);
        selectsubactivity_sub_tv3.setOnClickListener(this);
        selectsubactivity_sub_tv4.setOnClickListener(this);
        selectsubactivity_sub_tv5.setOnClickListener(this);
        selectsubactivity_sub_tv6.setOnClickListener(this);
        selectsubactivity_bt.setOnClickListener(this);
    }

    private void resetGradeView() {
        selectsubactivity_grade_v1.setVisibility(View.VISIBLE);
        selectsubactivity_grade_v2.setVisibility(View.VISIBLE);
        selectsubactivity_grade_v3.setVisibility(View.VISIBLE);
        selectsubactivity_grade_tv1.setBackgroundResource(R.drawable.selector_5dp_selectsub_tv1);
        selectsubactivity_grade_tv2.setBackgroundResource(R.drawable.selector_5dp_selectsub_tv1);
        selectsubactivity_grade_tv3.setBackgroundResource(R.drawable.selector_5dp_selectsub_tv1);
        selectsubactivity_grade_tv4.setBackgroundResource(R.drawable.selector_5dp_selectsub_tv1);
    }

    private void resetSubView() {
        selectsubactivity_sub_tv1.setBackgroundResource(R.drawable.selector_5dp_selectsub_tv2);
        selectsubactivity_sub_tv2.setBackgroundResource(R.drawable.selector_5dp_selectsub_tv2);
        selectsubactivity_sub_tv3.setBackgroundResource(R.drawable.selector_5dp_selectsub_tv2);
        selectsubactivity_sub_tv4.setBackgroundResource(R.drawable.selector_5dp_selectsub_tv2);
        selectsubactivity_sub_tv5.setBackgroundResource(R.drawable.selector_5dp_selectsub_tv2);
        selectsubactivity_sub_tv6.setBackgroundResource(R.drawable.selector_5dp_selectsub_tv2);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.selectsubactivity_grade_tv1:
                resetGradeView();
                selectsubactivity_grade_v1.setVisibility(View.GONE);
                selectsubactivity_grade_tv1.setBackgroundResource(R.drawable.selector_5dp_selectsub_tv1_selected);
                grade = "大一";
                break;
            case R.id.selectsubactivity_grade_tv2:
                resetGradeView();
                selectsubactivity_grade_v1.setVisibility(View.GONE);
                selectsubactivity_grade_v2.setVisibility(View.GONE);
                selectsubactivity_grade_tv2.setBackgroundResource(R.drawable.selector_5dp_selectsub_tv1_selected);
                grade = "大二";
                break;
            case R.id.selectsubactivity_grade_tv3:
                resetGradeView();
                selectsubactivity_grade_v2.setVisibility(View.GONE);
                selectsubactivity_grade_v3.setVisibility(View.GONE);
                selectsubactivity_grade_tv3.setBackgroundResource(R.drawable.selector_5dp_selectsub_tv1_selected);
                grade = "大三";
                break;
            case R.id.selectsubactivity_grade_tv4:
                resetGradeView();
                selectsubactivity_grade_v3.setVisibility(View.GONE);
                selectsubactivity_grade_tv4.setBackgroundResource(R.drawable.selector_5dp_selectsub_tv1_selected);
                grade = "大四";
                break;
            case R.id.selectsubactivity_sub_tv1:
                resetSubView();
                selectsubactivity_sub_tv1.setBackgroundResource(R.drawable.selector_5dp_selectsub_tv2_selected);
                sub = "临床医学";
                break;
            case R.id.selectsubactivity_sub_tv2:
                resetSubView();
                selectsubactivity_sub_tv2.setBackgroundResource(R.drawable.selector_5dp_selectsub_tv2_selected);
                sub = "眼耳鼻喉";
                break;
            case R.id.selectsubactivity_sub_tv3:
                resetSubView();
                selectsubactivity_sub_tv3.setBackgroundResource(R.drawable.selector_5dp_selectsub_tv2_selected);
                sub = "麻醉学";
                break;
            case R.id.selectsubactivity_sub_tv4:
                resetSubView();
                selectsubactivity_sub_tv4.setBackgroundResource(R.drawable.selector_5dp_selectsub_tv2_selected);
                sub = "影像学";
                break;
            case R.id.selectsubactivity_sub_tv5:
                resetSubView();
                selectsubactivity_sub_tv5.setBackgroundResource(R.drawable.selector_5dp_selectsub_tv2_selected);
                sub = "儿科学";
                break;
            case R.id.selectsubactivity_sub_tv6:
                resetSubView();
                selectsubactivity_sub_tv6.setBackgroundResource(R.drawable.selector_5dp_selectsub_tv2_selected);
                sub = "法医学";
                break;
            case R.id.selectsubactivity_bt:
                if (grade == null) {
                    APPlication.showToast("请选择年级", 0);
                    return;
                }
                if (sub == null) {
                    APPlication.showToast("请选择专业", 0);
                    return;
                }
                APPlication.save.edit().putString("grade", grade)
                        .putString("subject", sub).apply();
                APPlication.grade = grade;
                APPlication.subject = sub;
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
        }
    }
}
