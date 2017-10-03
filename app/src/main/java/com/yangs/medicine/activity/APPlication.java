package com.yangs.medicine.activity;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by yangs on 2017/9/23 0023.
 */

public class APPlication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        Fresco.initialize(this);
    }

    public static void showToast(String msg, int time) {
        Toast.makeText(context, msg, time).show();
    }
}
