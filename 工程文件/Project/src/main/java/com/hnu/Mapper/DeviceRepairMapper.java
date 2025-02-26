package com.hnu.Mapper;

import com.hnu.entity.DeviceRepair;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface DeviceRepairMapper {
    List<DeviceRepair> findAllDeviceRepair();
    List<DeviceRepair> findDeviceRepairByDId(int deviceid);
    DeviceRepair findDeviceRepairByRID(int reid);
    List<DeviceRepair> findDeviceRepairByCtime(Date date);
    List<DeviceRepair> findDeviceRepairByHandler(int handler);
    int addDeviceRepair(DeviceRepair deviceRepair);
    int deleteDeviceRepairByRId(int reid);
    int deleteDeviceRepairByDId(int deviceid);
    int updateActionHandler(@Param("handler") String handler,@Param("reid") int reid);
    int updateAllFields(@Param("problem") String problem, @Param("handler") int handler, @Param("completetime") Date completetime, @Param("reid") int id);
    int updatePepairbyDate(@Param("completetime") Date date,@Param("reid") int reid);
    int updatePepairbyProblem(@Param("problem") String propblem,@Param("handler")int handler,@Param("reid") int reid);

}
