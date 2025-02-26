package com.hnu.service;

import com.hnu.Mapper.DeviceTaskMapper;
import com.hnu.entity.DeviceTask;
import com.hnu.util.SqlSessionUtil;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class DeviceTaskService {
    public List<DeviceTask> findAllTask() {
        SqlSession session = SqlSessionUtil.openSession();
        DeviceTaskMapper deviceTaskMapper = session.getMapper(DeviceTaskMapper.class);
        List<DeviceTask> tasks = deviceTaskMapper.findAllTask();
        session.close();
        return tasks;
    }

    public List<DeviceTask> findTaskByDId(int deviceid) {
        SqlSession session = SqlSessionUtil.openSession();
        DeviceTaskMapper deviceTaskMapper = session.getMapper(DeviceTaskMapper.class);
        List<DeviceTask> tasks = deviceTaskMapper.findTaskByDId(deviceid);
        session.close();
        return tasks;
    }

    public DeviceTask findTaskByTID(int taskid) {
        SqlSession session = SqlSessionUtil.openSession();
        DeviceTaskMapper deviceTaskMapper = session.getMapper(DeviceTaskMapper.class);
        DeviceTask task = deviceTaskMapper.findTaskByTID(taskid);
        session.close();
        return task;
    }

    public int addTask(DeviceTask deviceTask) {
        SqlSession session = SqlSessionUtil.openSession();
        DeviceTaskMapper deviceTaskMapper = session.getMapper(DeviceTaskMapper.class);
        int result = deviceTaskMapper.addTask(deviceTask);
        session.commit();
        session.close();
        return result;
    }

    public int deleteTaskByDId(int deviceid) {
        SqlSession session = SqlSessionUtil.openSession();
        DeviceTaskMapper deviceTaskMapper = session.getMapper(DeviceTaskMapper.class);
        int result = deviceTaskMapper.deleteTaskByDId(deviceid);
        session.commit();
        session.close();
        return result;
    }

    public int deleteTaskByTId(int taskid) {
        SqlSession session = SqlSessionUtil.openSession();
        DeviceTaskMapper deviceTaskMapper = session.getMapper(DeviceTaskMapper.class);
        int result = deviceTaskMapper.deleteTaskByTId(taskid);
        session.commit();
        session.close();
        return result;
    }

    public int updateTaskByTId(int deviceid, int taskid) {
        SqlSession session = SqlSessionUtil.openSession();
        DeviceTaskMapper deviceTaskMapper = session.getMapper(DeviceTaskMapper.class);
        int result = deviceTaskMapper.updateTaskByTId(deviceid, taskid);
        session.commit();
        session.close();
        return result;
    }

    public int getPages(List<DeviceTask> deviceTasks) {
        int length = deviceTasks.size();
        if (length % 8 == 0) {
            return length / 8;
        } else {
            return length / 8 + 1;
        }
    }
    public void close(){
    }
}