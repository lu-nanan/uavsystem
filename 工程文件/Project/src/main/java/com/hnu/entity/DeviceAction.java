package com.hnu.entity;

import java.util.Date;

public class DeviceAction {
    private int deviceid;
    private Date time;
    private String action;
    private int actid;

    public int getDeviceid() {
        return deviceid;
    }

    public Date getTime() {
        return time;
    }

    public String getAction() {
        return action;
    }

    public int getActid() {
        return actid;
    }

    public void setDeviceid(int deviceid) {
        this.deviceid = deviceid;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setActid(int actid) {
        this.actid = actid;
    }
    public DeviceAction(){}
}
