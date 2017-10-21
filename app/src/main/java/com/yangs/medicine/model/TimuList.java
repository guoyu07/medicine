package com.yangs.medicine.model;

import java.io.Serializable;

/**
 * Created by yangs on 2017/10/12 0012.
 */

public class TimuList implements Serializable {
    private int index;          //序号
    private String status;      //状态 :  对,错,没做
    private String type = "";
    private int fragIndex = 0;
    private String answer = "";
    private Boolean isSubmmit = false;

    public Boolean getSubmmit() {
        return isSubmmit;
    }

    public void setSubmmit(Boolean submmit) {
        isSubmmit = submmit;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getFragIndex() {
        return fragIndex;
    }

    public void setFragIndex(int fragIndex) {
        this.fragIndex = fragIndex;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
