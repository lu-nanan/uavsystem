package com.hnu.service;

import com.hnu.Mapper.DeviceInfoMapper;
import com.hnu.entity.DeviceInfo;
import com.hnu.entity.User;
import com.hnu.util.SqlSessionUtil;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

/*public class DeviceInfoService {
    private SqlSession session = SqlSessionUtil.openSession();
    private DeviceInfoMapper deviceInfoMapper=session.getMapper(DeviceInfoMapper.class);
    public DeviceInfo findDeviceById(int id){
        return deviceInfoMapper.findDeviceById(id);
    }
    public List<DeviceInfo> findAllDevices(){
        return deviceInfoMapper.findAllDevices();
    }
    public List<DeviceInfo>findDeviceByType(String type){
        return deviceInfoMapper.findDeviceByType(type);
    }
    public Boolean addDevice(DeviceInfo deviceInfo){
        int i=deviceInfoMapper.addDevice(deviceInfo);
        if(i>0){
            session.commit();
            return true;
        }else {
            return false;
        }

    }
    public Boolean deleteDeviceById(int id){
        SqlSession session1 = SqlSessionUtil.openSession();
        DeviceInfoMapper deviceInfoMapper1=session1.getMapper(DeviceInfoMapper.class);
        int i=deviceInfoMapper1.deleteDeviceById(id);
        session1.commit();
        session1.close();
        if(i>0){
            return true;
        }else {
            return false;
        }

    }
    public List<DeviceInfo>findDeviceByProductor(String productor){
        return deviceInfoMapper.findDeviceByProductor(productor);
    }
    public void close () {
        session.commit();
    }
    public List<Integer> selectAvailableDroneIds(){
        return deviceInfoMapper.selectAvailableDroneIds();
    }
    public int getPages(List<DeviceInfo> deviceInfos){
        int lengh=deviceInfos.size();
        if(lengh%6==0){
            return lengh/8;
        }else {
            return lengh/8+1;
        }
    }
}


 */

import com.hnu.Mapper.DeviceInfoMapper;
import com.hnu.entity.DeviceInfo;
import com.hnu.util.SqlSessionUtil;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class DeviceInfoService {
    public DeviceInfo findDeviceById(int id) {
        SqlSession session = SqlSessionUtil.openSession();
        DeviceInfoMapper deviceInfoMapper = session.getMapper(DeviceInfoMapper.class);
        DeviceInfo deviceInfo = deviceInfoMapper.findDeviceById(id);
        session.close();
        return deviceInfo;
    }

    public List<DeviceInfo> findAllDevices() {
        SqlSession session = SqlSessionUtil.openSession();
        DeviceInfoMapper deviceInfoMapper = session.getMapper(DeviceInfoMapper.class);
        List<DeviceInfo> devices = deviceInfoMapper.findAllDevices();
        session.close();
        return devices;
    }

    public List<DeviceInfo> findDeviceByType(String type) {
        SqlSession session = SqlSessionUtil.openSession();
        DeviceInfoMapper deviceInfoMapper = session.getMapper(DeviceInfoMapper.class);
        List<DeviceInfo> devices = deviceInfoMapper.findDeviceByType(type);
        session.close();
        return devices;
    }

    public Boolean addDevice(DeviceInfo deviceInfo) {
        SqlSession session = SqlSessionUtil.openSession();
        DeviceInfoMapper deviceInfoMapper = session.getMapper(DeviceInfoMapper.class);
        int result = deviceInfoMapper.addDevice(deviceInfo);
        session.commit();
        session.close();
        return result > 0;
    }

    public Boolean deleteDeviceById(int id) {
        SqlSession session = SqlSessionUtil.openSession();
        DeviceInfoMapper deviceInfoMapper = session.getMapper(DeviceInfoMapper.class);
        int result = deviceInfoMapper.deleteDeviceById(id);
        session.commit();
        session.close();
        return result > 0;
    }

    public List<DeviceInfo> findDeviceByProductor(String productor) {
        SqlSession session = SqlSessionUtil.openSession();
        DeviceInfoMapper deviceInfoMapper = session.getMapper(DeviceInfoMapper.class);
        List<DeviceInfo> devices = deviceInfoMapper.findDeviceByProductor(productor);
        session.close();
        return devices;
    }

    public List<Integer> selectAvailableDroneIds() {
        SqlSession session = SqlSessionUtil.openSession();
        DeviceInfoMapper deviceInfoMapper = session.getMapper(DeviceInfoMapper.class);
        List<Integer> ids = deviceInfoMapper.selectAvailableDroneIds();
        session.close();
        return ids;
    }

    public int getPages(List<DeviceInfo> deviceInfos) {
        int length = deviceInfos.size();
        if (length % 8 == 0) {
            return length / 8;
        } else {
            return length / 8 + 1;
        }
    }
    public void close(){
    }
}