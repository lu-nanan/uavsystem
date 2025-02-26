package com.hnu.entity;

import java.util.Date ;

public class TaskResult {
    private int taskid;
    private Date completetime;
    private int usetime;
    private String result;

    public int getTaskid() {
        return taskid;
    }

    public Date getCompletetime() {
        return completetime;
    }

    public int getUsetime() {
        return usetime;
    }

    public String getResult() {
        return result;
    }

    public void setTaskid(int taskid) {
        this.taskid = taskid;
    }

    public void setCompletetime(Date completetime) {
        this.completetime = completetime;
    }

    public void setUsetime(int usetime) {
        this.usetime = usetime;
    }

    public void setResult(String result) {
        this.result = result;
    }
    public TaskResult(){}
}
