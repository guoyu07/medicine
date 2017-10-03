package com.yangs.medicine.fragment;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.yangs.medicine.R;
import com.yangs.medicine.activity.APPlication;
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
    private TextView tv_name;
    private TextView tv_level;
    private Button bt_logout;
    private RecyclerView mrecyclerView;
    private MeAdapter meAdapter;
    private List<MeList> list;
    private View mLay;
    private TitleBuilder titleBuilder;

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
        meList.setIcon(R.drawable.ic_star);
        meList.setName("我的收藏");
        list.add(meList);
        meList = new MeList();
        meList.setIcon(R.drawable.ic_ring);
        meList.setName("消息");
        list.add(meList);
        meList = new MeList();
        meList.setIcon(R.drawable.ic_chat_gray);
        meList.setName("反馈意见");
        list.add(meList);
        meList = new MeList();
        meList.setIcon(R.drawable.ic_friend);
        meList.setName("关于我们");
        list.add(meList);
    }

    private void initView() {
        mLay = getContentView();
        titleBuilder = new TitleBuilder(mLay);
        titleBuilder.setLeftVisible(View.GONE).setTitleText("我的").setRightIv(R.drawable.ic_edit_white)
                .setRightIvOnclickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        APPlication.showToast("编辑...", 0);
                    }
                });
        sv_head = (SimpleDraweeView) mLay.findViewById(R.id.me_sv);
        tv_name = (TextView) mLay.findViewById(R.id.me_name);
        tv_level = (TextView) mLay.findViewById(R.id.me_level);
        bt_logout = (Button) mLay.findViewById(R.id.me_bt);
        mrecyclerView = (RecyclerView) mLay.findViewById(R.id.me_rv);
        meAdapter = new MeAdapter(getActivity(), list);
        meAdapter.setOnItemClickListener(this);
        bt_logout.setOnClickListener(this);
        mrecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mrecyclerView.addItemDecoration(new DividerItemDecoration(
                getActivity(), DividerItemDecoration.VERTICAL));
        mrecyclerView.setAdapter(meAdapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        APPlication.showToast(list.get(position).getName(), 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.me_bt:
                APPlication.showToast("logout...", 0);
                break;
        }
    }
}
