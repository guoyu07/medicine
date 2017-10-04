package com.yangs.medicine.fragment;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import com.yangs.medicine.R;
import com.yangs.medicine.adapter.TitleBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangs on 2017/9/23 0023.
 * 错题集
 */

public class ErrorFragment extends LazyLoadFragment implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private View mLay;
    private Button bt_1;
    private Button bt_2;
    private View v_1;
    private View v_2;
    private ViewPager vp;
    private List<Fragment> listFragment;
    private ErrorTodayFragment errorTodayFragment;
    private ErrorAllFragment errorAllFragment;

    @Override
    protected int setContentView() {
        return R.layout.errorfrag_layout;
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
        mLay = getContentView();
        listFragment = new ArrayList<>();
        errorTodayFragment = new ErrorTodayFragment();
        errorAllFragment = new ErrorAllFragment();
        listFragment.add(errorTodayFragment);
        listFragment.add(errorAllFragment);
        bt_1 = (Button) mLay.findViewById(R.id.error_bt_1);
        bt_2 = (Button) mLay.findViewById(R.id.error_bt_2);
        vp = (ViewPager) mLay.findViewById(R.id.error_vp);
        v_1 = mLay.findViewById(R.id.error_v_1);
        v_2 = mLay.findViewById(R.id.error_v_2);
        bt_1.setOnClickListener(this);
        bt_2.setOnClickListener(this);
        vp.setAdapter(new FragmentPagerAdapter(getFragmentManager()) {
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
            case R.id.error_bt_1:
                v_1.setVisibility(View.VISIBLE);
                v_2.setVisibility(View.INVISIBLE);
                bt_1.setTextColor(ContextCompat.getColor(getActivity(), R.color.error_white1));
                bt_2.setTextColor(ContextCompat.getColor(getActivity(), R.color.error_white2));
                vp.setCurrentItem(0);
                break;
            case R.id.error_bt_2:
                v_1.setVisibility(View.INVISIBLE);
                v_2.setVisibility(View.VISIBLE);
                bt_1.setTextColor(ContextCompat.getColor(getActivity(), R.color.error_white2));
                bt_2.setTextColor(ContextCompat.getColor(getActivity(), R.color.error_white1));
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
                bt_1.setTextColor(ContextCompat.getColor(getActivity(), R.color.error_white1));
                bt_2.setTextColor(ContextCompat.getColor(getActivity(), R.color.error_white2));
                break;
            case 1:
                v_1.setVisibility(View.INVISIBLE);
                v_2.setVisibility(View.VISIBLE);
                bt_1.setTextColor(ContextCompat.getColor(getActivity(), R.color.error_white2));
                bt_2.setTextColor(ContextCompat.getColor(getActivity(), R.color.error_white1));
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
