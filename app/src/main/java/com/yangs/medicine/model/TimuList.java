package com.yangs.medicine.model;

import java.util.List;

/**
 * Created by yangs on 2017/10/16 0016.
 */

public class TimuList {
    private String type = "";     //是否为标题
    private List<TimuDialogList> lists;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<TimuDialogList> getLists() {
        return lists;
    }

    public void setLists(List<TimuDialogList> lists) {
        this.lists = lists;
    }
}
