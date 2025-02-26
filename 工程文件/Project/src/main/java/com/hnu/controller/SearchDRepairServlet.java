package com.hnu.controller;

import com.hnu.entity.DeviceRepair;
import com.hnu.entity.User;
import com.hnu.service.DeviceRepairService;
import com.hnu.service.UserHistoryService;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@WebServlet(name = "SearchDRepairServlet", urlPatterns = {"/searchUavOm-servlet"})
public class SearchDRepairServlet extends HttpServlet {
    private DeviceRepairService deviceRepairService=new DeviceRepairService();
    private UserHistoryService userHistoryService=new UserHistoryService();
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json; charset=UTF-8");
        String searchKeyword=req.getParameter("searchKeyword");
        String searchBy=req.getParameter("searchBy");
        List<DeviceRepair> deviceRepairs=new ArrayList<>();
        deviceRepairs=deviceRepairService.searchDRepair(searchKeyword,searchBy);
        JSONArray jsonArray=new JSONArray();
        jsonArray=Tans(deviceRepairs);
        deviceRepairService.close();
        PrintWriter out = resp.getWriter();
        out.print(jsonArray.toJSONString());
        System.out.println(jsonArray.toJSONString());
        out.flush();
        User current=new User().getInstance();
        int id=current.getId();
        String history="用户"+id+"搜索无人机维修情况";
        userHistoryService.add(history,id);


    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doPost(request, response);
    }
    public JSONArray Tans(List<DeviceRepair> deviceRepairs){
        JSONArray jsonArray=new JSONArray();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (DeviceRepair deviceRepair:deviceRepairs){
            JSONObject jsonObject=new JSONObject();
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
        return jsonArray;
    }
}
