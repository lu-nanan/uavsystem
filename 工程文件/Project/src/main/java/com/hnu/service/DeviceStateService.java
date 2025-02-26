package com.hnu.service;

import com.hnu.Mapper.DeviceStateMapper;
import com.hnu.entity.DeviceState;
import com.hnu.util.SqlSessionUtil;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class DeviceStateService {
    public List<DeviceState> findAllState() {
        SqlSession session = SqlSessionUtil.openSession();
        DeviceStateMapper deviceStateMapper = session.getMapper(DeviceStateMapper.class);
        List<DeviceState> states = deviceStateMapper.findAllState();
        session.close();
        return states;
    }

    public DeviceState findStateById(int id) {
        SqlSession session = SqlSessionUtil.openSession();
        DeviceStateMapper deviceStateMapper = session.getMapper(DeviceStateMapper.class);
        DeviceState state = deviceStateMapper.findStateById(id);
        session.close();
        return state;
    }

    public List<DeviceState> findStateByAction(String action) {
        SqlSession session = SqlSessionUtil.openSession();
        DeviceStateMapper deviceStateMapper = session.getMapper(DeviceStateMapper.class);
        List<DeviceState> states = deviceStateMapper.findStateByAction(action);
        session.close();
        return states;
    }

    public Boolean addState(DeviceState deviceState) {
        SqlSession session = SqlSessionUtil.openSession();
        DeviceStateMapper deviceStateMapper = session.getMapper(DeviceStateMapper.class);
        int result = deviceStateMapper.addState(deviceState);
        session.commit();
        session.close();
        return result > 0;
    }

    public Boolean deleteStateById(int id) {
        SqlSession session = SqlSessionUtil.openSession();
        DeviceStateMapper deviceStateMapper = session.getMapper(DeviceStateMapper.class);
        int result = deviceStateMapper.deleteStateById(id);
        session.commit();
        session.close();
        return result > 0;
    }

    public int updateActionById(String action,int id) {
        SqlSession session = SqlSessionUtil.openSession();
        DeviceStateMapper deviceStateMapper = session.getMapper(DeviceStateMapper.class);
        int result = deviceStateMapper.updateActionById(action,id);
        session.commit();
        session.close();
        return result ;
    }

    public List<DeviceState> findStateByX(double x) {
        SqlSession session = SqlSessionUtil.openSession();
        DeviceStateMapper deviceStateMapper = session.getMapper(DeviceStateMapper.class);
        List<DeviceState> states = deviceStateMapper.findStateByX(x);
        session.close();
        return states;
    }

    public List<DeviceState> findStateByY(double y) {
        SqlSession session = SqlSessionUtil.openSession();
        DeviceStateMapper deviceStateMapper = session.getMapper(DeviceStateMapper.class);
        List<DeviceState> states = deviceStateMapper.findStateByY(y);
        session.close();
        return states;
    }

    public List<DeviceState> findStateByZ(double z) {
        SqlSession session = SqlSessionUtil.openSession();
        DeviceStateMapper deviceStateMapper = session.getMapper(DeviceStateMapper.class);
        List<DeviceState> states = deviceStateMapper.findStateByZ(z);
        session.close();
        return states;
    }

    public int getPages(List<DeviceState> deviceStates) {
        int length = deviceStates.size();
        if (length % 8 == 0) {
            return length / 8;
        } else {
            return length / 8 + 1;
        }
    }
    public void close(){
    }
}