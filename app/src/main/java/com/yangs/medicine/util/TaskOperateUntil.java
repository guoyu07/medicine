package com.yangs.medicine.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;

import com.yangs.medicine.activity.APPlication;
import com.yangs.medicine.source.QuestionSource;

/**
 * Created by yangs on 2017/12/12 0012.
 */

public class TaskOperateUntil {
    public interface OnResultListener {
        void onResult(int code);
    }

    public static void acceptTaskByID(Context context, final Handler handler, final String type
            , final String user, final String id, final OnResultListener onResultListener) {
        if (context == null || handler == null || id.equals("") || onResultListener == null)
            return;
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("处理中...");
        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                APPlication.questionSource.acceptTaskByID(type, user, id, new QuestionSource.OnResponseCodeResultListener() {
                    @Override
                    public void onResponseResult(final int code, final String response) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                if (code == -1) {
                                    onResultListener.onResult(-1);
                                    return;
                                }
                                if (response.equals("成功")) {
                                    onResultListener.onResult(1);
                                } else {
                                    onResultListener.onResult(0);
                                }
                            }
                        });
                    }
                });
            }
        }).start();
    }

    public static void doTaskByID(final String type, Context context, final Handler handler, final String id, final OnResultListener onResultListener) {
        if (context == null || handler == null || id.equals("") || onResultListener == null)
            return;
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("处理中...");
        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                APPlication.questionSource.doTaskByID(type, id, new QuestionSource.OnResponseCodeResultListener() {
                    @Override
                    public void onResponseResult(final int code, final String response) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                if (code == -1) {
                                    onResultListener.onResult(-1);
                                    return;
                                }
                                if (response.equals("成功")) {
                                    onResultListener.onResult(1);
                                } else {
                                    onResultListener.onResult(0);
                                }
                            }
                        });
                    }
                });
            }
        }).start();
    }
}
