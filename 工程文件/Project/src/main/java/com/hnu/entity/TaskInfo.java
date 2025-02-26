package com.hnu.entity;

import java.util.Date;

public class TaskInfo {
    private int id;
    private Date time;
    private int userid;
    private String content;
    private String taskstate;

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
    public TaskInfo(){}
}
