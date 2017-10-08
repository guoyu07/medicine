package com.yangs.medicine.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.yangs.medicine.R;
import com.yangs.medicine.activity.APPlication;
import com.yangs.medicine.activity.MeActivity;
import com.yangs.medicine.adapter.MeAdapter;
import com.yangs.medicine.adapter.TitleBuilder;
import com.yangs.medicine.model.MeList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangs on 2017/9/23 0023.
 * 我的
 */

public class MeFragment extends LazyLoadFragment implements MeAdapter.OnItemClickListener, View.OnClickListener {
    private SimpleDraweeView sv_head;
    private ImageView iv_edit;
    private TextView tv_name;
    private TextView tv_level;
    private Button bt_logout;
    private RecyclerView mrecyclerView;
    private MeAdapter meAdapter;
    private List<MeList> list;
    private View mLay;

    @Override
    protected int setContentView() {
        return R.layout.mefrag_layout;
    }

    @Override
    protected void lazyLoad() {
        if (isInit) {
            if (!isLoad) {
                initData();
                initView();
            }
        }

    }

    private void initData() {
        list = new ArrayList<>();
        MeList meList = new MeList();
        meList.setIcon(R.drawable.icon_shoucang);
        meList.setName("我的收藏");
        list.add(meList);
        meList = new MeList();
        meList.setIcon(R.drawable.icon_renwu);
        meList.setName("我的任务");
        list.add(meList);
        meList = new MeList();
        meList.setIcon(R.drawable.icon_xiaoxi);
        meList.setName("消息");
        list.add(meList);
        meList = new MeList();
        meList.setIcon(R.drawable.icon_yijian);
        meList.setName("反馈意见");
        list.add(meList);
        meList = new MeList();
        meList.setIcon(R.drawable.icon_guanyuwomen);
        meList.setName("关于我们");
        list.add(meList);
    }

    private void initView() {
        mLay = getContentView();
        sv_head = (SimpleDraweeView) mLay.findViewById(R.id.me_sv);
        iv_edit = (ImageView) mLay.findViewById(R.id.me_iv_edit);
        tv_name = (TextView) mLay.findViewById(R.id.me_tv_name);
        tv_level = (TextView) mLay.findViewById(R.id.me_tv_level);
        bt_logout = (Button) mLay.findViewById(R.id.me_bt_logout);
        mrecyclerView = (RecyclerView) mLay.findViewById(R.id.me_rv);
        meAdapter = new MeAdapter(getActivity(), list);
        meAdapter.setOnItemClickListener(this);
        bt_logout.setOnClickListener(this);
        iv_edit.setOnClickListener(this);
        mrecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mrecyclerView.addItemDecoration(new DividerItemDecoration(
                getActivity(), DividerItemDecoration.VERTICAL));
        mrecyclerView.setAdapter(meAdapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        Bundle bundle = new Bundle();
        Intent intent;
        switch (position) {
            case 0:
                bundle.putString("name", "我的收藏");
                intent = new Intent(getActivity(), MeActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case 1:
                bundle.putString("name", "我的任务");
                intent = new Intent(getActivity(), MeActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            default:
                APPlication.showToast(list.get(position).getName(), 0);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.me_bt_logout:
                APPlication.showToast("logout...", 0);
                break;
            case R.id.me_iv_edit:
                APPlication.showToast("edit...", 0);
                break;
        }
    }
}
