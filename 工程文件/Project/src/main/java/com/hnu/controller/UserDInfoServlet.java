package com.hnu.controller;

import com.hnu.entity.DeviceInfo;
import com.hnu.service.DeviceInfoService;
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
@WebServlet(name = "UserDInfoServlet", urlPatterns = {"/uavInformation-servlet"})
public class UserDInfoServlet extends HttpServlet {
    private DeviceInfoService deviceInfoService=new DeviceInfoService();
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("UAvINfoget");
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json; charset=UTF-8");
        String pages=req.getParameter("page");
        int page=Integer.valueOf(pages);
        List<DeviceInfo> deviceInfos=new ArrayList<>();
        deviceInfos=deviceInfoService.findAllDevices();
        int length=deviceInfos.size();
        int totalPages=deviceInfoService.getPages(deviceInfos);
        //System.out.println(length);
        JSONArray jsonArray = new JSONArray();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for(int i=(page-1)*8;i<min(page*8,length);i++){
            DeviceInfo deviceInfo=deviceInfos.get(i);
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("id",deviceInfo.getId());
            jsonObject.put("totalPages",totalPages);
            jsonObject.put("model",deviceInfo.getType());
            jsonObject.put("endurance",deviceInfo.getBattery());
            jsonObject.put("producer",deviceInfo.getProductor());
            jsonObject.put("imageTrans",deviceInfo.getDistance());
            jsonObject.put("buydingDate",sdf.format(deviceInfo.getBuytime()));
            jsonArray.add(jsonObject);
        }
        //System.out.println(jsonArray);
        //System.out.println(deviceInfos.get(0).getId());
        deviceInfoService.close();
        System.out.println(page+1);
        PrintWriter out = resp.getWriter();
        //System.out.println(jsonArray.toJSONString());
        out.print(jsonArray.toJSONString());
        out.flush();
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doPost(request, response);
    }
}

