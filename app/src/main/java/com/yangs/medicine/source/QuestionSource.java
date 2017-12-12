package com.yangs.medicine.source;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Handler;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.yangs.medicine.R;
import com.yangs.medicine.activity.APPlication;
import com.yangs.medicine.model.DiscussList;
import com.yangs.medicine.model.Question;
import com.yangs.medicine.util.IntenetUtil;

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
import static com.yangs.medicine.activity.APPlication.subject;

/**
 * Created by yangs on 2017/10/21 0021.
 */

public class QuestionSource {
    private OkHttpClient mOkHttpClient;
    private Headers requestHeaders;
    private String username;
    private String pwd;
    private static final String TAG = "QuestionSource";
    private static final String HEAD_URL = "http://120.55.46.93:8080/medicine1.2.2/";
    private static final String CHECK_USER_URL = "http://";
    private static final String CHECK_STATUS_URL = "http://";
    private static final String GET_QUESTION_URL = HEAD_URL + "QuestionServlet";
    private static final String GET_SUBJECT_URL = HEAD_URL + "SubjectServlet";
    private static final String GET_CHA_URL = HEAD_URL + "ChaServlet";
    private static final String DISCUSS_URL = HEAD_URL + "DiscussServlet";
    private static final String RECORD_URL = HEAD_URL + "RecordServlet";
    private static final String REGISTER_URL = HEAD_URL + "RegisterServlet";
    private static final String LOGIN_URL = HEAD_URL + "LoginServlet";
    private static final String UPDATE_URL = HEAD_URL + "UpdateServlet";
    private static final String READ_URL = HEAD_URL + "ReadServlet";
    private static final String TASK_URL = HEAD_URL + "TaskServlet";
    public static final String QUESTION_TABLE_NAME = "题目_tmp";
    public static final String SUBJECT_TABLE_NAME = "科目_tmp";
    public static final String CHA_TABLE_NAME = "章节_tmp";
    public static final String ERRORTODAY_TABLE_NAME = "错题集_tmp";
    public static final String ERROR_TABLE_NAME = "错题集详细_tmp";
    public static final String READ_TABLE_NAME = "材料阅读_tmp";
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
        String user = APPlication.user;
        FormBody.Builder formBodyBuilder = new FormBody.Builder().add("check", "yangs")
                .add("action", "getQuestionList").add("client", "android")
                .add("SP", SP).add("Cha", Cha).add("user", user);
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
            APPlication.db.execSQL("create table " + QUESTION_TABLE_NAME + " " +
                    "(id INTEGER PRIMARY KEY AUTOINCREMENT,question text not null," +
                    "answer text not null,A text,B text,C text,D text,E text,explains text," +
                    "type text,Cha int,SP int,RealIndex int,yourAnswer text,IsInError text);");
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
                    cv.put("yourAnswer", jo.getString("yourAnswer"));
                    cv.put("IsInError", jo.getString("IsInError"));
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

    public int getErrorQuestion(String user, String SP, String flag) {
        FormBody.Builder formBodyBuilder = new FormBody.Builder().add("check", "yangs")
                .add("action", "getErrorList").add("flag", flag)
                .add("SP", SP).add("user", user);
        RequestBody requestBody = formBodyBuilder.build();
        Request request = new Request.Builder().url(GET_QUESTION_URL).headers(requestHeaders)
                .post(requestBody).build();
        try {
            Response response = mOkHttpClient.newCall(request).execute();
            String sql = "select count(*) as c from Sqlite_master  where type ='table' " +
                    "and name ='" + ERROR_TABLE_NAME + "' ";
            Cursor cursor = APPlication.db.rawQuery(sql, null);
            try {
                if (cursor.moveToNext()) {
                    int count = cursor.getInt(0);
                    if (count > 0) {
                        APPlication.db.execSQL("drop table " + ERROR_TABLE_NAME);
                    }
                }
            } catch (Exception e) {
                APPlication.showToast("清空题目缓存表: " + ERROR_TABLE_NAME + "失败!\n" + e.getMessage(), 1);
            } finally {
                if (cursor != null)
                    cursor.close();
            }
            APPlication.db.execSQL("create table " + ERROR_TABLE_NAME + " " +
                    "(id INTEGER PRIMARY KEY AUTOINCREMENT,question text not null," +
                    "answer text not null,A text,B text,C text,D text,E text,explains text," +
                    "type text,Cha int,SP int,RealIndex int,yourAnswer text,IsInError text);");
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
                    cv.put("Cha", 0);
                    cv.put("SP", SP);
                    cv.put("RealIndex", jo.getString("id"));
                    cv.put("yourAnswer", "");
                    cv.put("IsInError", jo.getString("IsInError"));
                    APPlication.db.insert(ERROR_TABLE_NAME, null, cv);
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

    public int getSubject(String pro, String grade) {
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
        APPlication.db.execSQL("create table " + SUBJECT_TABLE_NAME + " (id int not null" +
                ",sub text not null,pro text not null,error int);");
        if (DEBUG)
            Log.i(TAG, "getSubject start...");
        FormBody.Builder formBodyBuilder = new FormBody.Builder().add("check", "yangs")
                .add("action", "getSubjectList").add("pro", pro)
                .add("grade", grade);
        RequestBody requestBody = formBodyBuilder.build();
        Request request = new Request.Builder().url(GET_SUBJECT_URL).headers(requestHeaders)
                .post(requestBody).build();
        try {
            Response response = mOkHttpClient.newCall(request).execute();
            String s = response.body().string();
            JSONObject jsonObject = JSON.parseObject(s);
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

    public int getCha(String pro, String grade) {
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
                + " (id INTEGER PRIMARY KEY AUTOINCREMENT,name text,number int,count int,finish int,SP int);");
        sql = "select count(*) as c from Sqlite_master  where type ='table' " +
                "and name ='" + READ_TABLE_NAME + "' ";
        cursor = APPlication.db.rawQuery(sql, null);
        try {
            if (cursor.moveToNext()) {
                int count = cursor.getInt(0);
                if (count > 0) {
                    APPlication.db.execSQL("drop table " + READ_TABLE_NAME);
                }
            }
        } catch (Exception e) {
            APPlication.showToast("清空缓存表: " + READ_TABLE_NAME + "失败!\n" + e.getMessage(), 1);
        } finally {
            if (cursor != null)
                cursor.close();
        }
        APPlication.db.execSQL("create table " + READ_TABLE_NAME + "(id int,title int,SP int);");
        if (DEBUG)
            Log.i(TAG, "getCha start...");
        FormBody.Builder formBodyBuilder = new FormBody.Builder().add("check", "yangs")
                .add("action", "getChaNameAll").add("pro", pro)
                .add("user", APPlication.user).add("grade", grade);
        RequestBody requestBody = formBodyBuilder.build();
        Request request = new Request.Builder().url(GET_CHA_URL).headers(requestHeaders)
                .post(requestBody).build();
        try {
            Response response = mOkHttpClient.newCall(request).execute();
            String s = response.body().string();
            JSONObject ChajsonObject = JSON.parseObject(s);
            response.close();
            formBodyBuilder = new FormBody.Builder().add("check", "yangs")
                    .add("action", "getReadBrief").add("pro", pro)
                    .add("grade", grade);
            requestBody = formBodyBuilder.build();
            request = new Request.Builder().url(READ_URL).headers(requestHeaders)
                    .post(requestBody).build();
            try {
                response = mOkHttpClient.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
                return -1;
            }
            JSONObject ReadjsonObject = JSON.parseObject(response.body().string());
            response.close();
            sql = "select * from " + QuestionSource.SUBJECT_TABLE_NAME;
            cursor = APPlication.db.rawQuery(sql, null);
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        String subject = cursor.getString(1);
                        int SP = cursor.getInt(0);
                        JSONArray jsonArray = ReadjsonObject.getJSONArray(subject);
                        for (int i = 0; i < jsonArray.size(); i++) {
                            JSONObject jo = (JSONObject) jsonArray.get(i);
                            String id = jo.getString("id");
                            String title = jo.getString("title");
                            ContentValues cv = new ContentValues();
                            cv.put("id", id);
                            cv.put("title", title);
                            cv.put("SP", SP);
                            APPlication.db.insert(READ_TABLE_NAME, null, cv);
                        }
                        jsonArray = ChajsonObject.getJSONArray(subject);
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
                            cv.put("finish", jo.getInteger("finish"));
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
        String model = APPlication.getModel();
        FormBody.Builder formBodyBuilder = new FormBody.Builder().add("check", "yangs")
                .add("action", "postDiscuss").add("realIndex", realIndex)
                .add("content", content).add("user", APPlication.user).add("model", model);
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

    public String uploadRecord(String user, String operate, String realID, String result,
                               String answer, String SP, String Cha) {
        String s = "";
        String network = IntenetUtil.getNetworkState(APPlication.getContext());
        String model = APPlication.getModel();
        String version = APPlication.getVersion();
        FormBody.Builder formBodyBuilder = new FormBody.Builder().add("check", "yangs")
                .add("action", "addRecord").add("user", user)
                .add("SP", SP).add("Cha", Cha).add("answer", answer)
                .add("operate", operate).add("realID", realID)
                .add("result", result).add("network", network)
                .add("model", model).add("version", version);
        RequestBody requestBody = formBodyBuilder.build();
        Request request = new Request.Builder().url(RECORD_URL).headers(requestHeaders)
                .post(requestBody).build();
        try {
            Response response = mOkHttpClient.newCall(request).execute();
            s = response.body().string();
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s;
    }

    public void removeError(String user, String realID, OnResponseCodeResultListener onResponseCodeResultListener) {
        if (onResponseCodeResultListener == null)
            return;
        FormBody.Builder formBodyBuilder = new FormBody.Builder().add("check", "yangs")
                .add("action", "removeError").add("user", user)
                .add("realID", realID);
        RequestBody requestBody = formBodyBuilder.build();
        Request request = new Request.Builder().url(RECORD_URL).headers(requestHeaders)
                .post(requestBody).build();
        try {
            Response response = mOkHttpClient.newCall(request).execute();
            onResponseCodeResultListener.onResponseResult(1, response.body().string());
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
            onResponseCodeResultListener.onResponseResult(-1, null);
        }
    }

    public String checkUserIsExist(String username) {
        String result = "";
        FormBody.Builder formBodyBuilder = new FormBody.Builder().add("check", "yangs")
                .add("action", "checkIsExist").add("username", username);
        RequestBody requestBody = formBodyBuilder.build();
        Request request = new Request.Builder().url(REGISTER_URL).headers(requestHeaders)
                .post(requestBody).build();
        try {
            Response response = mOkHttpClient.newCall(request).execute();
            result = response.body().string();
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String registerUser(String username, String pwd) {
        String result = "";
        FormBody.Builder formBodyBuilder = new FormBody.Builder().add("check", "yangs")
                .add("action", "register").add("username", username)
                .add("pwd", pwd);
        RequestBody requestBody = formBodyBuilder.build();
        Request request = new Request.Builder().url(REGISTER_URL).headers(requestHeaders)
                .post(requestBody).build();
        try {
            Response response = mOkHttpClient.newCall(request).execute();
            result = response.body().string();
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String login(String username, String pwd) {
        String result = "";
        FormBody.Builder formBodyBuilder = new FormBody.Builder().add("check", "yangs")
                .add("username", username).add("pwd", pwd);
        RequestBody requestBody = formBodyBuilder.build();
        Request request = new Request.Builder().url(LOGIN_URL).headers(requestHeaders)
                .post(requestBody).build();
        try {
            Response response = mOkHttpClient.newCall(request).execute();
            result = response.body().string();
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String addGradeSubject(String username, String grade, String subject) {
        String result = "";
        FormBody.Builder formBodyBuilder = new FormBody.Builder().add("check", "yangs")
                .add("action", "addGradeSubject").add("username", username)
                .add("grade", grade).add("subject", subject);
        RequestBody requestBody = formBodyBuilder.build();
        Request request = new Request.Builder().url(REGISTER_URL).headers(requestHeaders)
                .post(requestBody).build();
        try {
            Response response = mOkHttpClient.newCall(request).execute();
            result = response.body().string();
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public int getErrorList(String time, String user, String pro) {
        int code;
        String sql = "select count(*) as c from Sqlite_master  where type ='table' " +
                "and name ='" + ERRORTODAY_TABLE_NAME + "' ";
        Cursor cursor = APPlication.db.rawQuery(sql, null);
        try {
            if (cursor.moveToNext()) {
                int count = cursor.getInt(0);
                if (count > 0) {
                    APPlication.db.execSQL("drop table " + ERRORTODAY_TABLE_NAME);
                }
            }
        } catch (Exception e) {
            APPlication.showToast("清空缓存表: " + ERRORTODAY_TABLE_NAME + "失败!\n" + e.getMessage(), 1);
        } finally {
            if (cursor != null)
                cursor.close();
        }
        APPlication.db.execSQL("create table " + ERRORTODAY_TABLE_NAME + "" +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT,question text not null,realID int not null" +
                ",SP int not null);");
        if (DEBUG)
            Log.i(TAG, "getError start...");
        FormBody.Builder formBodyBuilder = new FormBody.Builder().add("check", "yangs")
                .add("action", "getError").add("user", user)
                .add("time", time).add("pro", pro)
                .add("grade", APPlication.grade);
        RequestBody requestBody = formBodyBuilder.build();
        Request request = new Request.Builder().url(RECORD_URL).headers(requestHeaders)
                .post(requestBody).build();
        try {
            Response response = mOkHttpClient.newCall(request).execute();
            String s = response.body().string();
            JSONObject jsonObject = JSON.parseObject(s);
            sql = "select * from " + QuestionSource.SUBJECT_TABLE_NAME;
            Cursor cursor2 = null;
            try {
                cursor2 = APPlication.db.rawQuery(sql, null);
                if (cursor2.getCount() > 0) {
                    if (cursor2.moveToFirst()) {
                        do {
                            int SP = cursor2.getInt(0);
                            String subject = cursor2.getString(1);
                            JSONArray jsonArray = jsonObject.getJSONArray(subject);
                            for (int i = 0; i < jsonArray.size(); i++) {
                                JSONObject jo = (JSONObject) jsonArray.get(i);
                                ContentValues cv = new ContentValues();
                                cv.put("question", jo.getString("question"));
                                cv.put("realID", jo.getInteger("realID"));
                                cv.put("SP", SP);
                                APPlication.db.insert(ERRORTODAY_TABLE_NAME, null, cv);
                            }
                            sql = "update " + SUBJECT_TABLE_NAME + " set error=" + jsonArray.size()
                                    + " where sub='" + subject + "' and pro='" + pro + "'";
                            APPlication.db.execSQL(sql);
                        } while (cursor2.moveToNext());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor2 != null)
                    cursor2.close();
            }
            response.close();
            code = 1;
        } catch (IOException e) {
            e.printStackTrace();
            code = -1;
        }
        return code;
    }

    public void getRead(String id, OnResponseCodeResultListener onResponseCodeResultListener) {
        if (onResponseCodeResultListener == null)
            return;
        FormBody.Builder formBodyBuilder = new FormBody.Builder().add("check", "yangs")
                .add("action", "getOneRead")
                .add("id", id);
        RequestBody requestBody = formBodyBuilder.build();
        Request request = new Request.Builder().url(READ_URL).headers(requestHeaders)
                .post(requestBody).build();
        try {
            Response response = mOkHttpClient.newCall(request).execute();
            onResponseCodeResultListener.onResponseResult(1, response.body().string());
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
            onResponseCodeResultListener.onResponseResult(-1, null);
        }
    }

    public void editInfo(String user, String grade, String subject, OnResponseCodeResultListener onResponseCodeResultListener) {
        if (onResponseCodeResultListener == null)
            return;
        FormBody.Builder formBodyBuilder = new FormBody.Builder().add("check", "yangs")
                .add("action", "editInfo")
                .add("user", user).add("grade", grade)
                .add("subject", subject);
        RequestBody requestBody = formBodyBuilder.build();
        Request request = new Request.Builder().url(UPDATE_URL).headers(requestHeaders)
                .post(requestBody).build();
        try {
            Response response = mOkHttpClient.newCall(request).execute();
            onResponseCodeResultListener.onResponseResult(1, response.body().string());
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
            onResponseCodeResultListener.onResponseResult(-1, null);
        }
    }

    public void checkVersion(Handler handler, final OnResponseResultListener onResponseResultListener) {
        if (onResponseResultListener == null)
            return;
        String s = null;
        FormBody.Builder formBodyBuilder = new FormBody.Builder().add("check", "yangs")
                .add("action", "checkVersion")
                .add("cu_version", APPlication.getVersion());
        RequestBody requestBody = formBodyBuilder.build();
        Request request = new Request.Builder().url(UPDATE_URL).headers(requestHeaders)
                .post(requestBody).build();
        try {
            Response response = mOkHttpClient.newCall(request).execute();
            s = response.body().string();
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        final String finalS = s;
        handler.post(new Runnable() {
            @Override
            public void run() {
                onResponseResultListener.onResponseResult(finalS);
            }
        });
    }

    public void uploadErrorLog(String src, String filename) {
        if (src == null || src.length() == 0)
            return;
        FormBody.Builder formBodyBuilder = new FormBody.Builder().add("check", "yangs")
                .add("action", "uploadErrorLog")
                .add("msg", src).add("filename", filename)
                .add("user", APPlication.user);
        RequestBody requestBody = formBodyBuilder.build();
        Request request = new Request.Builder().url(RECORD_URL).headers(requestHeaders)
                .post(requestBody).build();
        try {
            mOkHttpClient.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addTask(String content, String user, String money, OnResponseCodeResultListener onResponseCodeResultListener) {
        FormBody.Builder formBodyBuilder = new FormBody.Builder().add("check", "yangs")
                .add("action", "addTask")
                .add("content", content).add("user", user)
                .add("money", money);
        RequestBody requestBody = formBodyBuilder.build();
        Request request = new Request.Builder().url(TASK_URL).headers(requestHeaders)
                .post(requestBody).build();
        try {
            Response response = mOkHttpClient.newCall(request).execute();
            onResponseCodeResultListener.onResponseResult(1, response.body().string());
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
            onResponseCodeResultListener.onResponseResult(-1, null);
        }
    }

    public void getTaskList(String type, String user, String start, OnResponseCodeResultListener onResponseCodeResultListener) {
        FormBody.Builder formBodyBuilder = new FormBody.Builder().add("check", "yangs")
                .add("action", "getTaskList")
                .add("type", type).add("user", user)
                .add("start", start);
        RequestBody requestBody = formBodyBuilder.build();
        Request request = new Request.Builder().url(TASK_URL).headers(requestHeaders)
                .post(requestBody).build();
        try {
            Response response = mOkHttpClient.newCall(request).execute();
            onResponseCodeResultListener.onResponseResult(1, response.body().string());
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
            onResponseCodeResultListener.onResponseResult(-1, null);
        }
    }

    public void acceptTaskByID(String type, String user, String id, OnResponseCodeResultListener onResponseCodeResultListener) {
        FormBody.Builder formBodyBuilder = new FormBody.Builder().add("check", "yangs")
                .add("action", "acceptTaskByID")
                .add("type", type).add("user", user)
                .add("id", id);
        RequestBody requestBody = formBodyBuilder.build();
        Request request = new Request.Builder().url(TASK_URL).headers(requestHeaders)
                .post(requestBody).build();
        try {
            Response response = mOkHttpClient.newCall(request).execute();
            onResponseCodeResultListener.onResponseResult(1, response.body().string());
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
            onResponseCodeResultListener.onResponseResult(-1, null);
        }
    }

    public void doTaskByID(String type, String id, OnResponseCodeResultListener onResponseCodeResultListener) {
        FormBody.Builder formBodyBuilder = new FormBody.Builder().add("check", "yangs")
                .add("action", type)
                .add("id", id);
        RequestBody requestBody = formBodyBuilder.build();
        Request request = new Request.Builder().url(TASK_URL).headers(requestHeaders)
                .post(requestBody).build();
        try {
            Response response = mOkHttpClient.newCall(request).execute();
            onResponseCodeResultListener.onResponseResult(1, response.body().string());
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
            onResponseCodeResultListener.onResponseResult(-1, null);
        }
    }

    public interface OnResponseResultListener {
        void onResponseResult(String response);
    }

    public interface OnResponseCodeResultListener {
        void onResponseResult(int code, String response);
    }
}
