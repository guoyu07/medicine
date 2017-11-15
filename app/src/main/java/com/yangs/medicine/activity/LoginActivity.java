package com.yangs.medicine.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yangs.medicine.R;
import com.yangs.medicine.util.FitStatusBar;

/**
 * Created by yangs on 2017/10/5 0005.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private EditText et_user;
    private EditText et_pwd;
    private TextView tv_forgetpwd;
    private TextView tv_register;
    private Button bt_login;
    private Button bt_nologin;
    private ImageView iv_qq;
    private ImageView iv_wechat;
    private String user;
    private String pwd;
    private ProgressDialog progressDialog;
    private int getSubjectCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginactivity_layout);
        initView();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 3);
        }
    }

    private void initView() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        FitStatusBar.addStatusBarView(this);
        et_user = (EditText) findViewById(R.id.loginactivity_et_user);
        et_pwd = (EditText) findViewById(R.id.loginactivity_et_pwd);
        tv_forgetpwd = (TextView) findViewById(R.id.loginactivity_tv_forgetpwd);
        tv_register = (TextView) findViewById(R.id.loginactivity_tv_register);
        bt_login = (Button) findViewById(R.id.loginactivity_bt_login);
        bt_nologin = (Button) findViewById(R.id.loginactivity_bt_nologin);
        iv_qq = (ImageView) findViewById(R.id.loginactivity_iv_qq);
        iv_wechat = (ImageView) findViewById(R.id.loginactivity_iv_wechat);
        tv_forgetpwd.setOnClickListener(this);
        tv_register.setOnClickListener(this);
        bt_login.setOnClickListener(this);
        bt_nologin.setOnClickListener(this);
        iv_qq.setOnClickListener(this);
        iv_wechat.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                Bundle bundle = data.getExtras();
                if (bundle != null && "success".equals(bundle.getString("result"))) {
                    String s1 = APPlication.save.getString("username", "");
                    String s2 = APPlication.save.getString("pwd", "");
                    et_user.setText(s1);
                    et_pwd.setText(s2);
                }
                break;
        }
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case -1:
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                    APPlication.showToast("用户名或密码错误", 0);
                    break;
                case -2:
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                    APPlication.showToast("网络出错", 0);
                    break;
                case 1:
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                    APPlication.showToast("登录成功", 0);
                    APPlication.user = APPlication.save.getString("username", "游客");
                    if (APPlication.grade.equals("") || APPlication.subject.equals("")) {
                        startActivity(new Intent(LoginActivity.this, SelectSubActivity.class));
                        finish();
                    } else {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                getSubjectCode = APPlication.questionSource.getSubject(APPlication.subject);
                                handler.sendEmptyMessage(2);
                            }
                        }).start();
                    }
                    break;
                case 2:
                    switch (getSubjectCode) {
                        case 0:
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            APPlication.save.edit().putBoolean("login_status", true).apply();
                            break;
                        case -1:
                            APPlication.showToast("解析题目数据时发生了错误,请反馈!", 1);
                            break;
                        case -2:
                            APPlication.showToast("获取科目失败,请检查网络!", 1);
                            break;
                    }
                    finish();
                    break;
            }
            return false;
        }
    });

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginactivity_tv_forgetpwd:
                APPlication.showToast("未开放,请等待!", 0);
                break;
            case R.id.loginactivity_tv_register:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.loginactivity_bt_login:
                user = et_user.getText().toString().trim();
                pwd = et_pwd.getText().toString().trim();
                if ("".equals(user)) {
                    et_user.setError("手机号为空");
                    return;
                }
                if ("".equals(pwd)) {
                    et_pwd.setError("密码为空");
                    return;
                }
                progressDialog.setMessage("登录中...");
                progressDialog.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String result = APPlication.questionSource.login(user, pwd);
                        String check = result.split(";")[0];
                        if (check.contains("登陆成功")) {
                            String grade = result.split(";")[2].replace(" ", "");
                            String subject = result.split(";")[3].replace(" ", "");
                            APPlication.grade = grade;
                            APPlication.subject = subject;
                            APPlication.save.edit().putString("grade", grade).putString("subject", subject)
                                    .putString("username", user).putString("pwd", pwd).apply();
                            handler.sendEmptyMessage(1);
                        } else if (check.contains("用户名或密码错误"))
                            handler.sendEmptyMessage(-1);
                        else
                            handler.sendEmptyMessage(-2);
                    }
                }).start();
                break;
            case R.id.loginactivity_bt_nologin:
                progressDialog.setMessage("登录中...");
                progressDialog.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                        APPlication.user = "游客";
                        startActivity(new Intent(LoginActivity.this, SelectSubActivity.class));
                        finish();
                    }
                }, 1000);
                break;
            case R.id.loginactivity_iv_qq:
                APPlication.showToast("目前未开放qq登录,请等待!", 0);
                break;
            case R.id.loginactivity_iv_wechat:
                APPlication.showToast("目前未开放微信登录,请等待!", 0);
                break;
        }
    }
}
