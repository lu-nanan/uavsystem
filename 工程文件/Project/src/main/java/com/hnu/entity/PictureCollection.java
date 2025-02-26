package com.hnu.entity;

import java.awt.Image;
import java.util.Date;

public class PictureCollection {
    private int id;
    private int uavId;
    private Date returnTime;
    private byte[] image;
    public PictureCollection() {};
    public int getId() {return id;}
    public int getUavId() {return uavId;}
    public Date getReturnTime() {return returnTime;}
    public byte[] getImage() {return image;}
    public void setId(int id) {this.id = id;}
    public void setUavId(int uavId) {this.uavId = uavId;}
    public void setReturnTime(Date returnTime) {this.returnTime = returnTime;}
}
