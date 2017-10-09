package com.yangs.medicine.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yangs.medicine.R;
import com.yangs.medicine.activity.APPlication;
import com.yangs.medicine.activity.QuestionActivity;
import com.yangs.medicine.activity.SearchActivity;
import com.yangs.medicine.adapter.TopicAdapter;
import com.yangs.medicine.model.TopicList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangs on 2017/9/23 0023.
 * 题集
 */

public class TopicFragment extends LazyLoadFragment implements View.OnClickListener, TopicAdapter.OnItemClickListener {
    private View mLay;
    private LinearLayout topic_title_ll;
    private ImageView topic_title_share;
    private TextView topic_tv_id;
    private RecyclerView mRecyclerView;
    private TopicAdapter mAdapter;
    private List<TopicList> list;
    private Toolbar toolbar;

    @Override
    protected int setContentView() {
        return R.layout.topicfrag_layout;
    }

    @Override
    protected void lazyLoad() {
        if (isInit) {
            if (!isLoad) {
                initView();
            }
        }

    }

    private void initView() {
        initData();
        mLay = getContentView();
        toolbar = (Toolbar) mLay.findViewById(R.id.topic_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        topic_title_ll = (LinearLayout) mLay.findViewById(R.id.topic_title_ll);
        topic_title_share = (ImageView) mLay.findViewById(R.id.topic_title_share);
        topic_tv_id = (TextView) mLay.findViewById(R.id.topic_tv_id);
        mRecyclerView = (RecyclerView) mLay.findViewById(R.id.topic_rv);
        mAdapter = new TopicAdapter(list, getActivity());
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(
//                getActivity(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);
        topic_title_ll.setOnClickListener(this);
        topic_title_share.setOnClickListener(this);
    }

    private void initData() {
        list = new ArrayList<>();
        TopicList topic = new TopicList();
        topic.setIndex(1 + "");
        topic.setName("生理学");
        topic.setLock("unlock");
        list.add(topic);
        topic = new TopicList();
        topic.setIndex(2 + "");
        topic.setName("病理学");
        topic.setLock("unlock");
        list.add(topic);
        topic = new TopicList();
        topic.setIndex(3 + "");
        topic.setName("生物化学");
        topic.setLock("unlock");
        list.add(topic);
        topic = new TopicList();
        topic.setIndex(4 + "");
        topic.setName("内外妇儿");
        topic.setLock("lock");
        topic.setOperation("qq");
        list.add(topic);
        topic = new TopicList();
        topic.setIndex(5 + "");
        topic.setName("系统解剖学");
        topic.setLock("unlock");
        list.add(topic);
        topic = new TopicList();
        topic.setIndex(6 + "");
        topic.setName("病理学");
        topic.setLock("unlock");
        list.add(topic);
        topic = new TopicList();
        topic.setIndex(7 + "");
        topic.setName("诊断学");
        topic.setLock("unlock");
        list.add(topic);
        topic = new TopicList();
        topic.setIndex(8 + "");
        topic.setName("临床心理学");
        topic.setLock("lock");
        topic.setOperation("wechat");
        list.add(topic);
        topic = new TopicList();
        topic.setIndex(9 + "");
        topic.setName("免疫学");
        topic.setLock("unlock");
        list.add(topic);
        topic = new TopicList();
        topic.setIndex(10 + "");
        topic.setName("组织胚胎学");
        topic.setLock("unlock");
        list.add(topic);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.topic_title_ll:
                startActivity(new Intent(getActivity(), SearchActivity.class));
                break;
            case R.id.topic_title_share:
                APPlication.showToast("share...", 0);
                break;
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        if (list.get(position).getLock().equals("lock")) {
            APPlication.showToast("分享后解锁哦", 0);
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putInt("index", position);     //科目类别
        Intent intent = new Intent(getActivity(), QuestionActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
