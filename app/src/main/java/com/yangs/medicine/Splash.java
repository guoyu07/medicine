package com.yangs.medicine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.yangs.medicine.activity.APPlication;
import com.yangs.medicine.activity.BaseActivity;
import com.yangs.medicine.activity.LoginActivity;
import com.yangs.medicine.activity.MainActivity;
import com.yangs.medicine.activity.SelectSubActivity;
import com.yangs.medicine.db.QuestionUtil;
import com.yangs.medicine.util.FitStatusBar;

/**
 * Created by yangs on 2017/10/3 0003.
 */

public class Splash extends AppCompatActivity {
    private TextView sp_jump_btn;
    private int code;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (APPlication.save.getBoolean("login_status", false)) {
            handler = new Handler();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    code = APPlication.questionSource.getSubject(APPlication.subject);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (code == 0) {
                                startActivity(new Intent(Splash.this, MainActivity.class));
                            } else {
                                APPlication.showToast("获取科目失败,请检查网络!", 1);
                            }
                            finish();
                        }
                    });
                }
            }).start();
        } else {
            startActivity(new Intent(Splash.this, LoginActivity.class));
            finish();
        }
//        setContentView(R.layout.splash_layout);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        sp_jump_btn = (TextView) findViewById(R.id.sp_jump_btn);
//        code = -1;
//        final CountDownTimer timer = new CountDownTimer(3200, 1000) {
//            int count = 3;
//
//            @Override
//            public void onTick(long millisUntilFinished) {
//                sp_jump_btn.setVisibility(View.VISIBLE);
//                sp_jump_btn.setText("跳过(" + millisUntilFinished / 1000 + "s)");
//                count--;
//            }
//
//            @Override
//            public void onFinish() {
//                sp_jump_btn.setText("跳过(" + 0 + "s)");
//                if (APPlication.save.getBoolean("login_status", false)) {
//                    if (code == 0) {
//                        startActivity(new Intent(Splash.this, MainActivity.class));
//                    } else {
//                        APPlication.showToast("获取科目失败,请检查网络!", 1);
//                    }
//                } else {
//                    startActivity(new Intent(Splash.this, LoginActivity.class));
//                }
//                finish();
//            }
//        };
//        timer.start();
//        if (APPlication.save.getBoolean("login_status", false)) {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    code = APPlication.questionSource.getSubject(APPlication.subject);
//                }
//            }).start();
//        }
    }
}
