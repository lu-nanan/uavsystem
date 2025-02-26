package com.hnu.entity;

import java.util.Date;

public class MessegeTrans {
    private  int transid;
    private int send;
    private int msend;
    private Date time;
    private Boolean succuss;

    public int getTransid() {
        return transid;
    }

    public int getSend() {
        return send;
    }

    public int getMsend() {
        return msend;
    }

    public Date getTime() {
        return time;
    }

    public Boolean getSuccuss() {
        return succuss;
    }

    public void setTransid(int transid) {
        this.transid = transid;
    }

    public void setSend(int send) {
        this.send = send;
    }

    public void setMsend(int msend) {
        this.msend = msend;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public void setSuccuss(Boolean succuss) {
        this.succuss = succuss;
    }
    public MessegeTrans(){}
}
