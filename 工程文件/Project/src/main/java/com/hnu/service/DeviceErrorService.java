package com.hnu.service;

import com.hnu.Mapper.DeviceErrorMapper;
import com.hnu.entity.DeviceError;
import com.hnu.entity.User;
import com.hnu.util.SqlSessionUtil;
import org.apache.ibatis.session.SqlSession;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DeviceErrorService {
    //private SqlSession session = SqlSessionUtil.openSession();
    // private DeviceErrorMapper deviceErrorMapper=session.getMapper(DeviceErrorMapper.class);
    public List<DeviceError> findAllDeviceErrors(){
        SqlSession session = SqlSessionUtil.openSession();
        DeviceErrorMapper deviceErrorMapper=session.getMapper(DeviceErrorMapper.class);
        List<DeviceError> deviceErrors = deviceErrorMapper.findAllDeviceErrors();
        session.close();
        return deviceErrors;
    }
    public List<DeviceError> findDeviceErrorByDId(int deviceid){
        SqlSession session = SqlSessionUtil.openSession();
        DeviceErrorMapper deviceErrorMapper=session.getMapper(DeviceErrorMapper.class);
        List<DeviceError> deviceErrors=deviceErrorMapper.findDeviceErrorByDId(deviceid);
        session.close();
        return deviceErrors;
    }
    public DeviceError findDeviceActionByEID(int errorid){
        SqlSession session = SqlSessionUtil.openSession();
        DeviceErrorMapper deviceErrorMapper=session.getMapper(DeviceErrorMapper.class);
        DeviceError deviceError=deviceErrorMapper.findDeviceActionByEID(errorid);
        session.close();
        return deviceError;
    }
    public List<DeviceError> findDeviceErrorBytime(Date date){
        SqlSession session = SqlSessionUtil.openSession();
        DeviceErrorMapper deviceErrorMapper=session.getMapper(DeviceErrorMapper.class);
        List<DeviceError> deviceErrors=deviceErrorMapper.findDeviceErrorBytime(date);
        session.close();
        return deviceErrors;
    }
    public int addDeviceError(DeviceError deviceError){
        SqlSession session = SqlSessionUtil.openSession();
        DeviceErrorMapper deviceErrorMapper=session.getMapper(DeviceErrorMapper.class);
        int i=deviceErrorMapper.addDeviceError(deviceError);
        session.commit();
        session.close();
        return i;
    }
    public int deleteDeviceErrorByEId(int errorid){
        SqlSession session = SqlSessionUtil.openSession();
        DeviceErrorMapper deviceErrorMapper=session.getMapper(DeviceErrorMapper.class);
        int i=deviceErrorMapper.deleteDeviceErrorByEId(errorid);
        session.commit();
        session.close();
        return i;
    }
    public int deleteDeviceActionByDId(int deviceid){
        SqlSession session = SqlSessionUtil.openSession();
        DeviceErrorMapper deviceErrorMapper=session.getMapper(DeviceErrorMapper.class);
        int i=deviceErrorMapper.deleteDeviceErrorByDId(deviceid);
        session.commit();
        session.close();
        return i;
    }

    public int updateErrorByEId(int istorepair,int errorid){
        SqlSession session = SqlSessionUtil.openSession();
        DeviceErrorMapper deviceErrorMapper=session.getMapper(DeviceErrorMapper.class);
        int i=deviceErrorMapper.updateErrorByEId(istorepair,errorid);
        session.commit();
        session.close();
        return i;
    }
    public String getIsRepair(int i){
        String message;
        if(i==0){
            message="未送修";

        }else {
            message="已送修";
        }
        return message;
    }
    public void close(){
    }
    public int getPages(List<DeviceError> deviceErrors){
        int lengh=deviceErrors.size();
        if(lengh%8==0){
            return lengh/8;
        }else {
            return lengh/8+1;
        }
    }
    public List<DeviceError> findDeviceErrorByRepair(int istorepair){
        SqlSession session = SqlSessionUtil.openSession();
        DeviceErrorMapper deviceErrorMapper=session.getMapper(DeviceErrorMapper.class);
        return deviceErrorMapper.findDeviceErrorByRepair(istorepair);
    }
    public List<DeviceError> searchError(String searchInput,String searchOption){
        List<DeviceError> deviceErrors=new ArrayList<>();
        SqlSession session = SqlSessionUtil.openSession();
        DeviceErrorMapper deviceErrorMapper=session.getMapper(DeviceErrorMapper.class);
        if(searchInput==null){
            deviceErrors=deviceErrorMapper.findAllDeviceErrors();
            session.close();
            return deviceErrors;
        }
        if(searchOption.equals("ID")){
            deviceErrors.add(deviceErrorMapper.findDeviceActionByEID(Integer.valueOf(searchInput)));
        } else if (searchOption.equals("uavId")) {
            deviceErrors=deviceErrorMapper.findDeviceErrorByDId(Integer.valueOf(searchInput));
        } else if (searchOption.equals("isSendToRepair")) {
            int i=0;
            if(searchInput.equals("已送修")){
                i=1;
            }else {
                i=0;
            }
            deviceErrors=deviceErrorMapper.findDeviceErrorByRepair(i);

        }else {
            deviceErrors=deviceErrorMapper.findAllDeviceErrors();
        }
        session.close();
        return deviceErrors;
    }
}
