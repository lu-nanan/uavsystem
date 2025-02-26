package com.hnu.Mapper;

import com.hnu.entity.TaskExample;
import com.hnu.entity.TaskInfo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface TaskInfoMapper {
    List<TaskInfo> findAllTaskInfo();
    List<TaskInfo> findTaskByUId(int userid);
    TaskInfo findTaskByTid(int id);
    TaskInfo findTaskByDate(Date date);
    int addTask(TaskInfo taskInfo);
    int deleteTaskByTId(int id);
    int updateTaskStatByTId(@Param("taskstate") String taskstate, @Param("id") int id);
    TaskInfo findTaskByDateID(@Param("time") Date date,@Param("userid") int userid);
    List<TaskInfo> findTaskByContentID(@Param("content") String content, @Param("userid") int userid);
    List<TaskInfo> findTaskBystateID(@Param("taskstate") String taskstate, @Param("userid") int userid);
    int updateContentByTId(@Param("content") String content,@Param("userid") int id);
    List<TaskExample> selectTaskResult();
    List<TaskExample> selectTaskResultbyUid(int userid);
    List<TaskExample> selectTaskResultbyDid(@Param("time") Date time,@Param("userid") int userid);
    List<TaskExample> selectTaskResultbyCid(@Param("content") String content,@Param("userid") int userid);
    List<TaskExample> selectTaskResultbySid(@Param("taskstate") String taskstate,@Param("userid") int userid);
    List<TaskExample> selectTaskResultbyTid(@Param("id") int id,@Param("userid") int userid);
    List<TaskExample> selectTaskResultbyUav(@Param("deviceid") int deviceid,@Param("userid") int userid);
}
