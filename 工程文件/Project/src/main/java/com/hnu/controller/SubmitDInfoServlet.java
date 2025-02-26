package com.hnu.controller;

import com.hnu.entity.DeviceInfo;
import com.hnu.entity.DeviceState;
import com.hnu.entity.FeedBack;
import com.hnu.entity.User;
import com.hnu.service.DeviceInfoService;
import com.hnu.service.DeviceStateService;
import com.hnu.service.LogService;
import com.hnu.service.UserHistoryService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.ibatis.annotations.Param;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
@WebServlet(name = "SubmitDInfoServlet", urlPatterns = {"/submitUavInfo-servlet"})
public class SubmitDInfoServlet extends HttpServlet {
    private DeviceInfoService deviceInfoService=new DeviceInfoService();
    private DeviceStateService deviceStateService=new DeviceStateService();
    private UserHistoryService userHistoryService=new UserHistoryService();
    private LogService logService=new LogService();
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 设置响应内容类型
        request.setCharacterEncoding("utf-8");
        response.setContentType("application/json; charset=UTF-8");
        // 获取请求参数
        String type = request.getParameter("model");
        String productor = request.getParameter("producer");
        String distance = request.getParameter("imageTrans");
        String time = request.getParameter("buydingDate");
        String charge = request.getParameter("endurance");
        System.out.println("type"+type+" productor"+productor+"distance"+distance+"time"+time+"charge"+charge);
        DeviceInfo deviceInfo=new DeviceInfo();
        deviceInfo.setBattery(Integer.valueOf(charge));
        deviceInfo.setType(type);
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        System.out.println("ok1");
        try {
            System.out.println(3);
            date = simpleDateFormat.parse(time);
        } catch (ParseException e) {

            throw new RuntimeException(e);
        }
        deviceInfo.setBuytime(date);
        deviceInfo.setDistance(Integer.valueOf(distance));
        deviceInfo.setProductor(productor);
        Boolean flag=deviceInfoService.addDevice(deviceInfo);
        System.out.println("ok2");
        DeviceState deviceState=new DeviceState();
        deviceState.setAction("空闲中");
        deviceState.setCharge(100);
        deviceState.setX(0);
        deviceState.setY(0);
        deviceState.setZ(0);
        deviceState.setDeviceid(deviceInfo.getId());
        System.out.println("id="+deviceInfo.getId());
        Boolean i=deviceStateService.addState(deviceState);
        deviceStateService.close();
        deviceInfoService.close();
        User current=new User().getInstance();
        int id=current.getId();
        if(flag==true&&i==true){


            String history="用户"+id+"新增无人机";
            userHistoryService.add(history,id);
            response.getWriter().write(new JSONObject().put("result", "200").put("message", "提交成功").toString());
//            response.sendRedirect("/UVASystem_war_exploded/html/addUVA.html");
        }
        else{
            logService.addLogToFile(id,"提交无人机信息失败", "error","SubmitDInfoServlet.java");
            response.getWriter().write(new JSONObject().put("result", "error").put("message", "提交错误").toString());
//            response.sendRedirect("/UVASystem_war_exploded/html/addUVA.html");
        }
        logService.close();
        System.out.println("ok3");
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 重定向到POST方法处理
        doPost(req, resp);
    }
}
