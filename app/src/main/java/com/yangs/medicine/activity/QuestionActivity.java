package com.yangs.medicine.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.yangs.medicine.R;
import com.yangs.medicine.adapter.TimuDialogAdapter;
import com.yangs.medicine.model.TimuList;
import com.yangs.medicine.model.TimuDialogList;
import com.yangs.medicine.question.AskQuesFragment;
import com.yangs.medicine.question.BlankQuesFragment;
import com.yangs.medicine.question.CheckQuesFragment;
import com.yangs.medicine.question.ChooseQuesFragment;
import com.yangs.medicine.question.ExplainQuesFragment;
import com.yangs.medicine.util.FitStatusBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangs on 2017/9/24 0024.
 */

public class QuestionActivity extends BaseActivity implements View.OnClickListener {
    private Button bt_back;
    private Button bt_ok;
    private LinearLayout ll_chat;
    private ImageView iv_love;
    private ImageView iv_share;
    private ImageView iv_chat;
    private ImageView iv_timu;
    private ViewPager viewPager;
    private List<Fragment> frag_list;
    private ChooseQuesFragment chooseQuesFragment1;
    private CheckQuesFragment checkQuesFragment;
    private BlankQuesFragment blankQuesFragment;
    private ExplainQuesFragment explainQuesFragment;
    private AskQuesFragment askQuesFragment;
    private Dialog timuDialog;
    private DialogOnClickListener timuListener;
    private List<TimuDialogList> timudialog_list;
    private Boolean isInitOk = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.questionactivity_layout);
        FitStatusBar.addStatusBarView(this);
        initView();
        initData();
        viewPager.setOffscreenPageLimit(5);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return frag_list.get(position);
            }

            @Override
            public int getCount() {
                return frag_list.size();
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initData() {
        timudialog_list.clear();
        new Thread(new Runnable() {
            @Override
            public void run() {
                isInitOk = false;
                initChoose();
                initBlank();
                initCheck();
                isInitOk = true;
            }
        }).start();
    }

    /*
    初始化判断题
     */
    private void initCheck() {
        TimuDialogList timuDialogList = new TimuDialogList();
        timuDialogList.setType("判断题");
        timudialog_list.add(timuDialogList);
        List<TimuList> list_8 = new ArrayList<>();
        TimuList timuList;
        for (int i = 40; i < 48; i++) {
            timuList = new TimuList();
            timuList.setIndex(i + 1);
            timuList.setStatus("未做");
            list_8.add(timuList);
        }
        timuDialogList = new TimuDialogList();
        timuDialogList.setLists(list_8);
        timudialog_list.add(timuDialogList);
    }

    /*
    初始化填空题
     */
    private void initBlank() {
        TimuDialogList timuDialogList = new TimuDialogList();
        timuDialogList.setType("填空题");
        timudialog_list.add(timuDialogList);
        List<TimuList> list_8 = new ArrayList<>();
        TimuList timuList;
        for (int i = 20; i < 28; i++) {
            timuList = new TimuList();
            timuList.setIndex(i + 1);
            timuList.setStatus("未做");
            list_8.add(timuList);
        }
        timuDialogList = new TimuDialogList();
        timuDialogList.setLists(list_8);
        timudialog_list.add(timuDialogList);
        list_8 = new ArrayList<>();
        for (int i = 28; i < 36; i++) {
            timuList = new TimuList();
            timuList.setIndex(i + 1);
            timuList.setStatus("未做");
            list_8.add(timuList);
        }
        timuDialogList = new TimuDialogList();
        timuDialogList.setLists(list_8);
        timudialog_list.add(timuDialogList);
        timuDialogList = new TimuDialogList();
        timuDialogList.setLists(list_8);
        timudialog_list.add(timuDialogList);
        list_8 = new ArrayList<>();
        for (int i = 36; i < 40; i++) {
            timuList = new TimuList();
            timuList.setIndex(i + 1);
            timuList.setStatus("未做");
            list_8.add(timuList);
        }
        timuDialogList = new TimuDialogList();
        timuDialogList.setLists(list_8);
        timudialog_list.add(timuDialogList);
    }

    /*
    初始化选择题
     */
    private void initChoose() {
        TimuDialogList timuDialogList = new TimuDialogList();
        timuDialogList.setType("选择题");
        timudialog_list.add(timuDialogList);
        List<TimuList> list_8 = new ArrayList<>();
        TimuList timuList;
        for (int i = 0; i < 8; i++) {
            if (i == 3 || i == 5) {
                timuList = new TimuList();
                timuList.setIndex(i + 1);
                timuList.setStatus("错");
            } else {
                timuList = new TimuList();
                timuList.setIndex(i + 1);
                timuList.setStatus("对");
            }
            list_8.add(timuList);
        }
        timuDialogList = new TimuDialogList();
        timuDialogList.setLists(list_8);
        timudialog_list.add(timuDialogList);
        list_8 = new ArrayList<>();
        for (int i = 8; i < 16; i++) {
            if (i == 10) {
                timuList = new TimuList();
                timuList.setIndex(i + 1);
                timuList.setStatus("错");
            } else if (i >= 12) {
                timuList = new TimuList();
                timuList.setIndex(i + 1);
                timuList.setStatus("未做");
            } else {
                timuList = new TimuList();
                timuList.setIndex(i + 1);
                timuList.setStatus("对");
            }
            list_8.add(timuList);
        }
        timuDialogList = new TimuDialogList();
        timuDialogList.setLists(list_8);
        timudialog_list.add(timuDialogList);
        list_8 = new ArrayList<>();
        for (int i = 16; i < 20; i++) {
            timuList = new TimuList();
            timuList.setIndex(i + 1);
            timuList.setStatus("未做");
            list_8.add(timuList);
        }
        timuDialogList = new TimuDialogList();
        timuDialogList.setLists(list_8);
        timudialog_list.add(timuDialogList);
    }

    private void initView() {
        bt_back = (Button) findViewById(R.id.questionactivity_head_back);
        bt_ok = (Button) findViewById(R.id.questionactivity_head_ok);
        ll_chat = (LinearLayout) findViewById(R.id.questionactivity_ll);
        iv_love = (ImageView) findViewById(R.id.questionactivity_iv_love);
        iv_share = (ImageView) findViewById(R.id.questionactivity_iv_share);
        iv_chat = (ImageView) findViewById(R.id.questionactivity_iv_talk);
        iv_timu = (ImageView) findViewById(R.id.questionactivity_iv_timu);
        viewPager = (ViewPager) findViewById(R.id.questionactivity_vp);
        bt_back.setOnClickListener(this);
        bt_ok.setOnClickListener(this);
        ll_chat.setOnClickListener(this);
        iv_love.setOnClickListener(this);
        iv_share.setOnClickListener(this);
        iv_chat.setOnClickListener(this);
        iv_timu.setOnClickListener(this);
        frag_list = new ArrayList<>();
        timudialog_list = new ArrayList<>();
        chooseQuesFragment1 = new ChooseQuesFragment();
        frag_list.add(chooseQuesFragment1);
        checkQuesFragment = new CheckQuesFragment();
        frag_list.add(checkQuesFragment);
        blankQuesFragment = new BlankQuesFragment();
        frag_list.add(blankQuesFragment);
        explainQuesFragment = new ExplainQuesFragment();
        frag_list.add(explainQuesFragment);
        askQuesFragment = new AskQuesFragment();
        frag_list.add(askQuesFragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.questionactivity_menu, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.questionactivity_head_back:
                finish();
                break;
            case R.id.questionactivity_head_ok:
                if (viewPager.getCurrentItem() == 0)
                    chooseQuesFragment1.checkOK();
                if (viewPager.getCurrentItem() == 1)
                    checkQuesFragment.checkOK();
                break;
            case R.id.questionactivity_ll:
                APPlication.showToast("评论", 0);
                break;
            case R.id.questionactivity_iv_love:
                APPlication.showToast("收藏", 0);
                break;
            case R.id.questionactivity_iv_share:
                APPlication.showToast("分享", 0);
                break;
            case R.id.questionactivity_iv_talk:
                APPlication.showToast("讨论", 0);
                break;
            case R.id.questionactivity_iv_timu:
                if (!isInitOk) {
                    APPlication.showToast("题目为初始化完成", 0);
                    return;
                }
                if (timuDialog == null) {
                    timuListener = new DialogOnClickListener();
                    timuDialog = new Dialog(QuestionActivity.this, R.style.my_dialog);
                    LinearLayout timuDialog_ll = (LinearLayout) LayoutInflater.from(QuestionActivity.this)
                            .inflate(R.layout.timudialog_layout, null);
                    timuDialog_ll.findViewById(R.id.timudialog_iv_love).setOnClickListener(timuListener);
                    timuDialog_ll.findViewById(R.id.timudialog_iv_share).setOnClickListener(timuListener);
                    timuDialog_ll.findViewById(R.id.timudialog_iv_talk).setOnClickListener(timuListener);
                    timuDialog_ll.findViewById(R.id.timudialog_iv_timu).setOnClickListener(timuListener);
                    RecyclerView timu_rv = (RecyclerView) timuDialog_ll.findViewById(R.id.timudialog_rv);
                    TimuDialogAdapter timuDialogAdapter = new TimuDialogAdapter(timudialog_list, this);
                    timu_rv.setAdapter(timuDialogAdapter);
                    timu_rv.setLayoutManager(new LinearLayoutManager(this));
                    timuDialog.setContentView(timuDialog_ll);
                    Window dialogWindow = timuDialog.getWindow();
                    dialogWindow.setGravity(Gravity.BOTTOM);
                    dialogWindow.setWindowAnimations(R.style.dialogstyle);
                    WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                    lp.x = 0;
                    lp.y = -20;
                    lp.width = getResources().getDisplayMetrics().widthPixels;
                    timuDialog_ll.measure(0, 0);
                    lp.height = timuDialog_ll.getMeasuredHeight();
                    lp.alpha = 8f;
                    dialogWindow.setAttributes(lp);
                }
                timuDialog.show();
                break;
        }
    }

    public class DialogOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.timudialog_iv_love:
                    if (timuDialog != null)
                        timuDialog.cancel();
                    break;
                case R.id.timudialog_iv_share:
                    break;
                case R.id.timudialog_iv_talk:
                    break;
                case R.id.timudialog_iv_timu:
                    break;
            }
        }
    }
}
