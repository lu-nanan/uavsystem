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
@WebServlet(name = "searchDErrorServlet", urlPatterns = {"/searchUavBreakdown-servlet"})
public class searchDErrorServlet extends HttpServlet {
    private DeviceErrorService deviceErrorService=new DeviceErrorService();
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json; charset=UTF-8");
        String  searchInput= req.getParameter("searchKeyword");
        String searchOption=req.getParameter("searchBy");
        System.out.println(searchInput+searchOption);
        List<DeviceError> deviceErrors=new ArrayList<>();
        deviceErrors=deviceErrorService.searchError(searchInput,searchOption);
        JSONArray jsonArray=new JSONArray();
        jsonArray=Trans(deviceErrors);
        System.out.println(jsonArray.toJSONString());
        PrintWriter out = resp.getWriter();
        out.print(jsonArray.toJSONString());
        deviceErrorService.close();
        out.flush();
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doPost(request, response);
    }
    public JSONArray Trans(List<DeviceError> deviceErrors){
        JSONArray jsonArray = new JSONArray();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(DeviceError deviceError:deviceErrors){
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("breakdownId",deviceError.getErrorid());
            jsonObject.put("uavId",deviceError.getDeviceid());
            jsonObject.put("breakdownTime",sdf.format(deviceError.getTime()));
            System.out.println(deviceError.getIstorepair());
            String isSend=deviceErrorService.getIsRepair(deviceError.getIstorepair());
            System.out.println(isSend);
            jsonObject.put("isSendToRepair",isSend);
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }
}
