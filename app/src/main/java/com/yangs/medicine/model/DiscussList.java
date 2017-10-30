package com.yangs.medicine.model;

/**
 * Created by yangs on 2017/10/29 0029.
 */

public class DiscussList {
    private String id;
    private String content;
    private String user;
    private String time;
    private String star;
    private String isYouStar;

    public String getIsYouStar() {
        return isYouStar;
    }

    public void setIsYouStar(String isYouStar) {
        this.isYouStar = isYouStar;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String imgUrl;
    private String type = "";

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
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

    public String getRealIndex() {
        return realIndex;
    }

    public void setRealIndex(String realIndex) {
        this.realIndex = realIndex;
    }

    public void setModel(String model) {
        this.model = model;
    }

    private String realIndex;

    private String ip;
    private String model;
}
