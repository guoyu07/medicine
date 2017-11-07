package com.yangs.medicine.source;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.yangs.medicine.R;
import com.yangs.medicine.activity.APPlication;
import com.yangs.medicine.model.DiscussList;

import java.io.IOException;
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
    private static final String GET_QUESTION_URL = "http://120.55.46.93:8080/medicine2/QuestionServlet";
    private static final String GET_SUBJECT_URL = "http://120.55.46.93:8080/medicine2/SubjectServlet";
    private static final String GET_CHA_URL = "http://120.55.46.93:8080/medicine2/ChaServlet";
    private static final String DISCUSS_URL = "http://120.55.46.93:8080/medicine2/DiscussServlet";
    public static final String QUESTION_TABLE_NAME = "题目_tmp";
    public static final String SUBJECT_TABLE_NAME = "科目_tmp";
    public static final String CHA_TABLE_NAME = "章节_tmp";
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
                .add("action", "getQuestionList").add("client", "android")
                .add("SP", SP).add("Cha", Cha);
        RequestBody requestBody = formBodyBuilder.build();
        Request request = new Request.Builder().url(GET_QUESTION_URL).headers(requestHeaders)
                .post(requestBody).build();
        try {
            Response response = mOkHttpClient.newCall(request).execute();
            String sql = "select count(*) as c from Sqlite_master  where type ='table' " +
                    "and name ='" + QUESTION_TABLE_NAME + "' ";
            Cursor cursor = APPlication.db.rawQuery(sql, null);
            try {
                if (cursor.moveToNext()) {
                    int count = cursor.getInt(0);
                    if (count > 0) {
                        APPlication.db.execSQL("drop table " + QUESTION_TABLE_NAME);
                    }
                }
            } catch (Exception e) {
                APPlication.showToast("清空题目缓存表: " + QUESTION_TABLE_NAME + "失败!\n" + e.getMessage(), 1);
            } finally {
                if (cursor != null)
                    cursor.close();
            }
            APPlication.db.execSQL("create table " + QUESTION_TABLE_NAME + " (id INTEGER PRIMARY KEY AUTOINCREMENT,question text not null,answer text not null,A text,B text,C text,D text,E text,explains text,type text,Cha int,SP int,RealIndex int);");
            String s = response.body().string();
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
                    cv.put("explains", jo.getString("explain"));
                    cv.put("type", type);
                    cv.put("Cha", this.Cha);
                    cv.put("SP", this.SP);
                    cv.put("RealIndex", jo.getString("id"));
                    APPlication.db.insert(QUESTION_TABLE_NAME, null, cv);
                }
            }
            response.close();
            return 0;
        } catch (JSONException e) {
            e.printStackTrace();
            return -1;
        } catch (IOException e) {
            e.printStackTrace();
            return -2;
        }
    }

    public int getSubject(String pro) {
        String sql = "select count(*) as c from Sqlite_master  where type ='table' " +
                "and name ='" + SUBJECT_TABLE_NAME + "' ";
        Cursor cursor = APPlication.db.rawQuery(sql, null);
        try {
            if (cursor.moveToNext()) {
                int count = cursor.getInt(0);
                if (count > 0) {
                    APPlication.db.execSQL("drop table " + SUBJECT_TABLE_NAME);
                }
            }
        } catch (Exception e) {
            APPlication.showToast("清空缓存表: " + SUBJECT_TABLE_NAME + "失败!\n" + e.getMessage(), 1);
        } finally {
            if (cursor != null)
                cursor.close();
        }
        APPlication.db.execSQL("create table " + SUBJECT_TABLE_NAME + " (id int not null,sub text not null,pro text not null);");
        if (DEBUG)
            Log.i(TAG, "getSubject start...");
        FormBody.Builder formBodyBuilder = new FormBody.Builder().add("check", "yangs")
                .add("action", "getSubjectList").add("pro", pro);
        RequestBody requestBody = formBodyBuilder.build();
        Request request = new Request.Builder().url(GET_SUBJECT_URL).headers(requestHeaders)
                .post(requestBody).build();
        try {
            Response response = mOkHttpClient.newCall(request).execute();
            JSONObject jsonObject = JSON.parseObject(response.body().string());
            JSONArray jsonArray = jsonObject.getJSONArray("subject");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jo = (JSONObject) jsonArray.get(i);
                ContentValues cv = new ContentValues();
                cv.put("id", jo.getInteger("SP"));
                cv.put("sub", jo.getString("Sub"));
                cv.put("pro", pro);
                APPlication.db.insert(SUBJECT_TABLE_NAME, null, cv);
            }
            response.close();
            return 0;
        } catch (JSONException e) {
            e.printStackTrace();
            return -1;
        } catch (IOException e) {
            e.printStackTrace();
            return -2;
        }
    }

    public int getCha(String pro) {
        String sql = "select count(*) as c from Sqlite_master  where type ='table' " +
                "and name ='" + CHA_TABLE_NAME + "' ";
        Cursor cursor = APPlication.db.rawQuery(sql, null);
        try {
            if (cursor.moveToNext()) {
                int count = cursor.getInt(0);
                if (count > 0) {
                    APPlication.db.execSQL("drop table " + CHA_TABLE_NAME);
                }
            }
        } catch (Exception e) {
            APPlication.showToast("清空缓存表: " + CHA_TABLE_NAME + "失败!\n" + e.getMessage(), 1);
        } finally {
            if (cursor != null)
                cursor.close();
        }
        APPlication.db.execSQL("create table " + CHA_TABLE_NAME
                + " (id INTEGER PRIMARY KEY AUTOINCREMENT,name text,number int,count int,SP int);");
        if (DEBUG)
            Log.i(TAG, "getCha start...");
        FormBody.Builder formBodyBuilder = new FormBody.Builder().add("check", "yangs")
                .add("action", "getChaNameAll").add("pro", pro);
        RequestBody requestBody = formBodyBuilder.build();
        Request request = new Request.Builder().url(GET_CHA_URL).headers(requestHeaders)
                .post(requestBody).build();
        try {
            Response response = mOkHttpClient.newCall(request).execute();
            JSONObject jsonObject = JSON.parseObject(response.body().string());
            sql = "select * from " + QuestionSource.SUBJECT_TABLE_NAME;
            cursor = APPlication.db.rawQuery(sql, null);
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        String subject = cursor.getString(1);
                        int SP = cursor.getInt(0);
                        JSONArray jsonArray = jsonObject.getJSONArray(subject);
                        for (int i = 0; i < jsonArray.size(); i++) {
                            JSONObject jo = (JSONObject) jsonArray.get(i);
                            String name = jo.getString("name");
                            String index = jo.getString("index");
                            int count = jo.getInteger("number");
                            if (name == null || name.equals("") || name.equals("null"))
                                name = "暂无";
                            ContentValues cv = new ContentValues();
                            cv.put("name", name);
                            cv.put("number", index);
                            cv.put("count", count);
                            cv.put("SP", SP);
                            APPlication.db.insert(CHA_TABLE_NAME, null, cv);
                        }
                    } while (cursor.moveToNext());
                }
            }
            response.close();
            return 0;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        } catch (JSONException e) {
            e.printStackTrace();
            return -2;
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

    public List<DiscussList> getDiscussList(String realIndex, int start) {
        List<DiscussList> lists = new ArrayList<>();
        FormBody.Builder formBodyBuilder = new FormBody.Builder().add("check", "yangs")
                .add("action", "getDiscussList").add("realIndex", realIndex + "")
                .add("start", start + "").add("user", APPlication.user);
        RequestBody requestBody = formBodyBuilder.build();
        Request request = new Request.Builder().url(DISCUSS_URL).headers(requestHeaders)
                .post(requestBody).build();
        try {
            Response response = mOkHttpClient.newCall(request).execute();
            String re = response.body().string();
            JSONObject jsonObject = JSON.parseObject(re);
            JSONArray jsonArray = jsonObject.getJSONArray("discuss");
            for (int i = 0, j = jsonArray.size(); i < j; i++) {
                DiscussList discussList;
                JSONObject jo = (JSONObject) jsonArray.get(i);
                discussList = new DiscussList();
                discussList.setId(jo.getString("id"));
                discussList.setContent(jo.getString("content"));
                discussList.setUser(jo.getString("user"));
                discussList.setStar(jo.getString("star"));
                discussList.setTime(jo.getString("time"));
                discussList.setIp(jo.getString("ip"));
                discussList.setModel(jo.getString("model"));
                discussList.setIsYouStar(jo.getString("getIsYouStar"));
                discussList.setRealIndex(jo.getString("realIndex"));
                discussList.setImgUrl("res://com.yangs.medicine/" + R.drawable.img_touxiang);
                lists.add(discussList);
            }
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return lists;
    }

    public int postDiscuss(String content, String realIndex) {
        String model = android.os.Build.MODEL + ";" + android.os.Build.VERSION.SDK + ";"
                + android.os.Build.VERSION.RELEASE;
        String user = "yangs";
        FormBody.Builder formBodyBuilder = new FormBody.Builder().add("check", "yangs")
                .add("action", "postDiscuss").add("realIndex", realIndex)
                .add("content", content).add("user", user).add("model", model);
        RequestBody requestBody = formBodyBuilder.build();
        Request request = new Request.Builder().url(DISCUSS_URL).headers(requestHeaders)
                .post(requestBody).build();
        try {
            Response response = mOkHttpClient.newCall(request).execute();
            String re = response.body().string();
            response.close();
            if ("评论成功".equals(re))
                return 0;
            else
                return -1;
        } catch (IOException e) {
            e.printStackTrace();
            return -2;
        }
    }

    public int discussStar(String action, String id, String user) {
        FormBody.Builder formBodyBuilder = new FormBody.Builder().add("check", "yangs")
                .add("action", action).add("id", id).add("user", user);
        RequestBody requestBody = formBodyBuilder.build();
        Request request = new Request.Builder().url(DISCUSS_URL).headers(requestHeaders)
                .post(requestBody).build();
        try {
            Response response = mOkHttpClient.newCall(request).execute();
            String re = response.body().string();
            response.close();
            if (re == null)
                return -2;
            switch (re) {
                case "赞成功":
                    return 0;
                case "没有这条评论":
                    APPlication.showToast("没有这条评论", 1);
                    break;
                case "已经赞过":
                    APPlication.showToast("已经赞过", 1);
                    break;
                case "服务器错误":
                    APPlication.showToast("服务器错误", 1);
                    break;
                case "取消赞成功":
                    return 1;
                case "取消赞失败":
                    APPlication.showToast("取消赞失败", 1);
                    break;
            }
            return -1;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -2;
    }
}
