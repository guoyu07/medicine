package com.yangs.medicine.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginactivity_layout);
        initView();
    }

    private void initView() {
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginactivity_tv_forgetpwd:
                APPlication.showToast("未开放,请等待!", 0);
                break;
            case R.id.loginactivity_tv_register:
                APPlication.showToast("目前未开放注册,请等待!", 0);
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
                APPlication.showToast("目前未开放登录,请等待!", 0);
                break;
            case R.id.loginactivity_bt_nologin:
                final ProgressDialog dialog = new ProgressDialog(this);
                dialog.setCancelable(false);
                dialog.setMessage("登录中...");
                dialog.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (dialog.isShowing())
                            dialog.dismiss();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }
                }, 1500);
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
