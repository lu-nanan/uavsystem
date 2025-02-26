package com.hnu.Mapper;

import com.hnu.entity.DeviceError;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface DeviceErrorMapper {
    List<DeviceError> findAllDeviceErrors();
    List<DeviceError> findDeviceErrorByDId(int deviceid);
    DeviceError findDeviceActionByEID(int errorid);
    List<DeviceError> findDeviceErrorByRepair(int istorepair);
    List<DeviceError> findDeviceErrorBytime(Date date);
    int addDeviceError(DeviceError deviceError);
    int deleteDeviceErrorByEId(int errorid);
    int deleteDeviceErrorByDId(int deviceid);
    int updateErrorByEId(@Param("istorepair") int istorepair, @Param("errorid") int errorid);
}
