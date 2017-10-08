package com.yangs.medicine.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.yangs.medicine.R;
import com.yangs.medicine.util.FitStatusBar;

/**
 * Created by yangs on 2017/9/24 0024.
 */

public class SearchActivity extends BaseActivity implements View.OnKeyListener {
    private Toolbar toolbar;
    private EditText et_txt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchactivity_layout);
        FitStatusBar.addStatusBarView(this);
        toolbar = (Toolbar) findViewById(R.id.search_ac_toolbar);
        et_txt = (EditText) toolbar.findViewById(R.id.search_ac_tv_txt);
        et_txt.setOnKeyListener(this);
        et_txt.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_UP) {
            InputMethodManager inputMethodManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(SearchActivity.this.getCurrentFocus()
                            .getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
            APPlication.showToast(et_txt.getText().toString(), 0);
            return true;
        }
        return false;
    }
}
