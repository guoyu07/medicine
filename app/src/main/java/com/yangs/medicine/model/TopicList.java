package com.yangs.medicine.model;

/**
 * Created by yangs on 2017/9/24 0024.
 */

public class TopicList {
    private String index;       //序号
    private String name;        //科目

    public String getRealIndex() {
        return realIndex;
    }

    public void setRealIndex(String realIndex) {
        this.realIndex = realIndex;
    }

    private String realIndex;

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

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
