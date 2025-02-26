package com.hnu.entity;

import java.util.Date;

public class DeviceRepair {
    private int deviceid;
    private Date time;
    private String problem;
    private int handler;
    private Date completetime;
    private int reid;

    public int getDeviceid() {
        return deviceid;
    }

    public Date getTime() {
        return time;
    }

    public String getProblem() {
        return problem;
    }

    public int getHandler() {
        return handler;
    }

    public int getReid() {
        return reid;
    }

    public Date getCompletetime() {
        return completetime;
    }

    public void setDeviceid(int deviceid) {
        this.deviceid = deviceid;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public void setHandler(int handler) {
        this.handler = handler;
    }

    public void setCompletetime(Date completetime) {
        this.completetime = completetime;
    }

    public void setReid(int reid) {
        this.reid = reid;
    }
    public DeviceRepair(){

    }
}
