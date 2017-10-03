package com.yangs.medicine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.yangs.medicine.activity.MainActivity;

/**
 * Created by yangs on 2017/10/3 0003.
 */

public class Splash extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(Splash.this, MainActivity.class));
        finish();
    }
}
