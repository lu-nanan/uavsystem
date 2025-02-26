package com.hnu.dao;

import com.hnu.Mapper.DeviceInfoMapper;
import com.hnu.entity.DeviceInfo;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/*
 * 子类未改变基类的方法签名，无前置后置条件，实现了基类的所有行为
 * */

public class DeviceInfoDaoiml implements DeviceInfoDao {
    private String resource = "mybatis-config.xml";
    private InputStream inputStream = Resources.getResourceAsStream(resource);
    private SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
    private SqlSession session = sqlSessionFactory.openSession();
    private DeviceInfoMapper deviceInfoMapper  = session.getMapper(DeviceInfoMapper.class);

    public DeviceInfoDaoiml() throws IOException {
    }

    @Override
    public DeviceInfo findDeviceById(int id) {
        DeviceInfo deviceInfo=deviceInfoMapper.findDeviceById(id);
        return deviceInfo;
    }

    @Override
    public List<DeviceInfo> findAllDevices() {
        List<DeviceInfo>deviceInfos=deviceInfoMapper.findAllDevices();
        return deviceInfos;
    }

    @Override
    public List<DeviceInfo> findDeviceByType(String type) {
        List<DeviceInfo>deviceInfos=deviceInfoMapper.findDeviceByType(type);
        return deviceInfos;
    }

    @Override
    public void addDevice(DeviceInfo deviceInfo) {
        deviceInfoMapper.addDevice(deviceInfo);

    }

    @Override
    public void deleteDeviceById(int id) {
        deviceInfoMapper.deleteDeviceById(id);
    }
}
