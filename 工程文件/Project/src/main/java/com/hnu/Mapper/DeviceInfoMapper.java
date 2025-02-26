package com.hnu.Mapper;

import com.hnu.entity.DeviceInfo;

import java.util.List;

public interface DeviceInfoMapper {
    DeviceInfo findDeviceById(int id);
    List<DeviceInfo>findAllDevices();
    List<DeviceInfo>findDeviceByType(String type);
    int addDevice(DeviceInfo deviceInfo);
    int deleteDeviceById(int id);
    List<DeviceInfo>findDeviceByProductor(String productor);
    List<Integer> selectAvailableDroneIds();
}
