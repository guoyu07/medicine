package com.yangs.medicine.model;

/**
 * Created by yangs on 2017/9/24 0024.
 */

public class TopicList {
    private String name;        //名称
    private String type;        //类型
    private String readID;
    private String hasFinishNumber;

    public String getHasFinishNumber() {
        return hasFinishNumber;
    }

    public void setHasFinishNumber(String hasFinishNumber) {
        this.hasFinishNumber = hasFinishNumber;
    }

    public String getReadID() {
        return readID;
    }

    public void setReadID(String readID) {
        this.readID = readID;
    }

    private Boolean isClick;   //是否点击
    private String index;       //序号

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public Boolean getClick() {
        return isClick;
    }

    public void setClick(Boolean click) {
        isClick = click;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    private String number;      //题目数量

    public String getSP() {
        return SP;
    }

    public void setSP(String SP) {
        this.SP = SP;
    }

    private String SP;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getLock() {
        return lock;
    }

    public void setLock(String lock) {
        this.lock = lock;
    }

    private String operation;   //解锁操作

    private String lock;        //锁定标志
}
