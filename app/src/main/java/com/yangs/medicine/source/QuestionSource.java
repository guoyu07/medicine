package com.yangs.medicine.source;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yangs.medicine.activity.APPlication;
import com.yangs.medicine.model.ChooseList;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.yangs.medicine.activity.APPlication.DEBUG;

/**
 * Created by yangs on 2017/10/21 0021.
 */

public class QuestionSource {
    private OkHttpClient mOkHttpClient;
    private Headers requestHeaders;
    private String username;
    private String pwd;
    private static final String TAG = "QuestionSource";
    private static final String CHECK_USER_URL = "http://";
    private static final String CHECK_STATUS_URL = "http://";
    private static final String GET_QUESTION_URL = "http://120.55.46.93:8080/medicine/action.GetQuestion";
    private static final String TABLE_NAME = "题目_tmp";
    private int cursor_index = 1;
    private String SP;
    private String Cha;

    public QuestionSource(String username, String pwd) {
        this.username = username;
        this.pwd = pwd;
        requestHeaders = new Headers.Builder()
                .add("User-Agent", "yangs;medicine").build();
        mOkHttpClient = new OkHttpClient.Builder().followRedirects(false)
                .followSslRedirects(false).build();
    }

    /*
    检查题目是否在维护中
    Step1: 验证用户
            1:  成功
            -1: 用户名或密码错误
            -2:  其他异常
    Step2: 取得维护码
           2:   正常
           3:   维护
           -3:  网络异常
     */
    public int checkStatus() {
        if (DEBUG)
            Log.i(TAG, "check [user,pwd] start...");
        FormBody.Builder formBodyBuilder = new FormBody.Builder().add("username", this.username)
                .add("passwd", this.pwd);
        RequestBody requestBody = formBodyBuilder.build();
        Request request = new Request.Builder().url(CHECK_USER_URL).headers(requestHeaders)
                .post(requestBody).build();
        try {
            Response response = mOkHttpClient.newCall(request).execute();
            int code;
            try {
                code = Integer.parseInt(response.body().string());
            } catch (Exception e) {
                e.printStackTrace();
                return -2;
            }
            if (code == -1)
                return -1;
            if (code == 1) {
                formBodyBuilder = new FormBody.Builder().add("username", this.username)
                        .add("question_code", "1-1");
                requestBody = formBodyBuilder.build();
                request = new Request.Builder().url(CHECK_STATUS_URL).headers(requestHeaders)
                        .post(requestBody).build();
                try {
                    response = mOkHttpClient.newCall(request).execute();
                    try {
                        code = Integer.parseInt(response.body().string());
                    } catch (Exception e) {
                        e.printStackTrace();
                        return -2;
                    }
                    return code;
                } catch (Exception e) {
                    return -2;
                }
            }
        } catch (Exception e) {
            return -2;
        }
        return 0;
    }

    /*
    取得题目数据
    0:  success
    -1: network error
     */
    public int getQuestion(String SP, String Cha) {
        if (DEBUG)
            Log.i(TAG, "getQuestion start...");
        this.SP = SP;
        this.Cha = Cha;
        FormBody.Builder formBodyBuilder = new FormBody.Builder().add("check", "yangs")
                .add("SP", SP).add("Cha", Cha).add("isClient", "true");
        RequestBody requestBody = formBodyBuilder.build();
        Request request = new Request.Builder().url(GET_QUESTION_URL).headers(requestHeaders)
                .post(requestBody).build();
        try {
            Response response = mOkHttpClient.newCall(request).execute();
            initTable(response.body().string());
            return 0;
        } catch (Exception e) {
            return -1;
        }
    }

    private void initTable(String s) {
        String sql = "select count(*) as c from Sqlite_master  where type ='table' " +
                "and name ='" + TABLE_NAME + "' ";
        Cursor cursor = APPlication.db.rawQuery(sql, null);
        try {
            if (cursor.moveToNext()) {
                int count = cursor.getInt(0);
                if (count > 0) {
                    APPlication.db.execSQL("drop table " + TABLE_NAME);
                }
            }
        } catch (Exception e) {
            APPlication.showToast("清空题目缓存表: " + TABLE_NAME + "失败!\n" + e.getMessage(), 1);
        } finally {
            if (cursor != null)
                cursor.close();
        }
        APPlication.db.execSQL("create table " + TABLE_NAME + " (id INTEGER PRIMARY KEY AUTOINCREMENT,question text not null,answer text not null,A text,B text,C text,D text,E text,explains text,type text,Cha int,SP int,RealIndex int);");
        JSONObject jsonObject = JSON.parseObject(s);
        List<String> list = new ArrayList<>();
        list.add("选择题");
        list.add("填空题");
        list.add("判断题");
        list.add("名词解释题");
        list.add("问答题");
        for (String type : list) {
            JSONArray jsonArray = jsonObject.getJSONArray(type);
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jo = (JSONObject) jsonArray.get(i);
                ContentValues cv = new ContentValues();
                cv.put("question", jo.getString("question"));
                cv.put("answer", jo.getString("answer"));
                cv.put("A", jo.getString("A"));
                cv.put("B", jo.getString("B"));
                cv.put("C", jo.getString("C"));
                cv.put("D", jo.getString("D"));
                cv.put("E", jo.getString("E"));
                cv.put("explains", jo.getString("explains"));
                cv.put("type", type);
                cv.put("Cha", this.Cha);
                cv.put("SP", this.SP);
                cv.put("RealIndex", jo.getString("id"));
                APPlication.db.insert(TABLE_NAME, null, cv);
            }
        }
    }
}
