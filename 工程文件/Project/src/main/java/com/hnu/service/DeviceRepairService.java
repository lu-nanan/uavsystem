package com.hnu.service;

import com.hnu.Mapper.DeviceRepairMapper;
import com.hnu.entity.DeviceRepair;
import com.hnu.util.SqlSessionUtil;
import org.apache.ibatis.session.SqlSession;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DeviceRepairService {
    public List<DeviceRepair> findAllDeviceRepair() {
        SqlSession session = SqlSessionUtil.openSession();
        DeviceRepairMapper deviceRepairMapper = session.getMapper(DeviceRepairMapper.class);
        List<DeviceRepair> deviceRepairs = deviceRepairMapper.findAllDeviceRepair();
        session.close();
        return deviceRepairs;
    }

    public List<DeviceRepair> findDeviceRepairByDId(int deviceid) {
        SqlSession session = SqlSessionUtil.openSession();
        DeviceRepairMapper deviceRepairMapper = session.getMapper(DeviceRepairMapper.class);
        List<DeviceRepair> deviceRepairs = deviceRepairMapper.findDeviceRepairByDId(deviceid);
        session.close();
        return deviceRepairs;
    }

    public DeviceRepair findDeviceRepairByRID(int reid) {
        SqlSession session = SqlSessionUtil.openSession();
        DeviceRepairMapper deviceRepairMapper = session.getMapper(DeviceRepairMapper.class);
        DeviceRepair deviceRepair = deviceRepairMapper.findDeviceRepairByRID(reid);
        session.close();
        return deviceRepair;
    }

    public List<DeviceRepair> findDeviceRepairByCtime(Date date) {
        SqlSession session = SqlSessionUtil.openSession();
        DeviceRepairMapper deviceRepairMapper = session.getMapper(DeviceRepairMapper.class);
        List<DeviceRepair> deviceRepairs = deviceRepairMapper.findDeviceRepairByCtime(date);
        session.close();
        return deviceRepairs;
    }

    public int addDeviceRepair(DeviceRepair deviceRepair) {
        SqlSession session = SqlSessionUtil.openSession();
        DeviceRepairMapper deviceRepairMapper = session.getMapper(DeviceRepairMapper.class);
        int result = deviceRepairMapper.addDeviceRepair(deviceRepair);
        session.commit();
        session.close();
        return result;
    }

    public int deleteDeviceRepairByRId(int reid) {
        SqlSession session = SqlSessionUtil.openSession();
        DeviceRepairMapper deviceRepairMapper = session.getMapper(DeviceRepairMapper.class);
        int result = deviceRepairMapper.deleteDeviceRepairByRId(reid);
        session.commit();
        session.close();
        return result;
    }

    public int deleteDeviceRepairByDId(int deviceid) {
        SqlSession session = SqlSessionUtil.openSession();
        DeviceRepairMapper deviceRepairMapper = session.getMapper(DeviceRepairMapper.class);
        int result = deviceRepairMapper.deleteDeviceRepairByDId(deviceid);
        session.commit();
        session.close();
        return result;
    }

    public int updateActionHandler(String handler, int reid) {
        SqlSession session = SqlSessionUtil.openSession();
        DeviceRepairMapper deviceRepairMapper = session.getMapper(DeviceRepairMapper.class);
        int result = deviceRepairMapper.updateActionHandler(handler, reid);
        session.commit();
        session.close();
        return result;
    }

    public int updateAllFields(String problem, int handler, Date completetime, int id) {
        SqlSession session = SqlSessionUtil.openSession();
        DeviceRepairMapper deviceRepairMapper = session.getMapper(DeviceRepairMapper.class);
        int result = deviceRepairMapper.updateAllFields(problem, handler, completetime, id);
        session.commit();
        session.close();
        return result;
    }

    public List<DeviceRepair> searchDRepair(String searchKeyword, String searchBy) {
        List<DeviceRepair> deviceRepairs = new ArrayList<>();
        SqlSession session = SqlSessionUtil.openSession();
        DeviceRepairMapper deviceRepairMapper = session.getMapper(DeviceRepairMapper.class);
        if (searchKeyword == null) {
            deviceRepairs = deviceRepairMapper.findAllDeviceRepair();
            session.close();
            return deviceRepairs;
        }
        if ("ID".equals(searchBy)) {
            deviceRepairs.add(deviceRepairMapper.findDeviceRepairByRID(Integer.valueOf(searchKeyword)));
        } else if ("uavId".equals(searchBy)) {
            deviceRepairs = deviceRepairMapper.findDeviceRepairByDId(Integer.valueOf(searchKeyword));
        } else if ("userId".equals(searchBy)) {
            deviceRepairs = deviceRepairMapper.findDeviceRepairByHandler(Integer.valueOf(searchKeyword));
        } else {
            deviceRepairs = deviceRepairMapper.findAllDeviceRepair();
        }
        session.close();
        return deviceRepairs;
    }

    public List<DeviceRepair> findDeviceRepairByHandler(int handler) {
        SqlSession session = SqlSessionUtil.openSession();
        DeviceRepairMapper deviceRepairMapper = session.getMapper(DeviceRepairMapper.class);
        List<DeviceRepair> deviceRepairs = deviceRepairMapper.findDeviceRepairByHandler(handler);
        session.close();
        return deviceRepairs;
    }

    public int getPages(List<DeviceRepair> deviceRepairs) {
        int length = deviceRepairs.size();
        if (length % 8 == 0) {
            return length / 8;
        } else {
            return length / 8 + 1;
        }
    }

    public int updatePepairbyDate(Date date, int reid) {
        SqlSession session = SqlSessionUtil.openSession();
        DeviceRepairMapper deviceRepairMapper = session.getMapper(DeviceRepairMapper.class);
        int result = deviceRepairMapper.updatePepairbyDate(date, reid);
        session.commit();
        session.close();
        return result;
    }

    public int updatePepairbyProblem(String problem, int handler, int reid) {
        SqlSession session = SqlSessionUtil.openSession();
        DeviceRepairMapper deviceRepairMapper = session.getMapper(DeviceRepairMapper.class);
        int result = deviceRepairMapper.updatePepairbyProblem(problem, handler, reid);
        session.commit();
        session.close();
        return result;
    }
    public void close(){
    }
}