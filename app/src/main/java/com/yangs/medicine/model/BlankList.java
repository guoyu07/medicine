package com.yangs.medicine.model;

/**
 * Created by yangs on 2017/10/10 0010.
 */

public class BlankList {
    private String question;        // $ 代替 ( )
    private String answer;          //答案 ; 分隔
    private String yourAnswer;
    private int index;              //序号
    private String realID;
    private String isFinish;
    private String Cha;
    private String SP;
    private Boolean isAddError = false;

    public String getYourAnswer() {
        return yourAnswer;
    }

    public void setYourAnswer(String yourAnswer) {
        this.yourAnswer = yourAnswer;
    }

    public Boolean getClick() {
        return isClick;
    }

    public void setClick(Boolean click) {
        isClick = click;
    }

    private Boolean isClick;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Boolean getAddError() {
        return isAddError;
    }

    public void setAddError(Boolean addError) {
        isAddError = addError;
    }

    public String getCha() {
        return Cha;
    }

    public void setCha(String cha) {
        Cha = cha;
    }

    public String getSP() {
        return SP;
    }

    public void setSP(String SP) {
        this.SP = SP;
    }

    public String getIsFinish() {
        return isFinish;
    }

    public void setIsFinish(String isFinish) {
        this.isFinish = isFinish;
    }

    public String getRealID() {
        return realID;
    }

    public void setRealID(String realID) {
        this.realID = realID;
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
}
