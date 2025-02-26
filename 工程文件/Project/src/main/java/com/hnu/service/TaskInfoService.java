package com.hnu.service;

import com.hnu.Mapper.TaskInfoMapper;
import com.hnu.entity.TaskExample;
import com.hnu.entity.TaskInfo;
import com.hnu.util.SqlSessionUtil;
import org.apache.ibatis.session.SqlSession;

import java.util.Date;
import java.util.List;

public class TaskInfoService {
    public List<TaskInfo> findAllTaskInfo() {
        SqlSession session = SqlSessionUtil.openSession();
        TaskInfoMapper taskInfoMapper = session.getMapper(TaskInfoMapper.class);
        List<TaskInfo> taskInfos = taskInfoMapper.findAllTaskInfo();
        session.close();
        return taskInfos;
    }

    public List<TaskInfo> findTaskByUId(int userid) {
        SqlSession session = SqlSessionUtil.openSession();
        TaskInfoMapper taskInfoMapper = session.getMapper(TaskInfoMapper.class);
        List<TaskInfo> taskInfos = taskInfoMapper.findTaskByUId(userid);
        session.close();
        return taskInfos;
    }

    public TaskInfo findTaskByTid(int id) {
        SqlSession session = SqlSessionUtil.openSession();
        TaskInfoMapper taskInfoMapper = session.getMapper(TaskInfoMapper.class);
        TaskInfo taskInfo = taskInfoMapper.findTaskByTid(id);
        session.close();
        return taskInfo;
    }

    public TaskInfo findTaskByDate(Date date) {
        SqlSession session = SqlSessionUtil.openSession();
        TaskInfoMapper taskInfoMapper = session.getMapper(TaskInfoMapper.class);
        TaskInfo taskInfo = taskInfoMapper.findTaskByDate(date);
        session.close();
        return taskInfo;
    }

    public int addTask(TaskInfo taskInfo) {
        SqlSession session = SqlSessionUtil.openSession();
        TaskInfoMapper taskInfoMapper = session.getMapper(TaskInfoMapper.class);
        int result = taskInfoMapper.addTask(taskInfo);
        session.commit();
        session.close();
        return result;
    }

    public int deleteTaskByTId(int id) {
        SqlSession session = SqlSessionUtil.openSession();
        TaskInfoMapper taskInfoMapper = session.getMapper(TaskInfoMapper.class);
        int result = taskInfoMapper.deleteTaskByTId(id);
        session.commit();
        session.close();
        return result;
    }

    public int updateTaskStatByTId(String taskstate, int id) {
        SqlSession session = SqlSessionUtil.openSession();
        TaskInfoMapper taskInfoMapper = session.getMapper(TaskInfoMapper.class);
        int result = taskInfoMapper.updateTaskStatByTId(taskstate, id);
        session.commit();
        session.close();
        return result;
    }

    public TaskInfo findTaskByDateID(Date date, int id) {
        SqlSession session = SqlSessionUtil.openSession();
        TaskInfoMapper taskInfoMapper = session.getMapper(TaskInfoMapper.class);
        TaskInfo taskInfo = taskInfoMapper.findTaskByDateID(date, id);
        session.close();
        return taskInfo;
    }

    public List<TaskInfo> findTaskByContentID(String content, int id) {
        SqlSession session = SqlSessionUtil.openSession();
        TaskInfoMapper taskInfoMapper = session.getMapper(TaskInfoMapper.class);
        List<TaskInfo> taskInfos = taskInfoMapper.findTaskByContentID(content, id);
        session.close();
        return taskInfos;
    }

    public List<TaskInfo> findTaskBystateID(String taskstate, int id) {
        SqlSession session = SqlSessionUtil.openSession();
        TaskInfoMapper taskInfoMapper = session.getMapper(TaskInfoMapper.class);
        List<TaskInfo> taskInfos = taskInfoMapper.findTaskBystateID(taskstate, id);
        session.close();
        return taskInfos;
    }

    public void close() {
        // No operation needed as each method closes the session individually
    }

    public int updateContentByTId(String content, int id) {
        SqlSession session = SqlSessionUtil.openSession();
        TaskInfoMapper taskInfoMapper = session.getMapper(TaskInfoMapper.class);
        int result = taskInfoMapper.updateContentByTId(content, id);
        session.commit();
        session.close();
        return result;
    }

    public int getPages(List<TaskInfo> taskInfos) {
        int length = taskInfos.size();
        if (length % 6 == 0) {
            return length / 6;
        } else {
            return length / 6 + 1;
        }
    }

    public List<TaskExample> selectTaskResult() {
        SqlSession session = SqlSessionUtil.openSession();
        TaskInfoMapper taskInfoMapper = session.getMapper(TaskInfoMapper.class);
        List<TaskExample> taskExamples = taskInfoMapper.selectTaskResult();
        session.close();
        return taskExamples;
    }

    public List<TaskExample> selectTaskResultbyUid(int userid) {
        SqlSession session = SqlSessionUtil.openSession();
        TaskInfoMapper taskInfoMapper = session.getMapper(TaskInfoMapper.class);
        List<TaskExample> taskExamples = taskInfoMapper.selectTaskResultbyUid(userid);
        session.close();
        return taskExamples;
    }

    public List<TaskExample> selectTaskResultbyDid(Date time, int userid) {
        SqlSession session = SqlSessionUtil.openSession();
        TaskInfoMapper taskInfoMapper = session.getMapper(TaskInfoMapper.class);
        List<TaskExample> taskExamples = taskInfoMapper.selectTaskResultbyDid(time, userid);
        session.close();
        return taskExamples;
    }

    public List<TaskExample> selectTaskResultbyCid(String content, int userid) {
        SqlSession session = SqlSessionUtil.openSession();
        TaskInfoMapper taskInfoMapper = session.getMapper(TaskInfoMapper.class);
        List<TaskExample> taskExamples = taskInfoMapper.selectTaskResultbyCid(content, userid);
        session.close();
        return taskExamples;
    }

    public List<TaskExample> selectTaskResultbySid(String taskstate, int userid) {
        SqlSession session = SqlSessionUtil.openSession();
        TaskInfoMapper taskInfoMapper = session.getMapper(TaskInfoMapper.class);
        List<TaskExample> taskExamples = taskInfoMapper.selectTaskResultbySid(taskstate, userid);
        session.close();
        return taskExamples;
    }

    public List<TaskExample> selectTaskResultbyTid(int id, int userid) {
        SqlSession session = SqlSessionUtil.openSession();
        TaskInfoMapper taskInfoMapper = session.getMapper(TaskInfoMapper.class);
        List<TaskExample> taskExamples = taskInfoMapper.selectTaskResultbyTid(id, userid);
        session.close();
        return taskExamples;
    }

    public List<TaskExample> selectTaskResultbyUav(int deviceid, int userid) {
        SqlSession session = SqlSessionUtil.openSession();
        TaskInfoMapper taskInfoMapper = session.getMapper(TaskInfoMapper.class);
        List<TaskExample> taskExamples = taskInfoMapper.selectTaskResultbyUav(deviceid, userid);
        session.close();
        return taskExamples;
    }

    public Boolean check(String content) {
        Boolean flag = false;
        String[] parts = content.split(" ");
        String[] tasks = {"浇水", "施肥", "虫害情况勘察", "农作物生长状态勘察"};
        if (parts.length < 3) {
            System.out.println(1);
            return false;
        }
        int i = Integer.valueOf(parts[0]);
        int j = Integer.valueOf(parts[1]);
        if (parts.length != 3) {
            System.out.println(1);
            return false;
        }
        if (i < 0 || i > 100) {
            System.out.println(2);
            return false;
        }
        if (j < 0 || j > 100) {
            System.out.println(3);
            return false;
        }
        for (String task : tasks) {
            if (task.equals(parts[2])) {
                System.out.println(4);
                return true;
            }
        }
        return false;
    }
}