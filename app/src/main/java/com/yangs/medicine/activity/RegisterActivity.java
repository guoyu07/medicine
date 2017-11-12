package com.yangs.medicine.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.aliyuncs.exceptions.ClientException;
import com.yangs.medicine.R;
import com.yangs.medicine.util.FitStatusBar;
import com.yangs.medicine.util.SmsUtil;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by yangs on 2017/11/8 0008.
 */

public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_step_1;
    private TextView tv_step_2;
    private TextView tv_step_3;
    private EditText et_text;
    private Button bt_sendagain;
    private Button bt_submit;
    private int current_step;
    private String phone;
    private String code;
    private String pwd;
    private String real_code;
    private ProgressDialog progressDialog;
    private Timer timer;
    private TimerTask task;
    private int code_time_count;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registeractivity_layout);
        FitStatusBar.addStatusBarView(this);
        initView();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (timer != null)
            timer.cancel();
        if (task != null)
            task.cancel();
    }

    private void initView() {
        code_time_count = 60;
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        tv_step_1 = (TextView) findViewById(R.id.registeractivity_info_step1);
        tv_step_2 = (TextView) findViewById(R.id.registeractivity_info_step2);
        tv_step_3 = (TextView) findViewById(R.id.registeractivity_info_step3);
        et_text = (EditText) findViewById(R.id.registeractivity_et_text);
        bt_sendagain = (Button) findViewById(R.id.registeractivity_bt_sendagain);
        bt_submit = (Button) findViewById(R.id.registeractivity_bt_submit);
        bt_sendagain.setOnClickListener(this);
        bt_submit.setOnClickListener(this);
        bt_submit.setClickable(false);
        et_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s))
                    bt_submit.setClickable(false);
                else
                    bt_submit.setClickable(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        goToStep(1);
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case -1:
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                    APPlication.showToast("发送验证码失败!", 0);
                    break;
                case 1:
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                    APPlication.showToast("已发送", 0);
                    goToStep(2);
                    break;
                case 2:
                    bt_sendagain.setText("重新获取(" + code_time_count + "s)");
                    if (code_time_count > 0)
                        code_time_count--;
                    else {
                        timer.cancel();
                        task.cancel();
                        bt_sendagain.setText("发送验证码");
                        String chars = "0123456789";
                        char[] rands = new char[6];
                        for (int i = 0; i < 6; i++) {
                            int rand = (int) (Math.random() * 10);
                            rands[i] = chars.charAt(rand);
                        }
                        real_code = new String(rands);
                        if (APPlication.DEBUG)
                            Log.i("注册验证码: ", real_code);
                    }
                    break;
            }
            return false;
        }
    });

    private void goToStep(int step) {
        switch (step) {
            case 1:
                current_step = 1;
                tv_step_1.setTextColor(ContextCompat.getColor(this, R.color.blue3));
                et_text.setHint("请输入手机号");
                et_text.setInputType(EditorInfo.TYPE_CLASS_PHONE);
                bt_sendagain.setVisibility(View.GONE);
                bt_submit.setText("获取验证码");
                String chars = "0123456789";
                char[] rands = new char[6];
                for (int i = 0; i < 6; i++) {
                    int rand = (int) (Math.random() * 10);
                    rands[i] = chars.charAt(rand);
                }
                real_code = new String(rands);
                if (APPlication.DEBUG)
                    Log.i("注册验证码: ", real_code);
                break;
            case 2:
                current_step = 2;
                tv_step_2.setTextColor(ContextCompat.getColor(this, R.color.blue3));
                et_text.setText(null);
                et_text.setHint("请输入验证码");
                et_text.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
                bt_sendagain.setVisibility(View.VISIBLE);
                bt_submit.setText("提交验证码");
                task = new TimerTask() {
                    @Override
                    public void run() {
                        handler.sendEmptyMessage(2);
                    }
                };
                timer = new Timer();
                timer.schedule(task, 200, 1000);
                break;
            case 3:
                current_step = 3;
                tv_step_3.setTextColor(ContextCompat.getColor(this, R.color.blue3));
                et_text.setText(null);
                et_text.setHint("请输入密码");
                et_text.setInputType(EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
                bt_sendagain.setVisibility(View.GONE);
                bt_submit.setText("注册");
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.registeractivity_bt_submit:
                switch (current_step) {
                    case 1:
                        phone = et_text.getText().toString().trim();
                        progressDialog.setMessage("发送验证码中...");
                        progressDialog.show();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    String result = SmsUtil.sendRegister(phone, real_code);
                                    if ("OK".equals(result)) {
                                        handler.sendEmptyMessage(1);
                                        return;
                                    }
                                } catch (ClientException e) {
                                    e.printStackTrace();
                                    APPlication.showToast(e.getMessage(), 1);
                                }
                                handler.sendEmptyMessage(-1);
                            }
                        }).start();
                        break;
                    case 2:
                        code = et_text.getText().toString().trim();
                        if (code.equals(real_code))
                            goToStep(3);
                        else
                            APPlication.showToast("验证码错误", 0);
                        break;
                    case 3:
                        pwd = et_text.getText().toString().trim();
                        APPlication.showToast(pwd, 0);
                        break;
                }
                break;
            case R.id.registeractivity_bt_sendagain:
                if (code_time_count != 0)
                    return;
                code_time_count = 60;
                progressDialog.setMessage("发送验证码中...");
                progressDialog.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String result = SmsUtil.sendRegister(phone, real_code);
                            if ("OK".equals(result)) {
                                handler.sendEmptyMessage(1);
                                return;
                            }
                        } catch (ClientException e) {
                            e.printStackTrace();
                            APPlication.showToast(e.getMessage(), 1);
                        }
                        handler.sendEmptyMessage(-1);
                    }
                }).start();
                break;
        }
    }
}
