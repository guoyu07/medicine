package com.yangs.medicine.model;

/**
 * Created by yangs on 2017/10/10 0010.
 */

public class BlankList {
    private String question;        // $ 代替 ( )
    private String answer;          //答案 ; 分隔

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

    private int index;              //序号

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
