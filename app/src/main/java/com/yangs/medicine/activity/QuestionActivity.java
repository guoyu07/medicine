package com.yangs.medicine.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
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
import com.yangs.medicine.model.ChooseList;
import com.yangs.medicine.model.TimuList;
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

public class QuestionActivity extends BaseActivity implements View.OnClickListener, TimuDialogAdapter.TimuOnClickListener {
    private Button bt_back;
    private Button bt_ok;
    private LinearLayout ll_chat;
    private ImageView iv_love;
    private ImageView iv_share;
    private ImageView iv_chat;
    private ImageView iv_timu;
    private ViewPager viewPager;
    private List<Fragment> frag_list;
    private ChooseQuesFragment chooseQuesFragment;
    private Dialog timuDialog;
    private DialogOnClickListener timuListener;
    private List<TimuList> timuLists;
    private List<ChooseList> chooseLists;
    private Boolean isInitOk = false;
    private int offset = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.questionactivity_layout);
        FitStatusBar.addStatusBarView(this);
        initView();
        initData();
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                TimuList timuList = timuLists.get(position + offset);
                if (!"".equals(timuList.getType())) {   //title
                    offset++;
                    timuList = timuLists.get(position + offset);
                }
                ChooseQuesFragment chooseQuesFragment = new ChooseQuesFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("question", timuList);
                chooseQuesFragment.setArguments(bundle);
                return chooseQuesFragment;
            }

            @Override
            public int getCount() {
                return frag_list.size();
            }
        });
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
        timuLists = new ArrayList<>();
        chooseLists = new ArrayList<>();
    }

    private void initData() {
        timuLists.clear();
        new Thread(new Runnable() {
            @Override
            public void run() {
                isInitOk = false;
                initTitle();
                initChoose();
                isInitOk = true;
            }
        }).start();
    }

    private void initTitle() {
        TimuList timuList = new TimuList();
        timuList.setType("生理学 第一章");
        timuLists.add(timuList);
    }

    /*
    初始化选择题
     */
    private void initChoose() {
        TimuList timuList = new TimuList();
        timuList.setType("选择题");
        timuLists.add(timuList);
        for (int i = 1; i < 11; i++) {
            if (i == 5) {
                timuList = new TimuList();
                timuList.setIndex(i);
                timuList.setStatus("错");
            } else if (i >= 8) {
                timuList = new TimuList();
                timuList.setIndex(i);
                timuList.setStatus("未做");
            } else {
                timuList = new TimuList();
                timuList.setIndex(i);
                timuList.setStatus("对");
            }
            timuLists.add(timuList);
        }
        timuList = new TimuList();
        timuList.setType("填空题");
        timuLists.add(timuList);
        for (int i = 11; i < 21; i++) {
            timuList = new TimuList();
            timuList.setIndex(i);
            timuList.setStatus("未做");
            timuLists.add(timuList);
        }
        timuList = new TimuList();
        timuList.setType("判断题");
        timuLists.add(timuList);
        for (int i = 21; i < 31; i++) {
            timuList = new TimuList();
            timuList.setIndex(i);
            timuList.setStatus("未做");
            timuLists.add(timuList);
        }
        for (int i = 0; i < timuLists.size(); i++) {
            ChooseQuesFragment chooseQuesFragment = new ChooseQuesFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("question", timuLists.get(i));
            chooseQuesFragment.setArguments(bundle);
            frag_list.add(chooseQuesFragment);
        }
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
                    APPlication.showToast("题目未初始化完成", 0);
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
                    TimuDialogAdapter timuDialogAdapter = new TimuDialogAdapter(timuLists, this);
                    timuDialogAdapter.setTimuOnClickListener(this);
                    timu_rv.setAdapter(timuDialogAdapter);
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 8);
                    gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                        @Override
                        public int getSpanSize(int position) {
                            if (!"".equals(timuLists.get(position).getType()))
                                return 8;
                            else
                                return 1;
                        }
                    });
                    timu_rv.setLayoutManager(gridLayoutManager);
                    timuDialog.setContentView(timuDialog_ll);
                    Window dialogWindow = timuDialog.getWindow();
                    dialogWindow.setGravity(Gravity.BOTTOM);
                    dialogWindow.setWindowAnimations(R.style.dialogstyle);
                    WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                    lp.x = 0;
                    lp.y = 0;
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

    @Override
    public void timuOnClick(int index) {
        viewPager.setCurrentItem(index - 1, false);
        if (timuDialog != null) {
            timuDialog.cancel();
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
