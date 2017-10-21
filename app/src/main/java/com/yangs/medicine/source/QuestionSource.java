package com.yangs.medicine.source;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yangs.medicine.activity.APPlication;
import com.yangs.medicine.model.ChooseList;

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
    private static final String GET_QUESTION_URL = "http://";
    private int cursor_index = 1;

    public QuestionSource(String username, String pwd) {
        this.username = username;
        this.pwd = pwd;
        requestHeaders = new Headers.Builder()
                .add("User-Agent", "Mozilla/5.0 (yangs; medicine; Windows NT 6.1; WOW64").build();
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
    public int getQuestion(String id) {
        if (DEBUG)
            Log.i(TAG, "getQuestion " + id + " start...");
        FormBody.Builder formBodyBuilder = new FormBody.Builder().add("id", id);
        RequestBody requestBody = formBodyBuilder.build();
        Request request = new Request.Builder().url(GET_QUESTION_URL).headers(requestHeaders)
                .post(requestBody).build();
        try {
            Response response = mOkHttpClient.newCall(request).execute();
            JSONObject jsonObject = JSON.parseObject(response.body().string());
            initTable("ti_choose", jsonObject.getJSONArray("choose"));
            initTable("ti_blank", jsonObject.getJSONArray("blank"));
            initTable("ti_check", jsonObject.getJSONArray("check"));
            initTable("ti_explain", jsonObject.getJSONArray("explain"));
            initTable("ti_ask", jsonObject.getJSONArray("ask"));
            return 0;
        } catch (Exception e) {
            return -1;
        }
    }

    private void initTable(String table_name, JSONArray array) {
        String sql = "select count(*) as c from Sqlite_master  where type ='table' " +
                "and name ='" + table_name + "' ";
        Cursor cursor = APPlication.db.rawQuery(sql, null);
        try {
            if (cursor.moveToNext()) {
                int count = cursor.getInt(0);
                if (count > 0) {
                    APPlication.db.execSQL("drop table " + table_name);
                }
            }
        } catch (Exception e) {
            APPlication.showToast("清空题目缓存表: " + table_name + "失败!\n" + e.getMessage(), 1);
        } finally {
            if (cursor != null)
                cursor.close();
        }
        if (array.size() == 0)
            return;
        switch (table_name) {
            case "ti_choose":
                APPlication.db.execSQL("create table ti_choose" +
                        "(index INTEGER,question TEXT,answer TEXT,A TEXT,B TEXT,C TEXT,D TEXT,E TEXT," +
                        "explian TEXT,realIndex INTEGER);");
                for (int i = 0; i < array.size(); i++) {
                    JSONObject one = (JSONObject) array.get(i);
                    ContentValues cv = new ContentValues();
                    cv.put("index", i + cursor_index);
                    cv.put("question", one.getString("question"));
                    cv.put("answer", one.getString("answer"));
                    cv.put("A", one.getString("A"));
                    cv.put("B", one.getString("B"));
                    cv.put("C", one.getString("C"));
                    cv.put("D", one.getString("D"));
                    cv.put("E", one.getString("E"));
                    cv.put("realIndex", one.getString("index"));
                    APPlication.db.insert("choose", null, cv);
                }
                cursor_index += array.size();
                break;
            case "ti_blank":
                APPlication.db.execSQL("create table ti_blank" +
                        "(index INTEGER,question TEXT,answer TEXT,realIndex INTEGER);");
                for (int i = 0; i < array.size(); i++) {
                    JSONObject one = (JSONObject) array.get(i);
                    ContentValues cv = new ContentValues();
                    cv.put("index", i + cursor_index);
                    cv.put("question", one.getString("question"));
                    cv.put("answer", one.getString("answer"));
                    cv.put("realIndex", one.getString("index"));
                    APPlication.db.insert("blank", null, cv);
                }
                cursor_index += array.size();
                break;
            case "ti_check":
                APPlication.db.execSQL("create table ti_check" +
                        "(index INTEGER,question TEXT,answer TEXT,explain TEXT,realIndex INTEGER);");
                for (int i = 0; i < array.size(); i++) {
                    JSONObject one = (JSONObject) array.get(i);
                    ContentValues cv = new ContentValues();
                    cv.put("index", i + cursor_index);
                    cv.put("question", one.getString("question"));
                    cv.put("answer", one.getString("answer"));
                    cv.put("explain", one.getString("explain"));
                    cv.put("realIndex", one.getString("index"));
                    APPlication.db.insert("check", null, cv);
                }
                cursor_index += array.size();
                break;
            case "ti_explain":
                APPlication.db.execSQL("create table ti_explain" +
                        "(index INTEGER,question TEXT,answer TEXT,realIndex INTEGER);");
                for (int i = 0; i < array.size(); i++) {
                    JSONObject one = (JSONObject) array.get(i);
                    ContentValues cv = new ContentValues();
                    cv.put("index", i + cursor_index);
                    cv.put("question", one.getString("question"));
                    cv.put("answer", one.getString("answer"));
                    cv.put("realIndex", one.getString("index"));
                    APPlication.db.insert("explain", null, cv);
                }
                cursor_index += array.size();
                break;
            case "ti_ask":
                APPlication.db.execSQL("create table ti_ask" +
                        "(index INTEGER,question TEXT,answer TEXT,realIndex INTEGER);");
                for (int i = 0; i < array.size(); i++) {
                    JSONObject one = (JSONObject) array.get(i);
                    ContentValues cv = new ContentValues();
                    cv.put("index", i + cursor_index);
                    cv.put("question", one.getString("question"));
                    cv.put("answer", one.getString("answer"));
                    cv.put("realIndex", one.getString("index"));
                    APPlication.db.insert("ask", null, cv);
                }
                cursor_index += array.size();
                break;
        }
    }
}
