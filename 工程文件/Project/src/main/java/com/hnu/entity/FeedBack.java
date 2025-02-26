package com.hnu.entity;

import java.util.Date;

public class FeedBack {
    private int userid;
    private Date time;
    private String content;
    private Boolean ishandle;
    private int feedid;

    public int getUserid() {
        return userid;
    }

    public Date getTime() {
        return time;
    }

    public String getContent() {
        return content;
    }

    public Boolean getIshandle() {
        return ishandle;
    }

    public int getFeedid() {
        return feedid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setIshandle(Boolean ishandle) {
        this.ishandle = ishandle;
    }

    public void setFeedid(int feedid) {
        this.feedid = feedid;
    }
    public FeedBack(){

    }
}
