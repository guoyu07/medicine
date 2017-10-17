package com.yangs.medicine.model;

import java.util.List;

/**
 * Created by yangs on 2017/10/16 0016.
 */

public class TimuDialogList {
    private String type = "";     //是否为标题
    private List<TimuList> lists;       //题目list 最大支持8个TimuList

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<TimuList> getLists() {
        return lists;
    }

    public void setLists(List<TimuList> lists) {
        this.lists = lists;
    }
}
