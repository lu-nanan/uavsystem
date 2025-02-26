package com.hnu.controller;

import com.hnu.entity.DeviceTask;
import com.hnu.entity.TaskInfo;
import com.hnu.entity.User;
import com.hnu.service.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@WebServlet(name = "EditTaskServlet", urlPatterns = {"/taskEdit-servlet"})
public class EditTaskServlet extends HttpServlet {
    private TaskInfoService taskInfoService=new TaskInfoService();
    private DeviceTaskService deviceTaskService=new DeviceTaskService();
    private DeviceInfoService deviceInfoService=new DeviceInfoService();
    private UserHistoryService userHistoryService=new UserHistoryService();
    private LogService logService=new LogService();
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json; charset=UTF-8");//taskContent，carryOutUav，taskId
        String taskContent=req.getParameter("taskContent");
        String carryOutUav=req.getParameter("carryOutUav");
        String taskId=req.getParameter("taskId");
        int taskid=Integer.valueOf(taskId);
        int deviceid=Integer.valueOf(carryOutUav);
        Boolean flag=isAvalible(deviceid);
        User current=new User().getInstance();
        int id=current.getId();
        Boolean flag1=taskInfoService.check(taskContent);
        if(!flag1){
            logService.addLogToFile(id,"任务格式错误","info","EditTaskServlet");
            resp.getWriter().write(new JSONObject().put("result", "error").put("message", "任务格式错误").toString());
            return;
        }
        if(!flag){
            logService.addLogToFile(id,"无空闲无人机","info","EditTaskServlet");
            resp.getWriter().write(new JSONObject().put("result", "error").put("message", "无人机进行任务中").toString());
            return;
        }

        int i=taskInfoService.updateContentByTId(taskContent,taskid);
        int j=deviceTaskService.updateTaskByTId(deviceid,taskid);

        if (i>0&&j>0){
            String history="用户"+id+"修改无人机任务"+taskId;
            userHistoryService.add(history,id);
            resp.getWriter().write(new JSONObject().put("result", "200").put("message", "修改成功").toString());
        }else {
            logService.addLogToFile(id,"编辑任务失败","error","EditTaskServlet");
            resp.getWriter().write(new JSONObject().put("result", "error").put("message", "修改失败").toString());
        }
        taskInfoService.close();
        deviceInfoService.close();
        deviceTaskService.close();
        userHistoryService.close();

    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }
    public Boolean isAvalible(int i){
        List<Integer> ids=deviceInfoService.selectAvailableDroneIds();
        int length=ids.size();
        for (int j=0;j<length;j++){
            if(ids.get(j)==i){
                return true;
            }
        }
        return false;
    }

}
