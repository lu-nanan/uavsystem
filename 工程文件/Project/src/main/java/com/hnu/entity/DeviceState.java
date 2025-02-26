package com.hnu.entity;

public class DeviceState {
    private int deviceid;
    private float charge;
    private double X;
    private double Y;
    private double Z;
    private String action;

    public int getDeviceid() {
        return deviceid;
    }

    public float getCharge() {
        return charge;
    }

    public double getX() {
        return X;
    }

    public double getY() {
        return Y;
    }

    public double getZ() {
        return Z;
    }

    public String getAction() {
        return action;
    }

    public void setDeviceid(int deviceid) {
        this.deviceid = deviceid;
    }

    public void setCharge(float charge) {
        this.charge = charge;
    }

    public void setX(double x) {
        this.X = x;
    }

    public void setY(double y) {
        this.Y = y;
    }

    public void setZ(double z) {
        this.Z = z;
    }

    public void setAction(String action) {
        this.action = action;
    }
    public DeviceState(){}
}
