package com.yangs.medicine.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yangs.medicine.R;
import com.yangs.medicine.adapter.DiscussAdapter;
import com.yangs.medicine.model.DiscussList;
import com.yangs.medicine.ui.FullyLinearLayoutManager;
import com.yangs.medicine.util.FitStatusBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangs on 2017/10/29 0029.
 */

public class DiscussActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, DiscussAdapter.OnThumbUpListener {
    private String realIndex;
    private RecyclerView recyclerView;
    private DiscussAdapter discussAdapter;
    private SwipeRefreshLayout srl;
    private List<DiscussList> lists;
    private ImageView head_back;
    private TextView tv_empty;
    private int star_code;
    private int thumb_click_position;
    private Intent back_intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.discussactivity_layout);
        initView();
        initData();
    }

    private void initView() {
        back_intent = new Intent();
        back_intent.putExtra("isChanged", "false");
        FitStatusBar.addStatusBarView(this);
        head_back = (ImageView) findViewById(R.id.discussactivity_head_back);
        recyclerView = (RecyclerView) findViewById(R.id.discussactivity_rv);
        srl = (SwipeRefreshLayout) findViewById(R.id.discussactivity_srl);
        tv_empty = (TextView) findViewById(R.id.discussactivity_tv_empty);
        lists = new ArrayList<>();
        srl.setRefreshing(true);
        srl.setOnRefreshListener(this);
        srl.setColorSchemeColors(Color.CYAN, Color.GREEN, ContextCompat.getColor(this,
                R.color.colorPrimary));
        discussAdapter = new DiscussAdapter(lists, DiscussActivity.this);
        recyclerView.setLayoutManager(new FullyLinearLayoutManager(DiscussActivity.this));
        recyclerView.setAdapter(discussAdapter);
        discussAdapter.setOnThumbUpListener(this);
        onRefresh();
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        realIndex = bundle.getString("realIndex");
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    discussAdapter.clear();
                    if (lists.size() > 0) {
                        DiscussList discussList = new DiscussList();
                        discussList.setType("head");
                        discussList.setStar(lists.size() + "");
                        lists.add(0, discussList);
                        recyclerView.setVisibility(View.VISIBLE);
                        tv_empty.setVisibility(View.GONE);
                        discussAdapter.addAll(lists);
                        discussAdapter.notifyDataSetChanged();
                    } else {
                        recyclerView.setVisibility(View.GONE);
                        tv_empty.setVisibility(View.VISIBLE);
                    }
                    srl.setRefreshing(false);
                    break;
                case 2:
                    switch (star_code) {
                        case 0:
                            int count = Integer.parseInt(lists.get(thumb_click_position).getStar());
                            count++;
                            lists.get(thumb_click_position).setStar(count + "");
                            lists.get(thumb_click_position).setIsYouStar("1");
                            discussAdapter.notifyDataSetChanged();
                            back_intent.putExtra("isChanged", "true");
                            setResult(0, back_intent);
                            break;
                        case 1:
                            count = Integer.parseInt(lists.get(thumb_click_position).getStar());
                            count--;
                            lists.get(thumb_click_position).setStar(count + "");
                            lists.get(thumb_click_position).setIsYouStar("0");
                            discussAdapter.notifyDataSetChanged();
                            back_intent.putExtra("isChanged", "true");
                            setResult(0, back_intent);
                            break;
                        case -2:
                            APPlication.showToast("网络出错!", 0);
                            break;
                    }
                    break;
            }
            return true;
        }
    });

    @Override
    public void onRefresh() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                lists = APPlication.questionSource.getDiscussList(realIndex, 0);
                handler.sendEmptyMessage(0);
            }
        }).start();
    }

    @Override
    public void onThumbUp(final int position) {
        final DiscussList discussList = lists.get(position);
        new Thread(new Runnable() {
            @Override
            public void run() {
                String action;
                if (discussList.getIsYouStar().equals("0"))
                    action = "addStar";
                else
                    action = "removeStar";
                star_code = APPlication.questionSource.discussStar(action, discussList.getId(),
                        APPlication.user);
                thumb_click_position = position;
                handler.sendEmptyMessage(2);
            }
        }).start();
    }
}
