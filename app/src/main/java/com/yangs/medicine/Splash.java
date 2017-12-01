package com.yangs.medicine;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (APPlication.save.getBoolean("login_status", false)) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            code = APPlication.questionSource.getSubject(APPlication.subject
                                    , APPlication.grade);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    switch (code) {
                                        case 0:
                                            startActivity(new Intent(Splash.this, MainActivity.class));
                                            break;
                                        case -1:
                                            APPlication.showToast("解析题目数据时发生了错误,请反馈!", 1);
                                            break;
                                        case -2:
                                            APPlication.showToast("获取科目失败,请检查网络!", 1);
                                            break;
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
            }
        }, 2000);
    }
}
