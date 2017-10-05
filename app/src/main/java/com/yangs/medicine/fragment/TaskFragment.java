package com.yangs.medicine.fragment;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;
import com.yangs.medicine.R;
import com.yangs.medicine.activity.APPlication;
import com.yangs.medicine.adapter.TaskAdapter;
import com.yangs.medicine.model.TaskList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangs on 2017/10/4 0004.
 */

public class TaskFragment extends LazyLoadFragment implements TaskAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    private View mLay;
    private SwipeRefreshLayout srl;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private List<TaskList> lists;
    private TaskAdapter taskAdapter;
    private TextView empty_tv;
    private int c = 0;
    private String type;

    @Override
    protected int setContentView() {
        return R.layout.taskfrag_layout;
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
        lists = new ArrayList<>();
        mLay = getContentView();
        fab = (FloatingActionButton) mLay.findViewById(R.id.task_fab);
        srl = (SwipeRefreshLayout) mLay.findViewById(R.id.task_srl);
        recyclerView = (RecyclerView) mLay.findViewById(R.id.task_rv);
        if (type == null) {
            fab.setVisibility(View.VISIBLE);
            fab.attachToRecyclerView(recyclerView);
            fab.setOnClickListener(this);
        }
        taskAdapter = new TaskAdapter(getContext(), lists, type);
        empty_tv = (TextView) mLay.findViewById(R.id.task_tv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(
                getActivity(), DividerItemDecoration.VERTICAL));
        taskAdapter.setOnItemClickListener(this);
        srl.setColorSchemeColors(Color.CYAN, Color.GREEN, ContextCompat.getColor(getContext(),
                R.color.colorPrimary));
        recyclerView.setAdapter(taskAdapter);
        srl.setOnRefreshListener(this);
        srl.post(new Runnable() {
            @Override
            public void run() {
                srl.setRefreshing(true);
                onRefresh();
            }
        });
    }


    private void initData() {
        lists.clear();
        TaskList taskList = new TaskList();
        if ("我接受的".equals(type)) {
            taskList.setPicUrl("res://com.yangs.medicine/" + R.drawable.img_lisi);
            taskList.setName("Lisi");
            taskList.setTime("2017.9.30 11:51");
            taskList.setContent("笔者与一个在苏州出差的朋友和一个天津的朋友一起连线。刚分配进游戏服务器，还没读完盘，客户端就崩溃掉了。");
            taskList.setMoney("20¥");
            taskList.setFinish(true);
            lists.add(taskList);
            return;
        }
        taskList.setPicUrl("res://com.yangs.medicine/" + R.drawable.img_zhangsan);
        taskList.setName("Zhangsan");
        taskList.setTime("2017.10.1 10:21");
        taskList.setContent("2017年中秋佳节到！中文国际频道将为您奉上一场“饕餮盛宴”，我们将继续推出《传奇中国节•中秋》与 #央视中秋晚会# 无缝衔接，6小时直播“大餐”等你来！");
        taskList.setMoney("25¥");
        taskList.setFinish(false);
        lists.add(taskList);
        if ("我发布的".equals(type))
            return;
        taskList = new TaskList();
        taskList.setPicUrl("res://com.yangs.medicine/" + R.drawable.img_lisi);
        taskList.setName("Lisi");
        taskList.setTime("2017.9.30 11:51");
        taskList.setContent("笔者与一个在苏州出差的朋友和一个天津的朋友一起连线。刚分配进游戏服务器，还没读完盘，客户端就崩溃掉了。");
        taskList.setMoney("20¥");
        taskList.setFinish(false);
        lists.add(taskList);
        taskList = new TaskList();
        taskList.setPicUrl("res://com.yangs.medicine/" + R.drawable.img_wangan);
        taskList.setName("Wangan");
        taskList.setTime("2017.10.1 10:21");
        taskList.setContent("然后我一边刷官微，看玩家不停的抱怨，不停的ma，一边不停的试啊试啊，用UU一下子换东南亚，一下子换美服，然并卵。");
        taskList.setMoney("45¥");
        taskList.setFinish(false);
        lists.add(taskList);
        taskList = new TaskList();
        taskList.setPicUrl("res://com.yangs.medicine/" + R.drawable.img_oldsong);
        taskList.setName("Old song");
        taskList.setTime("2017.10.1 10:21");
        taskList.setContent("然后搜索游戏，然后并没有搜索到我的STEAM的大逃杀，只好手动指定！");
        taskList.setMoney("20¥");
        taskList.setFinish(false);
        lists.add(taskList);
    }

    public void setTyep(String type) {
        this.type = type;
    }

    @Override
    public void onItemClick(View view, int position) {
        APPlication.showToast(lists.get(position).getContent(), 0);
    }

    @Override
    public void onRefresh() {
        srl.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (c == 0) {
                    initData();
                    empty_tv.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    taskAdapter.notifyDataSetChanged();
                    c = 1;
                } else if (c == 1) {
                    empty_tv.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    c = 0;
                }
                srl.setRefreshing(false);
            }
        }, 2000);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.task_fab:
                APPlication.showToast("发布任务", 0);
                break;
        }
    }
}
