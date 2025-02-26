package com.hnu.controller;

import com.hnu.entity.*;
import com.hnu.service.DeviceRepairService;
import com.hnu.service.DeviceStateService;
import com.hnu.service.TaskInfoService;
import com.hnu.service.UserHistoryService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.json.simple.JSONArray;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static java.lang.Math.min;
@WebServlet(name = "CompeletRepairServlet", urlPatterns = {"/uavOmEnding-servlet"})
public class CompeletRepairServlet extends HttpServlet {
    private DeviceRepairService deviceRepairService=new DeviceRepairService();
    private UserHistoryService userHistoryService=new UserHistoryService();
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json; charset=UTF-8");
        String id=req.getParameter("id");
        DeviceRepair deviceRepair=deviceRepairService.findDeviceRepairByRID(Integer.valueOf(id));
        if(deviceRepair.getProblem()==null){
            resp.getWriter().write(new JSONObject().put("result", "error").put("message", "未检修").toString());
        }
        int i=deviceRepairService.updatePepairbyDate(new Date(),Integer.valueOf(id));
        User user=new User().getInstance();
        int userId=user.getId();
        /*
        在完成修理之后，就将状态改为”空闲中“
         */
        DeviceStateService deviceStateService=new DeviceStateService();
        DeviceState deviceState = deviceStateService.findStateById(deviceRepair.getDeviceid());
        int j;
        if (deviceState.getAction().equals("故障")) {
            j = deviceStateService.updateActionById("空闲中",deviceRepair.getDeviceid());
        } else {
            TaskInfoService taskInfoService = new TaskInfoService();
            List<TaskExample> taskInfo = taskInfoService.selectTaskResultbyUav(deviceRepair.getDeviceid(),userId);
            int flag = 0;
            if (!taskInfo.isEmpty()) {
                for (TaskExample taskExample : taskInfo) {
                    if (taskExample.getTaskstate().equals("进行中")){
                        flag = 1;
                    }
                }
            }
            if (flag == 1){
                j = deviceStateService.updateActionById("任务中",deviceRepair.getDeviceid());
            }
            else {
                j = deviceStateService.updateActionById("空闲中",deviceRepair.getDeviceid());
            }
        }
        if(i>0&&j>0){
            String history="无人机故障"+userId+"修理完成";
            userHistoryService.add(history,userId);
            System.out.println("ok");
            resp.getWriter().write(new JSONObject().put("result", "200").put("message", "处理成功").toString());
            resp.sendRedirect("/UVASystem_war_exploded/html/uavOm.html");
        }else{
            System.out.println("s");
            resp.getWriter().write(new JSONObject().put("result", "error").put("message", "处理失败").toString());
        }
        deviceRepairService.close();
        userHistoryService.close();

    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doPost(request, response);
    }
}

