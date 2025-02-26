package com.hnu.controller;

import com.hnu.entity.DeviceState;
import com.hnu.entity.History;
import com.hnu.entity.User;
import com.hnu.service.DeviceStateService;
import com.hnu.service.LogService;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "SearchHistoryServlet", urlPatterns = {"/historySea-servlet"})
public class SearchHistoryServlet extends HttpServlet {
    private UserHistoryService userHistoryService=new UserHistoryService();
    private LogService logService=new LogService();
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException  {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json; charset=UTF-8");
        String searchKeyword = req.getParameter("searchKeyword");
        String searchBy = req.getParameter("searchBy");
        String userID=req.getParameter("userID");
        int userid=Integer.valueOf(userID);
        System.out.println(searchBy);
        System.out.println(searchKeyword);
        List<History> histories=new ArrayList<>();
        User current=new User().getInstance();
        int id=current.getId();
        try {
            histories= userHistoryService.searchHistory(userid,searchKeyword,searchBy);
        } catch (ParseException e) {
            logService.addLogToFile(id,"转换失败","error","SearchHistoryServlet.java");
            throw new RuntimeException(e);
        }
        JSONArray jsonArray=Trans(histories);
        //System.out.println(jsonArray.toJSONString());
        PrintWriter out = resp.getWriter();
        out.print(jsonArray.toJSONString());
        out.flush();


        String history="用户"+id+"搜索历史记录";
        userHistoryService.add(history,id);
        userHistoryService.close();

    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doPost(request, response);
    }
    public JSONArray Trans(List<History>histories){
        JSONArray jsonArray=new JSONArray();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(History history:histories){
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("id",history.getHisid());
            jsonObject.put("content",history.getContent());
            jsonObject.put("time",sdf.format(history.getTime()));
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

}
