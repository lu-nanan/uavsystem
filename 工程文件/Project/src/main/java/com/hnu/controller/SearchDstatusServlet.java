package com.hnu.controller;

import com.hnu.entity.DeviceState;
import com.hnu.entity.User;
import com.hnu.service.DeviceStateService;
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
import java.util.ArrayList;
import java.util.List;
@WebServlet(name = "SearchDstatusServlet", urlPatterns = {"/uavStatusSea-servlet"})
public class SearchDstatusServlet extends HttpServlet {
    private DeviceStateService deviceStateService=new DeviceStateService();
    private UserHistoryService userHistoryService=new UserHistoryService();
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json; charset=UTF-8");
        String searchKeyword = req.getParameter("searchKeyword");
        String searchBy = req.getParameter("searchBy");
        List<DeviceState>deviceStates=new ArrayList<>();
        JSONArray jsonArray=new JSONArray();
        if(searchKeyword==null){
            deviceStates=deviceStateService.findAllState();
        }else{
            if (searchBy.equals("ID")) {
                deviceStates.add(deviceStateService.findStateById(Integer.valueOf(searchKeyword)));
            } else if (searchBy.equals("x")) {
                deviceStates=deviceStateService.findStateByX(Double.valueOf(searchKeyword));
            }else if(searchBy.equals("y")){
                deviceStates=deviceStateService.findStateByY(Double.valueOf(searchKeyword));
            } else if (searchBy.equals("height")) {
                deviceStates=deviceStateService.findStateByZ(Double.valueOf(searchKeyword));
            } else if (searchBy.equals("action")) {
                deviceStates=deviceStateService.findStateByAction(searchKeyword);
            }else {
                return;
            }
        }
        jsonArray=Trans(deviceStates);
        System.out.println(jsonArray.toJSONString()   );
        PrintWriter out = resp.getWriter();
        out.print(jsonArray.toJSONString());
        out.flush();
        User current=new User().getInstance();
        int id=current.getId();
        String history="用户"+id+"搜索无人机状态";
        userHistoryService.add(history,id);
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doPost(request, response);
    }
    public JSONArray Trans(List<DeviceState> deviceStates){
        JSONArray jsonArray=new JSONArray();
        for(DeviceState deviceState:deviceStates){
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("id",deviceState.getDeviceid());
            jsonObject.put("electric",deviceState.getCharge());
            jsonObject.put("x",deviceState.getX());
            jsonObject.put("y",deviceState.getY());
            jsonObject.put("height",deviceState.getZ());
            jsonObject.put("action",deviceState.getAction());
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }
}

