package com.hnu.dao;

import com.hnu.entity.DeviceInfo;

import java.util.List;

public interface DeviceInfoDao {
    DeviceInfo findDeviceById(int id);
    List<DeviceInfo> findAllDevices();
    List<DeviceInfo>findDeviceByType(String type);
    void addDevice(DeviceInfo deviceInfo);
    void deleteDeviceById(int id);
}
/* 隐藏了数据库访问的具体实现细节 */