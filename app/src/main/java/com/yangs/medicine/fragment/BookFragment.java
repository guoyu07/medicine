package com.yangs.medicine.fragment;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yangs.medicine.R;
import com.yangs.medicine.activity.APPlication;
import com.yangs.medicine.adapter.BookAdapter;
import com.yangs.medicine.adapter.TitleBuilder;
import com.yangs.medicine.model.BookList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangs on 2017/9/23 0023.
 * 教材
 */

public class BookFragment extends LazyLoadFragment implements SwipeRefreshLayout.OnRefreshListener, BookAdapter.OnItemClickListener, View.OnClickListener {
    private View mLay;
    private SwipeRefreshLayout srl;
    private TextView empty_tv;
    private RecyclerView recyclerView;
    private List<BookList> list;
    private BookAdapter bookAdapter;
    private ImageView iv_search;
    private int i = 0;

    @Override
    protected int setContentView() {
        return R.layout.bookfrag_layout;
    }

    @Override
    protected void lazyLoad() {
        if (isInit) {
            if (!isLoad) {
                initView();
            }
        }

    }

    private void initData() {
        list.clear();
        BookList bookList = new BookList();
        bookList.setName("临床医学概要");
        bookList.setTime("2017.10.1  更新");
        bookList.setSummary("对医学检验专业和药剂专业来说是唯一联系医学基础课与专业课的中介课程。随着卫生事业的发展和医学教育改革的深入，按照培养“实用型”人才模式，在编写过程中特别注意以下方面：在传授知识的同时，注意实践能力的培养");
        bookList.setCount("2.5K");
        bookList.setPicUrl("res://com.yangs.medicine/" + R.drawable.img_linchuanggaiyao);
        list.add(bookList);
        bookList = new BookList();
        bookList.setName("医学理论学");
        bookList.setTime("2017.9.30  更新");
        bookList.setSummary("运用一般伦理学原则解决医疗卫生实践和医学发展过程中的医学道德问题和医学道德现象的学科，它是医学的一个重要组成部分，又是伦理学的一个分支。");
        bookList.setCount("3.5K");
        bookList.setPicUrl("res://com.yangs.medicine/" + R.drawable.img_yixuelilun);
        list.add(bookList);
        bookList = new BookList();
        bookList.setName("精神病学");
        bookList.setTime("2017.9.29  更新");
        bookList.setSummary("现代医学科学的一个重要组成分支，它主要研究精神障碍的病因、发病机理、病象和临床规律以及预防、诊断、治疗和康复等有关问题。现代精神病学不单涉及各种精神病、神经症、心身疾病或伴随躯体疾病的精神障碍的诊治。");
        bookList.setCount("2.2K");
        bookList.setPicUrl("res://com.yangs.medicine/" + R.drawable.img_jinshenbingxue);
        list.add(bookList);
        bookList = new BookList();
        bookList.setName("临床医学理论");
        bookList.setTime("2017.9.28  更新");
        bookList.setSummary("一门实践性很强的应用科学专业，致力于培养具备基础医学、临床医学的基本理论和医疗预防的基本技能；能在医疗卫生单位、医学科研等部门从事医疗及预防、医学科研等方面工作的医学高级专门人才");
        bookList.setCount("2.5K");
        bookList.setPicUrl("res://com.yangs.medicine/" + R.drawable.img_lincuangyixuelilun);
        list.add(bookList);
    }

    private void initView() {
        mLay = getContentView();
        iv_search = (ImageView) mLay.findViewById(R.id.book_iv);
        srl = (SwipeRefreshLayout) mLay.findViewById(R.id.book_srl);
        recyclerView = (RecyclerView) mLay.findViewById(R.id.book_rv);
        empty_tv = (TextView) mLay.findViewById(R.id.book_tv);
        list = new ArrayList<>();
        bookAdapter = new BookAdapter(getContext(), list);
        bookAdapter.setOnItemClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(
                getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(bookAdapter);
        srl.setColorSchemeColors(Color.CYAN, Color.GREEN, ContextCompat.getColor(getContext(),
                R.color.colorPrimary));
        srl.setOnRefreshListener(this);
        iv_search.setOnClickListener(this);
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
                if (i == 1) {
                    empty_tv.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    i = 0;
                } else if (i == 0) {
                    initData();
                    empty_tv.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    bookAdapter.notifyDataSetChanged();
                    i = 1;
                }
                srl.setRefreshing(false);
            }
        }, 2000);
    }

    @Override
    public void onItemClickListener(View v, int position) {
        APPlication.showToast(list.get(position).getName(), 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.book_iv:
                APPlication.showToast("搜索书籍..", 0);
                break;
        }
    }
}
