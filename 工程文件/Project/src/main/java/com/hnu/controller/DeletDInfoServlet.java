package com.hnu.controller;

import com.hnu.entity.User;
import com.hnu.service.DeviceInfoService;
import com.hnu.service.FeedBackService;
import com.hnu.service.LogService;
import com.hnu.service.UserHistoryService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import java.io.IOException;
@WebServlet(name = "DeletDInfoServlet", urlPatterns = {"/uavDel-servlet"})
public class DeletDInfoServlet extends HttpServlet {
    private DeviceInfoService deviceInfoService=new DeviceInfoService();
    private LogService logService=new LogService();
    private UserHistoryService userHistoryService=new UserHistoryService();
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("uavdeleteget");
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json; charset=UTF-8");
        String id=req.getParameter("id");
        System.out.println(id);
        User current=new User().getInstance();
        int userid=current.getId();
        Boolean flag=deviceInfoService.deleteDeviceById(Integer.valueOf(id));
        if(flag){
            String history="用户"+userid+"删除无人机信息"+id;
            userHistoryService.add(history,userid);
            resp.getWriter().write(new JSONObject().put("result", "200").put("message", "删除成功").toString());
        }else{
            logService.addLogToFile(userid,"删除无人机信息失败","error","DeletDInfoServlet");
            resp.getWriter().write(new JSONObject().put("result", "error").put("message", "删除失败").toString());
        }
        deviceInfoService.close();
        logService.close();
        userHistoryService.close();
        resp.sendRedirect("/UVASystem_war_exploded/html/uavInformation.html");
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doPost(request, response);
    }
}
