package com.yangs.medicine;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yangs.medicine.activity.APPlication;
import com.yangs.medicine.activity.BaseActivity;
import com.yangs.medicine.activity.LoginActivity;
import com.yangs.medicine.activity.MainActivity;
import com.yangs.medicine.activity.SelectSubActivity;
import com.yangs.medicine.db.QuestionUtil;
import com.yangs.medicine.util.FitStatusBar;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by yangs on 2017/10/3 0003.
 */

public class Splash extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout linearLayout;
    private View splash_v;
    private int count = 3;
    private TextView tv_skip;
    private Timer timer;
    private TimerTask task;
    private int code;
    private File bg_file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bg_file = new File(APPlication.getPath() + "/kp.jpg");
        if (APPlication.kp_status.equals("关") || !bg_file.exists()) {
            handler.sendEmptyMessage(0);
        } else {
            setContentView(R.layout.splash_layout);
            linearLayout = (LinearLayout) findViewById(R.id.splash_layout);
            splash_v = findViewById(R.id.splash_v);
            tv_skip = (TextView) findViewById(R.id.splash_tv_skip);
            try {
                Drawable drawable = Drawable.createFromPath(APPlication.getPath() + "/kp.jpg");
                linearLayout.setBackground(drawable);
            } catch (Exception e) {
                e.printStackTrace();
            }
            splash_v.setOnClickListener(this);
            tv_skip.setOnClickListener(this);
            task = new TimerTask() {
                @Override
                public void run() {
                    handler.sendEmptyMessage(1);
                }
            };
            timer = new Timer();
            timer.schedule(task, 100, 1000);
        }
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
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
                    break;
                case 1:
                    if (count <= 0) {
                        if (timer != null)
                            timer.cancel();
                        if (task != null)
                            task.cancel();
                        handler.sendEmptyMessage(0);
                    }
                    tv_skip.setVisibility(View.VISIBLE);
                    tv_skip.setText("跳过(" + count + "s)");
                    count--;
                    break;
            }
            return true;
        }
    });

    @Override
    protected void onStop() {
        super.onStop();
        if (timer != null)
            timer.cancel();
        if (task != null)
            task.cancel();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.splash_tv_skip:
                count = 0;
                break;
            case R.id.splash_v:
                count = 0;
                String s = APPlication.save.getString("kp_remark", "");
                if (!s.equals("") && !s.equals("无")) {
                    PackageManager packageManager = getPackageManager();
                    try {
                        packageManager.getPackageInfo("com.tencent.mobileqq", 0);
                        String url = "mqqwpa://im/chat?chat_type=wpa&uin=" + s;
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } catch (PackageManager.NameNotFoundException e) {
                        APPlication.showToast("无法拉起手机qq!", 0);
                    }
                }
                break;
        }
    }
}
