package com.yangs.medicine.model;

/**
 * Created by yangs on 2017/10/26 0026.
 */

public class Question {
    private String realID;
    private String id;
    private String question;
    private String answer;
    private String explains;
    private String A;
    private String B;
    private String yourAnswer;
    private String IsInError;

    public String getIsInError() {
        return IsInError;
    }

    public void setIsInError(String isInError) {
        IsInError = isInError;
    }

    public String getYourAnswer() {
        return yourAnswer;
    }

    public void setYourAnswer(String yourAnswer) {
        this.yourAnswer = yourAnswer;
    }

    public String getRealID() {
        return realID;
    }

    public void setRealID(String realID) {
        this.realID = realID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getExplains() {
        return explains;
    }

    public void setExplains(String explains) {
        this.explains = explains;
    }

    public String getA() {
        return A;
    }

    public void setA(String a) {
        A = a;
    }

    public String getB() {
        return B;
    }

    public void setB(String b) {
        B = b;
    }

    public String getC() {
        return C;
    }

    public void setC(String c) {
        C = c;
    }

    public String getD() {
        return D;
    }

    public void setD(String d) {
        D = d;
    }

    public String getE() {
        return E;
    }

    public void setE(String e) {
        E = e;
    }

    public String getSP() {
        return SP;
    }

    public void setSP(String SP) {
        this.SP = SP;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCha() {
        return Cha;
    }

    public void setCha(String cha) {
        Cha = cha;
    }

    private String C;
    private String D;
    private String E;
    private String SP;
    private String type;
    private String Cha;
}
