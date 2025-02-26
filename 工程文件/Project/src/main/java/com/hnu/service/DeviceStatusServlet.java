package com.hnu.service;

import com.hnu.entity.DeviceState;
import com.hnu.entity.FeedBack;
import com.hnu.entity.User;
import com.hnu.service.DeviceStateService;
import com.hnu.service.FeedBackService;
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
import java.util.List;

import static java.lang.Math.min;
@WebServlet(name = "DeviceStatusServlet", urlPatterns = {"/uavStatus-servlet"})
public class DeviceStatusServlet extends HttpServlet {
    private DeviceStateService deviceStateService=new DeviceStateService();
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json; charset=UTF-8");
        String pages = req.getParameter("page");
        if(pages==null){
            resp.getWriter().write(new org.json.JSONObject().put("result", "error").put("message", "参数错误").toString());
            return;
        }
        int page=Integer.valueOf(pages);
        List<DeviceState>deviceStates=deviceStateService.findAllState();
        int length=deviceStates.size();
        int totalPages=deviceStateService.getPages(deviceStates);
        System.out.println(length);
        JSONArray jsonArray = new JSONArray();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(int i=(page-1)*8;i<min(page*8,length);i++){
            DeviceState deviceState=deviceStates.get(i);
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("totalPages",totalPages);
            jsonObject.put("id",deviceState.getDeviceid());
            jsonObject.put("electric",deviceState.getCharge());
            jsonObject.put("x",deviceState.getX());
            jsonObject.put("y",deviceState.getY());
            jsonObject.put("height",deviceState.getZ());
            jsonObject.put("action",deviceState.getAction());

            jsonArray.add(jsonObject);
        }
        deviceStateService.close();
        PrintWriter out = resp.getWriter();
        out.print(jsonArray.toJSONString());
        out.flush();
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doPost(request, response);
    }
}
