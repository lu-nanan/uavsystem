package com.hnu.entity;

import java.util.Date;

public class DeviceError {
    private int errorid;
    private int deviceid;
    private Date time;
    private int istorepair;

    public int getErrorid() {
        return errorid;
    }

    public int getDeviceid() {
        return deviceid;
    }

    public Date getTime() {
        return time;
    }

    public int getIstorepair() {
        return istorepair;
    }

    public void setDeviceid(int deviceid) {
        this.deviceid = deviceid;
    }

    public void setErrorid(int errorid) {
        this.errorid = errorid;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public void setIstorepair(int istorepair) {
        this.istorepair = istorepair;
    }
    public DeviceError(){}
}
