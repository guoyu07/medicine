package com.yangs.medicine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.yangs.medicine.activity.APPlication;
import com.yangs.medicine.activity.BaseActivity;
import com.yangs.medicine.activity.LoginActivity;
import com.yangs.medicine.activity.MainActivity;
import com.yangs.medicine.activity.SelectSubActivity;

/**
 * Created by yangs on 2017/10/3 0003.
 */

public class Splash extends BaseActivity implements View.OnClickListener {
    private TextView tv_time;
    private TextView tv_skip;
    private int code;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.splash_layout);
//        tv_time = (TextView) findViewById(R.id.splash_tv_time);
//        tv_skip = (TextView) findViewById(R.id.splash_tv_skip);
//        tv_skip.setOnClickListener(this);
        if (APPlication.save.getBoolean("login_status", false)) {
            setHandler();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    code = APPlication.questionSource.getSubject(APPlication.subject);
                    handler.sendEmptyMessage(0);
                }
            }).start();
        } else {
            startActivity(new Intent(Splash.this, LoginActivity.class));
        }
    }

    private void setHandler() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        if (code == 0) {
                            startActivity(new Intent(Splash.this, MainActivity.class));
                        } else {
                            APPlication.showToast("获取科目失败,请检查网络!", 1);
                        }
                        finish();
                        break;
                }
            }
        };
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.splash_tv_skip:
                CountDownTimer timer = new CountDownTimer(4000, 1000) {
                    int count = 3;

                    @Override
                    public void onTick(long millisUntilFinished) {
                        Log.i("TAG", count + "");
                        tv_time.setVisibility(View.VISIBLE);
                        tv_time.setText(count + "s");
                        count--;
                    }

                    @Override
                    public void onFinish() {
                    }
                };
                timer.start();
                break;
        }
    }
}
