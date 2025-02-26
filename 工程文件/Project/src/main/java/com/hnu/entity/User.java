package com.hnu.entity;
/**
 * 用户实体类
 */

import com.baomidou.mybatisplus.annotation.TableName;

@TableName("个人信息")
public class User {
    private String password;//密码
    private int id;//用户ID
    private String email;//邮箱
    private long phone; // 用户的电话号码属性
    private int type; // 用户的类型属性
    private static volatile User instance; // 用户的类型属性
    // getter方法，获取邮箱
    public String getEmail() {
        return email;
    }
    // getter方法，获取用户类型
    public int getType() {
        return type;
    }
    // getter方法，获取用户ID
    public int getId() {
        return id;
    }
    // getter方法，获取密码
    public String getPassword() {
        return password;
    }
    // getter方法，获取电话号码
    public long getPhone() {
        return phone;
    }
    // setter方法，设置密码
    public void setPassword(String password) {
        this.password = password;
    }
    // setter方法，设置用户类型
    public void setType(int type) {
        this.type = type;
    }
    // setter方法，设置用户ID
    public void setId(int id) {
        this.id = id;
    }
    // setter方法，设置用户邮箱
    public void setEmail(String email) {
        this.email = email;
    }
    // setter方法，设置用户手机号
    public void setPhone(long phone) {
        this.phone = phone;
    }
    /**
     * 构造函数
     */
    public User(){
        this.email=null;
    }
    /**
     * 带参数的构造函数，用于创建User对象。
     * @param password 用户密码
     * @param email 用户邮箱
     * @param phone 用户电话号码
     * @param type 用户类型
     */
    public User(String password, String email,long phone,int type){
        this.email=email;
        this.password=password;
        this.phone=phone;
        this.type=type;
    }
    /**
     * 获取User类的唯一实例。
     * 使用双重检查锁定（Double-Checked Locking）模式确保线程安全。
     * @return User类的唯一实例
     */
    public User getInstance(){
        if (instance == null) { // 检查实例是否为空
            synchronized (User.class) { // 同步块确保只有一个线程可以初始化实例
                if (instance == null) { // 双重检查锁定，确保只有一个实例被创建
                    instance = new User();
                }
            }
        }
        return instance;
    }

    /**
     * 设置User类的唯一实例。
     * @param instance User类的唯一实例
     */
    public static void setInstance(User instance) {
        User.instance = instance;
    }
}
