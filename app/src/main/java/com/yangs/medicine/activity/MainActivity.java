package com.yangs.medicine.activity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yangs.medicine.R;
import com.yangs.medicine.fragment.BookFragment;
import com.yangs.medicine.fragment.ErrorFragment;
import com.yangs.medicine.fragment.MeFragment;
import com.yangs.medicine.fragment.TaskFragment;
import com.yangs.medicine.fragment.TopicFragment;
import com.yangs.medicine.source.QuestionSource;
import com.yangs.medicine.util.AsyncTaskUtil;
import com.yangs.medicine.util.FitStatusBar;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private FragmentManager fm;
    private LinearLayout tab_ly_1;
    private LinearLayout tab_ly_2;
    private LinearLayout tab_ly_3;
    private LinearLayout tab_ly_4;
    private LinearLayout tab_ly_5;
    private TextView tab_tv_1;
    private TextView tab_tv_2;
    private TextView tab_tv_3;
    private TextView tab_tv_4;
    private TextView tab_tv_5;
    private ImageView tab_iv_1;
    private ImageView tab_iv_2;
    private ImageView tab_iv_3;
    private ImageView tab_iv_4;
    private ImageView tab_iv_5;
    private TopicFragment topicFragment;
    private ErrorFragment errorFragment;
    private BookFragment bookFragment;
    private MeFragment meFragment;
    private TaskFragment taskFragment;
    private AsyncTaskUtil mDownloadAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity_layout);
        init();
        switchFragment(1);
        if (!APPlication.DEBUG) {
            checkUpdate();
        }
    }

    private void checkUpdate() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                APPlication.questionSource.getAd(new QuestionSource.OnResponseResultListener() {
                    @Override
                    public void onResponseResult(String response) {
                        final JSONObject json = JSON.parseObject(response);
                        if (json.getString("状态").equals("开")) {
                            if (!json.getString("id").equals(
                                    APPlication.save.getString("kp_id", ""))) {
                                Boolean status = APPlication.questionSource.downloadKpAd(
                                        json.getString("url"));
                                if (status)
                                    APPlication.save.edit().putString("kp_id", json.getString("id"))
                                            .putString("kp_status", "开")
                                            .putString("kp_remark", json.getString("remark"))
                                            .apply();
                            } else
                                APPlication.save.edit().putString("kp_status", "开").apply();
                        } else {
                            APPlication.save.edit().putString("kp_status", "关").apply();
                        }
                    }
                });
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                APPlication.questionSource.uploadRecord(APPlication.user, "启动",
                        "", "", "", "", "");
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                APPlication.questionSource.checkVersion(handler, new QuestionSource.OnResponseResultListener() {
                    @Override
                    public void onResponseResult(final String response) {
                        if (response == null || response.equals("最新版"))
                            return;
                        JSONObject jsonObject = JSON.parseObject(response);
                        final String url = "http://120.55.46.93:8080/app/medicine.apk";
                        new AlertDialog.Builder(MainActivity.this).setCancelable(false)
                                .setTitle("发现新版本").setMessage("" +
                                "更新: " + jsonObject.getString("detail") + "\n"
                                + "版本号: " + jsonObject.getString("version") + "\n"
                                + "大小: " + jsonObject.getString("size") + "\n"
                                + "时间: " + jsonObject.getString("time"))
                                .setPositiveButton("下载安装", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(MainActivity.this, "正在下载中...", Toast.LENGTH_SHORT).show();
                                        mDownloadAsyncTask = new AsyncTaskUtil(MainActivity.this, handler);
                                        mDownloadAsyncTask.execute(url, "medicine.apk");
                                    }
                                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                APPlication.showToast("新版本有更好的体验哦，推荐下载!", 0);
                            }
                        }).create().show();
                    }
                });
            }
        }).start();
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            return true;
        }
    });

    private void init() {
        FitStatusBar.addStatusBarView(this);
        fm = getSupportFragmentManager();
        tab_ly_1 = (LinearLayout) findViewById(R.id.tab_ly_1);
        tab_ly_2 = (LinearLayout) findViewById(R.id.tab_ly_2);
        tab_ly_3 = (LinearLayout) findViewById(R.id.tab_ly_3);
        tab_ly_4 = (LinearLayout) findViewById(R.id.tab_ly_4);
        tab_ly_5 = (LinearLayout) findViewById(R.id.tab_ly_5);
        tab_tv_1 = (TextView) tab_ly_1.findViewById(R.id.tab_tv_1);
        tab_tv_2 = (TextView) tab_ly_2.findViewById(R.id.tab_tv_2);
        tab_tv_3 = (TextView) tab_ly_3.findViewById(R.id.tab_tv_3);
        tab_tv_4 = (TextView) tab_ly_4.findViewById(R.id.tab_tv_4);
        tab_tv_5 = (TextView) tab_ly_5.findViewById(R.id.tab_tv_5);
        tab_iv_1 = (ImageView) tab_ly_1.findViewById(R.id.tab_iv_1);
        tab_iv_2 = (ImageView) tab_ly_2.findViewById(R.id.tab_iv_2);
        tab_iv_3 = (ImageView) tab_ly_3.findViewById(R.id.tab_iv_3);
        tab_iv_4 = (ImageView) tab_ly_4.findViewById(R.id.tab_iv_4);
        tab_iv_5 = (ImageView) tab_ly_5.findViewById(R.id.tab_iv_5);
        tab_ly_1.setOnClickListener(this);
        tab_ly_2.setOnClickListener(this);
        tab_ly_3.setOnClickListener(this);
        tab_ly_4.setOnClickListener(this);
        tab_ly_5.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tab_ly_1:
                switchFragment(1);
                break;
            case R.id.tab_ly_2:
                switchFragment(2);
                break;
            case R.id.tab_ly_3:
                switchFragment(3);
                break;
            case R.id.tab_ly_4:
                switchFragment(4);
                break;
            case R.id.tab_ly_5:
                switchFragment(5);
                break;
        }

    }

    private void switchFragment(int i) {
        FragmentTransaction transaction = fm.beginTransaction();
        if (topicFragment != null)
            transaction.hide(topicFragment);
        if (errorFragment != null)
            transaction.hide(errorFragment);
        if (bookFragment != null)
            transaction.hide(bookFragment);
        if (meFragment != null)
            transaction.hide(meFragment);
        if (taskFragment != null)
            transaction.hide(taskFragment);
        tab_tv_1.setTextColor(Color.rgb(162, 180, 202));
        tab_tv_2.setTextColor(Color.rgb(162, 180, 202));
        tab_tv_3.setTextColor(Color.rgb(162, 180, 202));
        tab_tv_4.setTextColor(Color.rgb(162, 180, 202));
        tab_tv_5.setTextColor(Color.rgb(162, 180, 202));
        tab_iv_1.setBackgroundResource(R.drawable.tabbar_icon_tiji_default);
        tab_iv_2.setBackgroundResource(R.drawable.tabbar_icon_cuotiji_default);
        tab_iv_3.setBackgroundResource(R.drawable.tabbar_icon_jiaocai_default);
        tab_iv_4.setBackgroundResource(R.drawable.tabbar_icon_renwu_default);
        tab_iv_5.setBackgroundResource(R.drawable.tabbar_icon_wode_default);
        switch (i) {
            case 1:
                if (topicFragment == null) {
                    topicFragment = new TopicFragment();
                    transaction.add(R.id.frame, topicFragment);
                } else {
                    transaction.show(topicFragment);
                }
                tab_tv_1.setTextColor(Color.rgb(81, 151, 252));
                tab_iv_1.setBackgroundResource(R.drawable.tabbar_icon_tiji_selected);
                break;
            case 2:
                if (errorFragment == null) {
                    errorFragment = new ErrorFragment();
                    transaction.add(R.id.frame, errorFragment);
                } else {
                    transaction.show(errorFragment);
                }
                tab_tv_2.setTextColor(Color.rgb(81, 151, 252));
                tab_iv_2.setBackgroundResource(R.drawable.tabbar_icon_cuotiji_selected);
                break;
            case 3:
                if (bookFragment == null) {
                    bookFragment = new BookFragment();
                    transaction.add(R.id.frame, bookFragment);
                } else {
                    transaction.show(bookFragment);
                }
                tab_tv_3.setTextColor(Color.rgb(81, 151, 252));
                tab_iv_3.setBackgroundResource(R.drawable.tabbar_icon_jiaocaii_selected);
                break;
            case 4:
                if (taskFragment == null) {
                    taskFragment = new TaskFragment();
                    transaction.add(R.id.frame, taskFragment);
                } else {
                    transaction.show(taskFragment);
                }
                tab_tv_4.setTextColor(Color.rgb(81, 151, 252));
                tab_iv_4.setBackgroundResource(R.drawable.tabbar_icon_renwu_selected);
                break;
            case 5:
                if (meFragment == null) {
                    meFragment = new MeFragment();
                    transaction.add(R.id.frame, meFragment);
                } else {
                    transaction.show(meFragment);
                }
                tab_tv_5.setTextColor(Color.rgb(81, 151, 252));
                tab_iv_5.setBackgroundResource(R.drawable.tabbar_icon_wode_selected);
                break;
        }
        transaction.commitAllowingStateLoss();
    }
}
