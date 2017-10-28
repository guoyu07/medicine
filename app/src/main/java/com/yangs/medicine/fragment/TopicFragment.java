package com.yangs.medicine.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private Map<String, List<TopicList>> map;

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
        map = new HashMap<>();
        String sql = "select * from " + QuestionSource.SUBJECT_TABLE_NAME;
        int index = 0;
        Cursor cursor = null;
        try {
            cursor = APPlication.db.rawQuery(sql, null);
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        index++;
                        TopicList topic = new TopicList();
                        topic.setName(cursor.getString(1));
                        topic.setLock("unlock");
                        topic.setType("big");
                        topic.setIndex(index + "");
                        topic.setClick(false);
                        topic.setSP(cursor.getInt(0) + "");
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
        if (list.get(position).getType().equals("big")) {
            if (list.get(position).getLock().equals("lock")) {
                APPlication.showToast("分享后解锁哦", 0);
                return;
            }
            if (list.get(position).getClick()) {
                list.get(position).setClick(false);
                list.removeAll(map.get(list.get(position).getName()));
            } else {
                List<TopicList> lists2 = new ArrayList<>();
                TopicList topic = new TopicList();
                topic.setIndex("第1章");
                topic.setName("认识生理学");
                topic.setType("small");
                topic.setSP(list.get(position).getSP());
                topic.setNumber("0/20");
                lists2.add(topic);
                topic = new TopicList();
                topic.setIndex("第2章");
                topic.setName("生理的作用");
                topic.setType("small");
                topic.setSP(list.get(position).getSP());
                topic.setNumber("0/15");
                lists2.add(topic);
                topic = new TopicList();
                topic.setIndex("* ");
                topic.setName("外科学重点题");
                topic.setType("small");
                topic.setSP(list.get(position).getSP());
                topic.setNumber("0/60");
                lists2.add(topic);
                map.put(list.get(position).getName(), lists2);
                list.addAll(position + 1, lists2);
                list.get(position).setClick(true);
            }
            mAdapter.notifyDataSetChanged();
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString("SP", list.get(position).getSP());
        bundle.putString("Cha", list.get(position).getIndex().replace("第", "")
                .replace("章", "").replace("* ", ""));
        bundle.putString("Name", list.get(position).getName());
        Intent intent = new Intent(getActivity(), QuestionActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
