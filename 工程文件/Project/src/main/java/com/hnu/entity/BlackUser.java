package com.hnu.entity;

import java.util.Date;

public class BlackUser {
    private int userid;
    private int adminid;
    private Date dotime;

    public int getUserid() {
        return userid;
    }

    public int getAdminid() {
        return adminid;
    }

    public Date getDotime() {
        return dotime;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public void setAdminid(int adminid) {
        this.adminid = adminid;
    }

    public void setDotime(Date dotime) {
        this.dotime = dotime;
    }
    public BlackUser(){}

}
