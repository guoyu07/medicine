package com.yangs.medicine.fragment;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.yangs.medicine.R;
import com.yangs.medicine.activity.APPlication;
import com.yangs.medicine.adapter.BookMeAdapter;
import com.yangs.medicine.model.BookList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangs on 2017/10/5 0005.
 */

public class MeBookFragment extends LazyLoadFragment implements SwipeRefreshLayout.OnRefreshListener, BookMeAdapter.OnItemClickListener {
    private List<BookList> lists;
    private SwipeRefreshLayout srl;
    private TextView empty_tv;
    private RecyclerView rv;
    private BookMeAdapter bookMeAdapter;
    private View mlay;
    private int c = 0;

    @Override
    protected int setContentView() {
        return R.layout.mebookfrag_layout;
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
        mlay = getContentView();
        lists = new ArrayList<>();
        srl = (SwipeRefreshLayout) mlay.findViewById(R.id.mebookfrag_srl);
        empty_tv = (TextView) mlay.findViewById(R.id.mebookfrag_tv);
        rv = (RecyclerView) mlay.findViewById(R.id.mebookfrag_rv);
        bookMeAdapter = new BookMeAdapter(lists, getContext());
        bookMeAdapter.setOnItemClickListener(this);
        rv.setLayoutManager(new GridLayoutManager(getContext(), 3));
        rv.setAdapter(bookMeAdapter);
        srl.setOnRefreshListener(this);
        srl.setColorSchemeColors(Color.CYAN, Color.GREEN, ContextCompat.getColor(getContext(),
                R.color.colorPrimary));
        srl.post(new Runnable() {
            @Override
            public void run() {
                srl.setRefreshing(true);
                onRefresh();
            }
        });
    }

    @Override
    public void onRefresh() {
        srl.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (c == 0) {
                    empty_tv.setVisibility(View.GONE);
                    rv.setVisibility(View.VISIBLE);
                    initData();
                    bookMeAdapter.notifyDataSetChanged();
                    c = 1;
                } else if (c == 1) {
                    empty_tv.setVisibility(View.VISIBLE);
                    rv.setVisibility(View.GONE);
                }
                srl.setRefreshing(false);
            }
        }, 2000);
    }

    private void initData() {
        lists.clear();
        BookList bookList = new BookList();
        bookList.setName("医学理论学");
        bookList.setPicUrl("res://com.yangs.medicine/" + R.drawable.img_yixuelilun);
        lists.add(bookList);
        bookList = new BookList();
        bookList.setName("精神病学");
        bookList.setPicUrl("res://com.yangs.medicine/" + R.drawable.img_jinshenbingxue);
        lists.add(bookList);
        bookList = new BookList();
        bookList.setName("临床医学理论");
        bookList.setPicUrl("res://com.yangs.medicine/" + R.drawable.img_lincuangyixuelilun);
        lists.add(bookList);
        bookList = new BookList();
        bookList.setName("临床医学概要");
        bookList.setPicUrl("res://com.yangs.medicine/" + R.drawable.img_linchuanggaiyao);
        lists.add(bookList);
    }

    @Override
    public void onItemClickListener(View v, int position) {
        APPlication.showToast(lists.get(position).getName(), 0);
    }
}
