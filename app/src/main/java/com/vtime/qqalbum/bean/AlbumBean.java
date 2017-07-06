package com.vtime.qqalbum.bean;

/**
 * Created by Jue on 2017/7/6.
 */

public class AlbumBean {

    private String title;
    private String subId;
    private int url;
    public int value;     //自己在实体中添加的

    public int getUrl() {
        return url;
    }

    public void setUrl(int url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubId() {
        return subId;
    }

    public void setSubId(String subId) {
        this.subId = subId;
    }
}
