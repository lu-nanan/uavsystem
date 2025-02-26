package com.hnu.controller;

import com.hnu.Mapper.DeviceRepairMapper;
import com.hnu.entity.DeviceRepair;
import com.hnu.util.SqlSessionUtil;
import org.apache.ibatis.session.SqlSession;

import java.util.Date;
import java.util.List;

public class DeviceRepairService {
    private SqlSession session = SqlSessionUtil.openSession();
    private DeviceRepairMapper deviceRepairMapper=session.getMapper(DeviceRepairMapper.class);
    public List<DeviceRepair> findAllDeviceRepair(){
        return deviceRepairMapper.findAllDeviceRepair();
    }
    public List<DeviceRepair> findDeviceRepairByDId(int deviceid){
        return deviceRepairMapper.findDeviceRepairByDId(deviceid);
    }
    public DeviceRepair findDeviceRepairByRID(int reid){
        return deviceRepairMapper.findDeviceRepairByRID(reid);
    }
    public List<DeviceRepair> findDeviceRepairByCtime(Date date){
        return deviceRepairMapper.findDeviceRepairByCtime(date);
    }
    public int addDeviceRepair(DeviceRepair deviceRepair){
        int i=deviceRepairMapper.addDeviceRepair(deviceRepair);
        session.commit();
        return i;
    }
    public int deleteDeviceRepairByRId(int reid){
        int i=deviceRepairMapper.deleteDeviceRepairByRId(reid);
        session.commit();
        return i;
    }
    public int deleteDeviceRepairByDId(int deviceid){
        int i=deviceRepairMapper.deleteDeviceRepairByDId(deviceid);
        session.commit();
        return i;
    }
    public int updateActionHandler( String handler,int reid){
        int i=deviceRepairMapper.updateActionHandler(handler,reid);
        session.commit();
        return i;
    }
    public int updateAllFields(String problem, int handler, Date completetime, int id){
        int i=deviceRepairMapper.updateAllFields(problem,handler,completetime,id);
        session.commit();
        return i;
    }
    public void close(){
        session.commit();
    }

}

