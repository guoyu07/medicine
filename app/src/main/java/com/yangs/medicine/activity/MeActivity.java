package com.yangs.medicine.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yangs.medicine.R;
import com.yangs.medicine.fragment.ErrorTodayFragment;
import com.yangs.medicine.fragment.MeBookFragment;
import com.yangs.medicine.fragment.TaskFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangs on 2017/10/5 0005.
 */

public class MeActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private ImageView iv_back;
    private TextView tv_title;
    private Button bt_1;
    private Button bt_2;
    private View v_1;
    private View v_2;
    private ViewPager vp;
    private Bundle bundle;
    private List<Fragment> listFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meactivity_layout);
        initView();
        initType();
    }

    private void initView() {
        listFragment = new ArrayList<>();
        iv_back = (ImageView) findViewById(R.id.meactivity_iv_back);
        tv_title = (TextView) findViewById(R.id.meactivity_tv_title);
        bt_1 = (Button) findViewById(R.id.meactivity_bt_1);
        bt_2 = (Button) findViewById(R.id.meactivity_bt_2);
        v_1 = findViewById(R.id.meactivity_v_1);
        v_2 = findViewById(R.id.meactivity_v_2);
        vp = (ViewPager) findViewById(R.id.meactivity_vp);
        iv_back.setOnClickListener(this);
        bt_1.setOnClickListener(this);
        bt_2.setOnClickListener(this);
    }

    private void initType() {
        bundle = getIntent().getExtras();
        if ("我的收藏".equals(bundle.getString("name"))) {
            tv_title.setText("我的收藏");
            bt_1.setText("题");
            bt_2.setText("教材");
            ErrorTodayFragment errorTodayFragment = new ErrorTodayFragment();      //题
            MeBookFragment meBookFragment = new MeBookFragment();              //教材
            errorTodayFragment.setType("NotError");
            listFragment.add(errorTodayFragment);
            listFragment.add(meBookFragment);
        } else {
            tv_title.setText("我的任务");
            bt_1.setText("我发布的");
            bt_2.setText("我接受的");
            TaskFragment taskFragment1 = new TaskFragment();
            TaskFragment taskFragment2 = new TaskFragment();
            taskFragment1.setTyep("我发布的");
            taskFragment2.setTyep("我接受的");
            listFragment.add(taskFragment1);
            listFragment.add(taskFragment2);
        }
        vp.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return listFragment.get(position);
            }

            @Override
            public int getCount() {
                return listFragment.size();
            }
        });
        vp.addOnPageChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.meactivity_iv_back:
                finish();
                break;
            case R.id.meactivity_bt_1:
                v_1.setVisibility(View.VISIBLE);
                v_2.setVisibility(View.INVISIBLE);
                bt_1.setTextColor(ContextCompat.getColor(MeActivity.this, R.color.error_white1));
                bt_2.setTextColor(ContextCompat.getColor(MeActivity.this, R.color.error_white2));
                vp.setCurrentItem(0);
                break;
            case R.id.meactivity_bt_2:
                v_1.setVisibility(View.INVISIBLE);
                v_2.setVisibility(View.VISIBLE);
                bt_1.setTextColor(ContextCompat.getColor(MeActivity.this, R.color.error_white2));
                bt_2.setTextColor(ContextCompat.getColor(MeActivity.this, R.color.error_white1));
                vp.setCurrentItem(1);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                v_1.setVisibility(View.VISIBLE);
                v_2.setVisibility(View.INVISIBLE);
                bt_1.setTextColor(ContextCompat.getColor(MeActivity.this, R.color.error_white1));
                bt_2.setTextColor(ContextCompat.getColor(MeActivity.this, R.color.error_white2));
                vp.setCurrentItem(0);
                break;
            case 1:
                v_1.setVisibility(View.INVISIBLE);
                v_2.setVisibility(View.VISIBLE);
                bt_1.setTextColor(ContextCompat.getColor(MeActivity.this, R.color.error_white2));
                bt_2.setTextColor(ContextCompat.getColor(MeActivity.this, R.color.error_white1));
                vp.setCurrentItem(1);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
