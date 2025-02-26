package com.hnu.Mapper;

import com.hnu.entity.DeviceState;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DeviceStateMapper {
    List<DeviceState> findAllState();
    DeviceState findStateById(int id);
    List<DeviceState> findStateByAction(String action);
    List<DeviceState> findStateByX(double x);
    List<DeviceState>findStateByY(double Y);
    List<DeviceState>findStateByZ(double z);
    int addState(DeviceState deviceState);
    int deleteStateById(int id);
    int updateActionById(@Param("action") String action,@Param("deviceid") int id);

}

