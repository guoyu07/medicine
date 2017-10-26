package com.yangs.medicine.fragment;

import android.content.Intent;
import android.database.Cursor;
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
import com.yangs.medicine.source.QuestionSource;

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
        mRecyclerView.setAdapter(mAdapter);
        topic_title_ll.setOnClickListener(this);
        topic_title_share.setOnClickListener(this);
        if ("".equals(APPlication.subject)) {
            topic_tv_id.setText("临床医学");
        } else {
            topic_tv_id.setText(APPlication.subject);
        }
    }

    private void initData() {
        list = new ArrayList<>();
        String sql = "select * from " + QuestionSource.SUBJECT_TABLE_NAME;
        int count = 0;
        Cursor cursor = null;
        try {
            cursor = APPlication.db.rawQuery(sql, null);
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        count++;
                        TopicList topic = new TopicList();
                        topic.setIndex(count + "");
                        topic.setName(cursor.getString(1));
                        topic.setLock("unlock");
                        topic.setRealIndex(cursor.getInt(0) + "");
                        list.add(topic);
                    } while (cursor.moveToNext());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
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
        bundle.putString("SP", list.get(position).getRealIndex());
        bundle.putString("Cha", "1");
        Intent intent = new Intent(getActivity(), QuestionActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
