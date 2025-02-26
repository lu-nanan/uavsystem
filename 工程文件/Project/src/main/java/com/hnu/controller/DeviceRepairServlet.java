package com.hnu.controller;

import com.hnu.entity.DeviceRepair;
import com.hnu.service.DeviceRepairService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static java.lang.Math.min;

@WebServlet(name = "DeviceRepairServlet", urlPatterns = {"/uavOM-servlet"})
public class DeviceRepairServlet extends HttpServlet {
    private DeviceRepairService deviceRepairService=new DeviceRepairService();
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json; charset=UTF-8");
        String pages = req.getParameter("page");
        List<DeviceRepair> deviceRepairs=deviceRepairService.findAllDeviceRepair();
        //pages="1";
        if(pages==null){
            resp.getWriter().write(new org.json.JSONObject().put("result", "error").put("message", "参数错误").toString());
            return;
        }
        int page=Integer.valueOf(pages);
        int length=deviceRepairs.size();
        int totalPages=deviceRepairService.getPages(deviceRepairs);
        System.out.println(length);
        JSONArray jsonArray = new JSONArray();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(int i=(page-1)*8;i<min(page*8,length);i++){
            DeviceRepair deviceRepair=deviceRepairs.get(i);
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("totalPages",totalPages);
            jsonObject.put("uavId",deviceRepair.getDeviceid());
            jsonObject.put("repairTime",sdf.format(deviceRepair.getTime()));
            jsonObject.put("userId",deviceRepair.getHandler());
            Date date=deviceRepair.getCompletetime();
            if(date==null){
                jsonObject.put("repairendingTime",null);
            }else {
                jsonObject.put("repairendingTime",sdf.format(deviceRepair.getCompletetime()));
            }
            jsonObject.put("problem",deviceRepair.getProblem());
            jsonObject.put("uavOmId",deviceRepair.getReid());
            jsonArray.add(jsonObject);
        }
        deviceRepairService.close();
        PrintWriter out = resp.getWriter();
        out.print(jsonArray.toJSONString());
        System.out.println(jsonArray.toJSONString());
        out.flush();

    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doPost(request, response);
    }
}
