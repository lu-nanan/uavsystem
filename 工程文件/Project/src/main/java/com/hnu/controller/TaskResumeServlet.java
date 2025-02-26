package com.hnu.controller;

import com.hnu.entity.TaskExample;
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
import java.util.List;

import static java.lang.Math.min;


@WebServlet(name = "TaskResumeServlet", urlPatterns = {"/taskResume-servlet"})
public class TaskResumeServlet extends HttpServlet {
    private TaskInfoService taskInfoService=new TaskInfoService();
    private LogService logService=new LogService();
    private UserHistoryService userHistoryService=new UserHistoryService();
    private DeviceStateService deviceStateService = new DeviceStateService();

    private DeviceTaskService deviceTaskService = new DeviceTaskService();
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json; charset=UTF-8");

        String id=req.getParameter("id");
        int tid=Integer.valueOf(id);
        User current=new User().getInstance();
        int userid=current.getId();

        List<TaskExample> taskexamples =taskInfoService.selectTaskResultbyTid(tid,userid);
        TaskExample taskInfo = taskexamples.get(0);

        int i = taskInfoService.updateTaskStatByTId("未分配",tid);
        int j = deviceTaskService.deleteTaskByTId(tid);

        if (i>0&&j>0){
            String history="用户"+userid+"继续任务"+tid+" "+taskInfo.getContent();
            userHistoryService.add(history,userid);
            resp.getWriter().write(new JSONObject().put("result", "200").put("message", "继续任务成功").toString());
            resp.sendRedirect("/UVASystem_war_exploded/html/taskList.html");
        }
        else {
            logService.addLogToFile(userid,"继续任务失败","error" ,"TaskResumeServlet");
            resp.getWriter().write(new JSONObject().put("result", "error").put("message", "继续任务失败").toString());
            resp.sendRedirect("/UVASystem_war_exploded/html/taskList.html");
        }
        taskInfoService.close();
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }
}
