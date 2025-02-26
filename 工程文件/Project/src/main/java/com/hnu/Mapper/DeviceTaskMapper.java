package com.hnu.Mapper;

import com.hnu.entity.DeviceTask;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DeviceTaskMapper {
    List<DeviceTask> findAllTask();
    List<DeviceTask> findTaskByDId(int deviceid);
    DeviceTask findTaskByTID(int taskid);
    int addTask(DeviceTask deviceTask);
    int deleteTaskByDId(int deviceid);
    int deleteTaskByTId(int taskid );
    int updateTaskByTId(@Param("deviceid") int deviceid,@Param("taskid") int taskid);

}
