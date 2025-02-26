package com.hnu.entity;

import java.util.Date;

/**
 * create table 日志
 * (
 *    时间                   datetime not null,
 *    级别                   varchar(100),
 *    来源                   varchar(100),
 *    信息                   varchar(255),
 *    用户ID                 int,
 *    日志ID                 int not null auto_increment,
 *    primary key (日志ID)
 * )
 */
public class Log {
    private Date date;
    private String level;
    private String resource;
    private String message;
    private int userId;
    private int logId;

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }
    public Log(){

    }
}
