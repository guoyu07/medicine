package com.yangs.medicine.model;

/**
 * Created by yangs on 2017/10/29 0029.
 */

public class DiscussList {
    private int id;
    private String content;
    private String user;
    private int star;
    private String time;
    private String imgUrl;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getRealIndex() {
        return realIndex;
    }

    public void setRealIndex(int realIndex) {
        this.realIndex = realIndex;
    }

    private String ip;
    private String model;
    private int realIndex;
}
