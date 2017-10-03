package com.yangs.medicine.model;

/**
 * Created by yangs on 2017/10/3 0003.
 */

public class ErrorTodayList {
    private String name;
    private Boolean isClick;
    private int count;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getClick() {
        return isClick;
    }

    public void setClick(Boolean click) {
        isClick = click;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
