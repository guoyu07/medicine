package com.yangs.medicine.db;

import android.database.Cursor;

import com.yangs.medicine.activity.APPlication;
import com.yangs.medicine.model.Question;
import com.yangs.medicine.model.SubjectList;
import com.yangs.medicine.source.QuestionSource;

/**
 * Created by yangs on 2017/10/26 0026.
 */

public class QuestionUtil {
    public static int getQuestionCount(String type, String table) {
        String sql = "select count(*) from " + table + " where type='" + type + "';";
        Cursor cursor = null;
        try {
            cursor = APPlication.db.rawQuery(sql, null);
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    return cursor.getInt(0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return 0;
    }

    public static Question getQuestionByID(int id, String table) {
        Question question = new Question();
        String sql = "select * from " + table + " where id=" + id;
        Cursor cursor = null;
        try {
            cursor = APPlication.db.rawQuery(sql, null);
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    question.setId(cursor.getString(0));
                    question.setQuestion(cursor.getString(1));
                    question.setAnswer(cursor.getString(2));
                    question.setA(cursor.getString(3));
                    question.setB(cursor.getString(4));
                    question.setC(cursor.getString(5));
                    question.setD(cursor.getString(6));
                    question.setE(cursor.getString(7));
                    question.setExplains(cursor.getString(8));
                    question.setType(cursor.getString(9));
                    question.setCha(cursor.getString(10));
                    question.setSP(cursor.getString(11));
                    question.setRealID(cursor.getString(12));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return question;
    }
}
