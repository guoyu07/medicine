package com.yangs.medicine.model;

/**
 * Created by yangs on 2017/10/12 0012.
 */

public class TimuDialogList {
    private int index;          //序号
    private String status;      //状态 :  对错没做
    private String type;        //类型 :  题型

    public String getHeadtitle() {
        return headtitle;
    }

    public void setHeadtitle(String headtitle) {
        this.headtitle = headtitle;
    }

    private String headtitle = "";  //head

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
