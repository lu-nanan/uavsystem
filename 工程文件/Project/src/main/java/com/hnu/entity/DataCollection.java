package com.hnu.entity;

import java.util.Date;
public class DataCollection {
    private int dataId;
    private int uavId;
    private Date returnTime;
    private String content;
    public int getDataId() {
        return dataId;
    }
    public int getUavId() {
        return uavId;
    }
    public Date getReturnTime() {
        return  returnTime;
    }
    public String getContent() {
        return content;
    }
    public void setDataId(int dataId) {
        this.dataId = dataId;
    }
    public void setUavId(int uavId) {
        this.uavId = uavId;
    }
    public void setReturnTime(Date returnTime) {
        this.returnTime = returnTime;
    }
    public void setContent(String content) {
        this.content = content;
    }
}
