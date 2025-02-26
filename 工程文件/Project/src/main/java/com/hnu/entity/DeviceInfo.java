package com.hnu.entity;

import java.util.Date;

public class DeviceInfo {
    private int id;
    private String type;
    private String productor;
    private int battery;
    private int distance;
    private Date buytime;

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getProductor() {
        return productor;
    }

    public int getBattery() {
        return battery;
    }

    public int getDistance() {
        return distance;
    }

    public Date getBuytime() {
        return buytime;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setProductor(String productor) {
        this.productor = productor;
    }

    public void setBattery(int battery) {
        this.battery = battery;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public void setBuytime(Date buytime) {
        this.buytime = buytime;
    }
    public DeviceInfo(){

    }
}
