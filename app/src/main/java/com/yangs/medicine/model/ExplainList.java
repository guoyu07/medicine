package com.yangs.medicine.model;

/**
 * Created by yangs on 2017/10/10 0010.
 */

public class ExplainList {
    private int index;
    private String name;
    private String explain;
    private Boolean isClick;

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
