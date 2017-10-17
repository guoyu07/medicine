package com.yangs.medicine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.yangs.medicine.activity.APPlication;
import com.yangs.medicine.activity.LoginActivity;
import com.yangs.medicine.activity.MainActivity;
import com.yangs.medicine.activity.SelectSubActivity;

/**
 * Created by yangs on 2017/10/3 0003.
 */

public class Splash extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!APPlication.save.getBoolean("login_status", false))
            startActivity(new Intent(Splash.this, LoginActivity.class));
        else
            startActivity(new Intent(Splash.this, MainActivity.class));
        finish();
    }
}
