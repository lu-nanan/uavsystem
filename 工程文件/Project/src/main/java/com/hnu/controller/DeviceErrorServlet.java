package com.hnu.controller;

import com.hnu.entity.DeviceError;
import com.hnu.service.DeviceErrorService;
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

@WebServlet(name = "DeviceErrorServlet", urlPatterns = {"/uavError-servlet"})
public class DeviceErrorServlet extends HttpServlet {
    private DeviceErrorService deviceErrorService=new DeviceErrorService();
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json; charset=UTF-8");
        String pages = req.getParameter("page");
        if(pages==null){
            resp.getWriter().write(new org.json.JSONObject().put("result", "error").put("message", "参数错误").toString());
            return;
        }
        int page=Integer.valueOf(pages);
        List<DeviceError> deviceErrors=new ArrayList<>();
        deviceErrors=deviceErrorService.findAllDeviceErrors();
        int length=deviceErrors.size();
        int totalPages=deviceErrorService.getPages(deviceErrors);
        System.out.println(length);
        JSONArray jsonArray = new JSONArray();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for(int i=(page-1)*8;i<min(page*8,length);i++){
            DeviceError deviceError=deviceErrors.get(i);
            System.out.println(deviceError.getIstorepair());
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("totalPages",totalPages);
            jsonObject.put("breakdownId",deviceError.getErrorid());
            jsonObject.put("uavId",deviceError.getDeviceid());
            jsonObject.put("breakdownTime",sdf.format(deviceError.getTime()));
            System.out.println(deviceError.getIstorepair());
            String isSend=deviceErrorService.getIsRepair(deviceError.getIstorepair());
            System.out.println(isSend);
            jsonObject.put("isSendToRepair",isSend);
            jsonArray.add(jsonObject);
        }
        deviceErrorService.close();
        PrintWriter out = resp.getWriter();
        out.print(jsonArray.toJSONString());
        out.flush();
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doPost(request, response);
    }
}
