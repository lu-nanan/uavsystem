package com.hnu.controller;

import com.hnu.entity.TaskExample;
import com.hnu.entity.User;
import com.hnu.service.DeviceStateService;
import com.hnu.service.LogService;
import com.hnu.service.TaskInfoService;
import com.hnu.service.UserHistoryService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static java.lang.Math.min;

@WebServlet(name = "DeletTaskInfoServlet", urlPatterns = {"/taskDel-servlet"})
public class DeletTaskInfoServlet extends HttpServlet {
    private TaskInfoService taskInfoService = new TaskInfoService();
    private LogService logService=new LogService();
    private UserHistoryService userHistoryService=new UserHistoryService();
    private DeviceStateService deviceStateService = new DeviceStateService();
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("application/json; charset=UTF-8");
        String taskIdStr = request.getParameter("id");
        User current=new User().getInstance();
        int id=current.getId();
        if (taskIdStr!= null) {
            int taskId = Integer.parseInt(taskIdStr);
            // 调用服务层方法删除任务
            List<TaskExample>  taskExamples = taskInfoService.selectTaskResultbyTid(taskId,id);
            TaskExample taskExample = taskExamples.get(0);
            int result2 = 1;
            if (taskExample.getTaskstate().equals("进行中")){
                result2 = deviceStateService.updateActionById("空闲中",taskExample.getDeviceid());
            }
            int result1 = taskInfoService.deleteTaskByTId(taskId);
            JSONObject responseJson = new JSONObject();
            if (result1 > 0 && result2 > 0) {
                String history="用户"+id+"删除任务"+taskIdStr;
                userHistoryService.add(history,id);
                responseJson.put("message", "任务删除成功");
                response.sendRedirect("/UVASystem_war_exploded/html/taskList.html");
            } else {
                logService.addLogToFile(id,"任务删除失败","error","DeletTaskInfoServlet");
                responseJson.put("message", "任务删除失败");
                response.sendRedirect("/UVASystem_war_exploded/html/taskList.html");
            }
            PrintWriter out = response.getWriter();
            out.print(responseJson.toJSONString());
            out.flush();
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JSONObject errorJson = new JSONObject();
            errorJson.put("message", "缺少任务ID参数");
            PrintWriter out = response.getWriter();
            out.print(errorJson.toJSONString());
            out.flush();
            response.sendRedirect("/UVASystem_war_exploded/html/taskList.html");
        }
        taskInfoService.close();
        userHistoryService.close();
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doPost(request, response);
    }

}