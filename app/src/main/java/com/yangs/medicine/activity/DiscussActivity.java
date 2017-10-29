package com.yangs.medicine.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
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

public class DiscussActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    private String realIndex;
    private RecyclerView recyclerView;
    private DiscussAdapter discussAdapter;
    private SwipeRefreshLayout srl;
    private List<DiscussList> lists;
    private Button head_back;
    private TextView tv_empty;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.discussactivity_layout);
        initView();
        initData();
    }

    private void initView() {
        FitStatusBar.addStatusBarView(this);
        head_back = (Button) findViewById(R.id.discussactivity_head_back);
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
        onRefresh();
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        realIndex = bundle.getString("realIndex");
        lists.clear();
    }

    @Override
    public void onRefresh() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                DiscussList discussList = new DiscussList();
                discussList.setImgUrl("res://com.yangs.medicine/" + R.drawable.img_zhangsan);
                discussList.setUser("张三");
                discussList.setTime("2017-10-30 21:10");
                discussList.setStar(5);
                discussList.setContent("还不错!");
                lists.add(discussList);
                discussList = new DiscussList();
                discussList.setImgUrl("res://com.yangs.medicine/" + R.drawable.img_lisi);
                discussList.setUser("李四");
                discussList.setTime("2017-10-30 21:08");
                discussList.setStar(2);
                discussList.setContent("又做错了.");
                lists.add(discussList);
                discussList = new DiscussList();
                discussList.setImgUrl("res://com.yangs.medicine/" + R.drawable.img_lisi);
                discussList.setUser("李四");
                discussList.setTime("2017-10-30 21:08");
                discussList.setStar(2);
                discussList.setContent("又做错了.");
                lists.add(discussList);
                discussList = new DiscussList();
                discussList.setImgUrl("res://com.yangs.medicine/" + R.drawable.img_lisi);
                discussList.setUser("李四");
                discussList.setTime("2017-10-30 21:08");
                discussList.setStar(2);
                discussList.setContent("又做错了.");
                lists.add(discussList);
                discussList = new DiscussList();
                discussList.setImgUrl("res://com.yangs.medicine/" + R.drawable.img_lisi);
                discussList.setUser("李四");
                discussList.setTime("2017-10-30 21:08");
                discussList.setStar(2);
                discussList.setContent("又做错了.");
                lists.add(discussList);
                discussList = new DiscussList();
                discussList.setImgUrl("res://com.yangs.medicine/" + R.drawable.img_lisi);
                discussList.setUser("李四");
                discussList.setTime("2017-10-30 21:08");
                discussList.setStar(2);
                discussList.setContent("又做错了.");
                lists.add(discussList);
                discussAdapter.notifyDataSetChanged();
                srl.setRefreshing(false);
            }
        }, 2000);
    }
}
