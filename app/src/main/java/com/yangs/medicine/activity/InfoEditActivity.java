package com.yangs.medicine.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yangs.medicine.R;
import com.yangs.medicine.source.QuestionSource;
import com.yangs.medicine.util.FitStatusBar;

/**
 * Created by yangs on 2017/12/1 0001.
 */

public class InfoEditActivity extends BaseActivity implements View.OnClickListener {
    private ImageView head_back;
    private LinearLayout ll_pic;
    private LinearLayout ll_phone;
    private LinearLayout ll_grade;
    private LinearLayout ll_subject;
    private TextView tv_phone;
    private TextView tv_grade;
    private TextView tv_subject;
    private Button bt_save;
    private Dialog grade_dialog;
    private Dialog subject_dialog;
    private String[] grade_s;
    private String[] subject_s;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.infoeditactivity_layout);
        FitStatusBar.addStatusBarView(this);
        initView();
    }

    private void initView() {
        head_back = (ImageView) findViewById(R.id.infoeditactivity_head_back);
        ll_pic = (LinearLayout) findViewById(R.id.infoeditactivity_ll_pic);
        ll_phone = (LinearLayout) findViewById(R.id.infoeditactivity_ll_phone);
        ll_grade = (LinearLayout) findViewById(R.id.infoeditactivity_ll_grade);
        ll_subject = (LinearLayout) findViewById(R.id.infoeditactivity_ll_subject);
        tv_phone = (TextView) findViewById(R.id.infoeditactivity_tv_phone);
        tv_grade = (TextView) findViewById(R.id.infoeditactivity_tv_grade);
        tv_subject = (TextView) findViewById(R.id.infoeditactivity_tv_subject);
        bt_save = (Button) findViewById(R.id.infoeditactivity_bt_save);
        bt_save.setOnClickListener(this);
        head_back.setOnClickListener(this);
        ll_pic.setOnClickListener(this);
        ll_phone.setOnClickListener(this);
        ll_grade.setOnClickListener(this);
        ll_subject.setOnClickListener(this);
        tv_phone.setText(APPlication.user);
        tv_grade.setText(APPlication.grade);
        tv_subject.setText(APPlication.subject);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.infoeditactivity_head_back:
                finish();
                break;
            case R.id.infoeditactivity_ll_pic:
                APPlication.showToast("此功能暂未开放!", 0);
                break;
            case R.id.infoeditactivity_ll_grade:
                if (grade_s == null) {
                    grade_s = new String[4];
                    grade_s[0] = "大一";
                    grade_s[1] = "大二";
                    grade_s[2] = "大三";
                    grade_s[3] = "大四";
                }
                int i = 0;
                for (; i < 4; i++) {
                    if (grade_s[i].equals(APPlication.grade))
                        break;
                }
                if (grade_dialog == null) {
                    grade_dialog = new AlertDialog.Builder(this).setTitle("选择年级")
                            .setSingleChoiceItems(grade_s, i, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    if (!grade_s[which].equals(APPlication.grade))
                                        bt_save.setVisibility(View.VISIBLE);
                                    tv_grade.setText(grade_s[which]);
                                }
                            }).create();
                }
                grade_dialog.show();
                break;
            case R.id.infoeditactivity_ll_subject:
                if (subject_s == null) {
                    subject_s = new String[6];
                    subject_s[0] = "临床医学";
                    subject_s[1] = "眼耳鼻喉";
                    subject_s[2] = "麻醉学";
                    subject_s[3] = "影像学";
                    subject_s[4] = "儿科学";
                    subject_s[5] = "法医学";
                }
                i = 0;
                for (; i < 4; i++) {
                    if (subject_s[i].equals(APPlication.subject))
                        break;
                }
                if (subject_dialog == null) {
                    subject_dialog = new AlertDialog.Builder(this).setTitle("选择专业")
                            .setSingleChoiceItems(subject_s, i, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    if (!subject_s[which].equals(APPlication.subject))
                                        bt_save.setVisibility(View.VISIBLE);
                                    tv_subject.setText(subject_s[which]);
                                }
                            }).create();
                }
                subject_dialog.show();
                break;
            case R.id.infoeditactivity_bt_save:
                if (progressDialog == null) {
                    progressDialog = new ProgressDialog(this);
                    progressDialog.setCancelable(false);
                    progressDialog.setMessage("加载中...");
                }
                progressDialog.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        APPlication.questionSource.editInfo(APPlication.user
                                , tv_grade.getText().toString(), tv_subject.getText().toString()
                                , new QuestionSource.OnResponseCodeResultListener() {
                                    @Override
                                    public void onResponseResult(int code, final String response) {
                                        if (code == -1) {
                                            APPlication.showToast("网络出错", 0);
                                            return;
                                        }
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                progressDialog.dismiss();
                                                if (response.equals("成功")) {
                                                    APPlication.showToast("修改成功,请重新登录!", 1);
                                                    APPlication.save.edit().putBoolean("login_status", false)
                                                            .putString("grade", "").putString("subject", "").apply();
                                                    setResult(1);
                                                    finish();
                                                } else
                                                    APPlication.showToast("保存失败", 0);
                                            }
                                        });
                                    }
                                });
                    }
                }).start();
                break;
        }
    }
}
