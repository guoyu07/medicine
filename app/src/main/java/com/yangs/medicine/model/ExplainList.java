package com.yangs.medicine.model;

/**
 * Created by yangs on 2017/10/10 0010.
 */

public class ExplainList {
    private int index;
    private String name;
    private String explain;
    private Boolean isClick;
    private String Cha;
    private String SP;
    private String answer;
    private String realID;
    private Boolean isAddError = false;

    public Boolean getAddError() {
        return isAddError;
    }

    public void setAddError(Boolean addError) {
        isAddError = addError;
    }

    public String getRealID() {
        return realID;
    }

    public void setRealID(String realID) {
        this.realID = realID;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
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


    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    public Boolean getClick() {
        return isClick;
    }

    public void setClick(Boolean click) {
        isClick = click;
    }
}
