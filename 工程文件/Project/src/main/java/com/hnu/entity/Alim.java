package com.hnu.entity;

import java.util.Date;

public class Alim {
    private Integer taskId;
    private Date taskAssignTime;
    private Integer userId;
    private String taskContent;
    private String taskStatus;

    // 以下是对应的getter和setter方法
    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Date getTaskAssignTime() {
        return taskAssignTime;
    }

    public void setTaskAssignTime(Date taskAssignTime) {
        this.taskAssignTime = taskAssignTime;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTaskContent() {
        return taskContent;
    }

    public void setTaskContent(String taskContent) {
        this.taskContent = taskContent;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }
}