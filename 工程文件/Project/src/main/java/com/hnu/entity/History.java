package com.hnu.entity;

import java.util.Date;

public class History {
    private Date time;
    private int userid;
    private String content;
    private int hisid;

    public Date getTime() {
        return time;
    }

    public int getUserid() {
        return userid;
    }

    public String getContent() {
        return content;
    }

    public int getHisid() {
        return hisid;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setHisid(int hisid) {
        this.hisid = hisid;
    }
    public History(){}
}
