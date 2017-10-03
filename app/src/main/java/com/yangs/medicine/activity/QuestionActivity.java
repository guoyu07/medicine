package com.yangs.medicine.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.yangs.medicine.R;
import com.yangs.medicine.adapter.TitleBuilder;
import com.yangs.medicine.fragment.QuestionFragment;
import com.yangs.medicine.util.StatusBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangs on 2017/9/24 0024.
 */

public class QuestionActivity extends AppCompatActivity {
    private TitleBuilder titleBuilder;
    private ViewPager viewPager;
    private List<Fragment> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.questionactivity_layout);
        list = new ArrayList<>();
        QuestionFragment answerFragment = new QuestionFragment();
        list.add(answerFragment);
        answerFragment = new QuestionFragment();
        list.add(answerFragment);
        answerFragment = new QuestionFragment();
        list.add(answerFragment);
        viewPager = (ViewPager) findViewById(R.id.question_ac_vp);
        titleBuilder = new TitleBuilder(this);
        titleBuilder.setLeftOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }).setTitleText("临床医学").setRightBtText("交卷").setRightBtOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                APPlication.showToast("交卷...", 0);
            }
        });
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return list.get(position);
            }

            @Override
            public int getCount() {
                return list.size();
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.questionactivity_menu, menu);
        return true;
    }
}
