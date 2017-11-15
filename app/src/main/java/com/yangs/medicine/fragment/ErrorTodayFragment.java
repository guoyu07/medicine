package com.yangs.medicine.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.yangs.medicine.R;
import com.yangs.medicine.activity.APPlication;
import com.yangs.medicine.activity.QuestionActivity;
import com.yangs.medicine.adapter.ErrorTodayAdapter;
import com.yangs.medicine.model.ErrorTodayList;
import com.yangs.medicine.source.QuestionSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yangs on 2017/10/3 0003.
 */

public class ErrorTodayFragment extends LazyLoadFragment implements ErrorTodayAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    private View mLay;
    private RecyclerView mrecyclerView;
    private TextView empty_tv;
    private List<ErrorTodayList> list;
    private Map<String, List<ErrorTodayList>> map;
    private ErrorTodayAdapter errorTodayAdapter;
    private SwipeRefreshLayout srl;
    private String type;
    private int getErrorCode;
    private String time;

    @Override
    protected int setContentView() {
        return R.layout.errortodayfrag_layout;
    }

    @Override
    protected void lazyLoad() {
        if (isInit) {
            if (!isLoad) {
                list = new ArrayList<>();
                map = new HashMap<>();
                mLay = getContentView();
                mrecyclerView = (RecyclerView) mLay.findViewById(R.id.errortoday_rv);
                empty_tv = (TextView) mLay.findViewById(R.id.errortoday_tv);
                srl = (SwipeRefreshLayout) mLay.findViewById(R.id.errortoday_srl);
                srl.setColorSchemeColors(Color.CYAN, Color.GREEN, ContextCompat.getColor(getContext(),
                        R.color.colorPrimary));
                errorTodayAdapter = new ErrorTodayAdapter(list, getActivity(), type);
                errorTodayAdapter.setOnItemClickListener(ErrorTodayFragment.this);
                mrecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                mrecyclerView.setAdapter(errorTodayAdapter);
                srl.setOnRefreshListener(this);
                empty_tv.setVisibility(View.GONE);
                srl.post(new Runnable() {
                    @Override
                    public void run() {
                        srl.setRefreshing(true);
                        onRefresh();
                    }
                });
            }
        }
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setType(String type) {
        this.type = type;
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    srl.setRefreshing(false);
                    if (getErrorCode == 1) {
                        list.clear();
                        map.clear();
                        String sql = "select sub,error,id from " + QuestionSource.SUBJECT_TABLE_NAME;
                        Cursor cursor = null;
                        try {
                            cursor = APPlication.db.rawQuery(sql, null);
                            if (cursor.getCount() > 0) {
                                if (cursor.moveToFirst()) {
                                    int flag = 0;
                                    do {
                                        int count = cursor.getInt(1);
                                        if (count > 0) {
                                            flag++;
                                            ErrorTodayList errorTodayList = new ErrorTodayList();
                                            errorTodayList.setName(cursor.getString(0));
                                            errorTodayList.setClick(false);
                                            errorTodayList.setCount(count);
                                            errorTodayList.setType("big");
                                            errorTodayList.setSP(cursor.getInt(2));
                                            list.add(errorTodayList);
                                        }
                                    } while (cursor.moveToNext());
                                    if (flag == 0) {
                                        empty_tv.setVisibility(View.VISIBLE);
                                        mrecyclerView.setVisibility(View.GONE);
                                        empty_tv.setText("还没有错题哦");
                                    } else {
                                        empty_tv.setVisibility(View.GONE);
                                        mrecyclerView.setVisibility(View.VISIBLE);
                                        empty_tv.setText("今日还没有错题哦");
                                        errorTodayAdapter.notifyDataSetChanged();
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            if (cursor != null)
                                cursor.close();
                        }
                    } else {
                        APPlication.showToast("网络出错", 0);
                    }
                    break;
            }
            return true;
        }
    });

    @Override
    public void onItemClickListener(View view, int position) {
        if (list.get(position).getType().equals("small")) {
            Bundle bundle = new Bundle();
            bundle.putString("type", "error");
            bundle.putString("SP", list.get(position).getSP() + "");
            bundle.putString("Name", list.get(position).getSubject() + "错题");
            Intent intent = new Intent(getActivity(), QuestionActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
            return;
        }
        if (list.get(position).getClick()) {
            list.get(position).setClick(false);
            list.removeAll(map.get(list.get(position).getName()));
        } else {
            if (map.get(list.get(position).getName()) == null) {
                String sql = "select * from " + QuestionSource.ERRORTODAY_TABLE_NAME + " where SP="
                        + list.get(position).getSP() + "";
                List<ErrorTodayList> lists2 = new ArrayList<>();
                Cursor cursor = APPlication.db.rawQuery(sql, null);
                if (cursor.getCount() > 0) {
                    if (cursor.moveToFirst()) {
                        int count = 1;
                        do {
                            ErrorTodayList errorTodayList = new ErrorTodayList();
                            errorTodayList.setName(count + "." + cursor.getString(1));
                            errorTodayList.setType("small");
                            errorTodayList.setRealIndex(cursor.getInt(2));
                            errorTodayList.setSP(cursor.getInt(3));
                            errorTodayList.setSubject(list.get(position).getName());
                            lists2.add(errorTodayList);
                            count++;
                        } while (cursor.moveToNext());
                    }
                }
                map.put(list.get(position).getName(), lists2);
            }
            list.addAll(position + 1, map.get(list.get(position).getName()));
            list.get(position).setClick(true);
        }
        errorTodayAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getErrorCode = APPlication.questionSource.getErrorList(time, APPlication.user
                        , APPlication.subject);
                handler.sendEmptyMessage(1);
            }
        }).start();
    }
}
