package com.hnu.entity;

import java.util.Date;

public class TaskExample {
    private int id;
    private Date time;
    private int userid;
    private String content;
    private String taskstate;
    private Date completetime;
    private String result;
    private int deviceid;
    public int getId() {
        return id;
    }

    public Date getTime() {
        return time;
    }

    public int getUserid() {
        return userid;
    }

    public String getContent() {
        return content;
    }

    public String getTaskstate() {
        return taskstate;
    }

    public void setId(int id) {
        this.id = id;
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

    public void setTaskstate(String taskstate) {
        this.taskstate = taskstate;
    }
    public Date getCompletetime() {
        return completetime;
    }

    public String getResult() {
        return result;
    }
    public void setCompletetime(Date completetime) {
        this.completetime = completetime;
    }
    public void setResult(String result) {
        this.result = result;
    }
    public int getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(int deviceid) {
        this.deviceid = deviceid;
    }
}
