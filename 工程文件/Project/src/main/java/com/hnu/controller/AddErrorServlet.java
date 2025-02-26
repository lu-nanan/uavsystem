package com.hnu.controller;

import com.hnu.entity.DeviceError;
import com.hnu.entity.User;
import com.hnu.service.DeviceErrorService;
import com.hnu.service.DeviceInfoService;
import com.hnu.service.LogService;
import com.hnu.service.UserHistoryService;
import com.hnu.util.DateTimeUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;

@WebServlet(name = "AddErrorServlet", urlPatterns = {"/addError-servlet"})
public class AddErrorServlet extends HttpServlet {
    private DeviceErrorService deviceErrorService=new DeviceErrorService();
    private DeviceInfoService deviceInfoService=new DeviceInfoService();
    private UserHistoryService userHistoryService=new UserHistoryService();
    private LogService logService=new LogService();
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json; charset=UTF-8");
        String uavId=req.getParameter("uavId");
        System.out.println(uavId);
        User user=new User().getInstance();
        if(deviceInfoService.findDeviceById(Integer.valueOf(uavId))==null){
            resp.getWriter().write(new JSONObject().put("result", "error").put("message", "无人机不存在").toString());
            logService.addLogToFile(user.getId(), "用户提交无人机故障错误，无人机不存在","error","AddErrorServlet.java");
            return;
        }
        DeviceError deviceError=new DeviceError();
        deviceError.setDeviceid(Integer.valueOf(uavId));
        //deviceError.setTime(new Date());
        deviceError.setTime(DateTimeUtils.getCurrentDate());
        int j=0;
        deviceError.setIstorepair(j);
        int i=deviceErrorService.addDeviceError(deviceError);
        if(i>0){
            User current=new User().getInstance();
            int id=current.getId();
            String history="用户"+id+"添加无人机故障";
            System.out.println(history);

            userHistoryService.add(history,id);
            resp.getWriter().write(new JSONObject().put("result", "200").put("message", "添加成功").toString());
        }else{
            resp.getWriter().write(new JSONObject().put("result", "error").put("message", "添加失败").toString());
        }
        deviceErrorService.close();
        userHistoryService.close();

    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }
}
