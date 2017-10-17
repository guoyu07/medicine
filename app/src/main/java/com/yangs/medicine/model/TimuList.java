package com.yangs.medicine.model;

/**
 * Created by yangs on 2017/10/12 0012.
 */

public class TimuList {
    private int index;          //序号
    private String status;      //状态 :  对,错,没做

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
