package com.hnu.entity;

public class CunChu {
    private String code;
    private int type;
    private static volatile CunChu instance;
    public CunChu getInstance(){
        if (instance == null) { // 检查实例是否为空
            synchronized (CunChu.class) { // 同步块确保只有一个线程可以初始化实例
                if (instance == null) { // 双重检查锁定，确保只有一个实例被创建
                    instance = new CunChu();
                }
            }
        }
        return instance;
    }

    public static void setInstance(CunChu instance) {
        CunChu.instance = instance;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
