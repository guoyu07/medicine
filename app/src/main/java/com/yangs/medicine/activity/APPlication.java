package com.yangs.medicine.activity;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by yangs on 2017/9/23 0023.
 */

public class APPlication extends Application {
    private static Context context;
    public static SharedPreferences save;
    public static String grade;
    public static String subject;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        Fresco.initialize(this);
        save = getSharedPreferences("Medicine", MODE_PRIVATE);
        grade = save.getString("grade", "");
        subject = save.getString("subject", "");
    }

    public static void showToast(String msg, int time) {
        Toast.makeText(context, msg, time).show();
    }
}
