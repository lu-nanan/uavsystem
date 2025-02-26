package com.hnu.entity;

import java.util.Date;

public class textData {
    private int deviceid;
    private Date backtime;
    private String content;
    private int textid;

    public int getDeviceid() {
        return deviceid;
    }

    public Date getBacktime() {
        return backtime;
    }

    public String getContent() {
        return content;
    }

    public int getTextid() {
        return textid;
    }

    public void setDeviceid(int deviceid) {
        this.deviceid = deviceid;
    }

    public void setBacktime(Date backtime) {
        this.backtime = backtime;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTextid(int textid) {
        this.textid = textid;
    }
    public textData(){}
}
