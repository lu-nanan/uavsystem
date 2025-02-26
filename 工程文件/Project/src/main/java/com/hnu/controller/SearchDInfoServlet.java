package com.hnu.controller;

import com.hnu.entity.DeviceInfo;
import com.hnu.entity.User;
import com.hnu.service.DeviceInfoService;
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
import java.util.List;

import static java.lang.Math.min;

@WebServlet(name = "SearchDInfoServlet", urlPatterns = {"/uavSea-servlet"})
public class SearchDInfoServlet extends HttpServlet {
    private DeviceInfoService deviceInfoService=new DeviceInfoService();
    private UserHistoryService userHistoryService=new UserHistoryService();
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("uavsea");
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json; charset=UTF-8");
        String searchKeyword = req.getParameter("searchKeyword");
        String searchBy = req.getParameter("searchBy");
        List<DeviceInfo> deviceInfos=new ArrayList<>();
        PrintWriter out = resp.getWriter();
        JSONArray jsonArray=new JSONArray();
        if(searchKeyword==null){
            deviceInfos=deviceInfoService.findAllDevices();
            jsonArray=Trans(deviceInfos);
            out.print(jsonArray.toJSONString());
            return;
        }
        if(searchBy.equals("ID")){
            DeviceInfo deviceInfo=deviceInfoService.findDeviceById(Integer.valueOf(searchKeyword));
            deviceInfos.add(deviceInfo);
            jsonArray=Trans(deviceInfos);
            out.print(jsonArray.toJSONString());
        } else if (searchBy.equals("model")) {
            deviceInfos=deviceInfoService.findDeviceByType(searchKeyword);
            jsonArray=Trans(deviceInfos);
            out.print(jsonArray.toJSONString());
        } else if (searchBy.equals("producer")) {
            deviceInfos=deviceInfoService.findDeviceByProductor(searchKeyword);
            jsonArray=Trans(deviceInfos);
            out.print(jsonArray.toJSONString());
        }

        User current=new User().getInstance();
        int id=current.getId();
        String history="用户"+id+"搜索无人机信息";
        userHistoryService.add(history,id);

    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doPost(request, response);
    }
    public JSONArray Trans(List<DeviceInfo> deviceInfos){
        int length=deviceInfos.size();
        JSONArray jsonArray=new JSONArray();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(DeviceInfo deviceInfo:deviceInfos){
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("id",deviceInfo.getId());
            jsonObject.put("model",deviceInfo.getType());
            jsonObject.put("endurance",deviceInfo.getBattery());
            jsonObject.put("producer",deviceInfo.getProductor());
            jsonObject.put("imageTrans",deviceInfo.getDistance());
            jsonObject.put("buydingDate",sdf.format(deviceInfo.getBuytime()));
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

}
