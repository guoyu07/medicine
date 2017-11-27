package com.yangs.medicine.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yangs.medicine.R;
import com.yangs.medicine.source.QuestionSource;
import com.yangs.medicine.util.FitStatusBar;

/**
 * Created by yangs on 2017/11/26 0026.
 */

public class ReadActivity extends BaseActivity implements View.OnClickListener {
    private ImageView iv_back;
    private TextView head_title;
    private TextView tv_content;
    private Button bt_goto;
    private Bundle bundle;
    private String id;
    private ProgressDialog progressDialog;
    private String SP;
    private String Cha;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.readactivity_layout);
        FitStatusBar.addStatusBarView(this);
        initView();
        initData();
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    progressDialog.dismiss();
                    APPlication.showToast("网络出错", 0);
                    break;
            }
            return true;
        }
    });

    private void initData() {
        handler = new Handler();
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("加载中...");
        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                APPlication.questionSource.getRead(id, new QuestionSource.OnResponseCodeResultListener() {
                    @Override
                    public void onResponseResult(int code, String response) {
                        if (code == -1) {
                            handler.sendEmptyMessage(1);
                            return;
                        }
                        final JSONObject jsonObject = JSON.parseObject(response);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                tv_content.setText(jsonObject.getString("content"));
                                progressDialog.dismiss();
                                Cha = jsonObject.getString("Cha");
                                SP = jsonObject.getString("SP");
                                if (!Cha.equals("0"))
                                    bt_goto.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                });
            }
        }).start();
    }

    private void initView() {
        bundle = getIntent().getExtras();
        if (bundle == null)
            finish();
        id = bundle.getString("read_id");
        iv_back = (ImageView) findViewById(R.id.readactivity_head_back);
        head_title = (TextView) findViewById(R.id.readactivity_head_title);
        tv_content = (TextView) findViewById(R.id.readactivity_tv_content);
        bt_goto = (Button) findViewById(R.id.readactivity_bt_goto);
        tv_content.setMovementMethod(ScrollingMovementMethod.getInstance());
        bt_goto.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.readactivity_bt_goto:
                Bundle bundle = new Bundle();
                bundle.putString("type", "notError");
                bundle.putString("SP", SP);
                bundle.putString("Cha", Cha);
                String sql = "select * from " + QuestionSource.CHA_TABLE_NAME + " where SP="
                        + SP + " and Cha=" + Cha;
                Cursor cursor = APPlication.db.rawQuery(sql, null);
                if (cursor.getCount() > 0) {
                    if (cursor.moveToFirst()) {
                        bundle.putString("Name", cursor.getString(1));
                        Intent intent = new Intent(this, QuestionActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else {
                        APPlication.showToast("无法跳转!", 0);
                    }
                }
                break;
        }
    }
}
