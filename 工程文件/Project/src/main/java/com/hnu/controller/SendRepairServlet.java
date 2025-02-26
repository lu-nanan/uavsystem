package com.hnu.controller;

import com.hnu.entity.DeviceRepair;
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

@WebServlet(name = "SendRepairServlet", urlPatterns = {"/breakdownSendToRepair-servlet"})
public class SendRepairServlet extends HttpServlet {
    private DeviceRepairService deviceRepairService=new DeviceRepairService();
    private DeviceErrorService deviceErrorService=new DeviceErrorService();
    private UserHistoryService userHistoryService=new UserHistoryService();
    private DeviceStateService deviceStateService=new DeviceStateService();
    private TaskInfoService taskInfoService=new TaskInfoService();
    private DeviceTaskService deviceTaskService=new DeviceTaskService();
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json; charset=UTF-8");
        /*
        breakdownId，userId，后端在无人机维修记录表添加数据（问题，修理完成时间默认为空，维修记录Id尽量与故障Id一致）
         */
        String userId=req.getParameter("userId");
        String uavId=req.getParameter("uavId");//无人机Id
        String breakdownId=req.getParameter("breakdownId");//故障Id
        System.out.println(uavId);
        System.out.println(userId);
        System.out.println(breakdownId);
        int j=1;
        int i=deviceErrorService.updateErrorByEId(j,Integer.valueOf(breakdownId));
        deviceErrorService.close();
        DeviceRepair deviceRepair=new DeviceRepair();
        deviceRepair.setDeviceid(Integer.valueOf(uavId));
        deviceRepair.setReid(Integer.valueOf(breakdownId));
        deviceRepair.setHandler(Integer.valueOf(userId));
        deviceRepair.setTime(new Date());
        deviceRepair.setCompletetime(null);
        deviceRepair.setProblem(null);
        int k=deviceRepairService.addDeviceRepair(deviceRepair);
        if(i>0&&k>0){
            User current=new User().getInstance();
            int id=Integer.valueOf(userId);
            String history="用户"+id+"送修故障无人机"+uavId;
            userHistoryService.add(history,id);
            int l=deviceStateService.updateActionById("故障",Integer.valueOf(uavId));
            resp.getWriter().write(new JSONObject().put("result", "200").put("message", "添加成功").toString());
            resp.sendRedirect("/UVASystem_war_exploded/html/uavBreakdown.html");
        }else{
            resp.getWriter().write(new JSONObject().put("result", "error").put("message", "添加失败").toString());
            resp.sendRedirect("/UVASystem_war_exploded/html/uavBreakdown.html");
        }
        deviceRepairService.close();

    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }
}
