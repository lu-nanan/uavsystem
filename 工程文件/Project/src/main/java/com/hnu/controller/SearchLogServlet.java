package com.hnu.controller;

import com.hnu.entity.Log;
import com.hnu.entity.User;
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
@WebServlet(name = "SearchLogServlet", urlPatterns = {"/searchLog-servlet"})
public class SearchLogServlet extends HttpServlet {
    private LogService logService=new LogService();
    private UserHistoryService userHistoryService=new UserHistoryService();
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json; charset=UTF-8");
        String searchInput = req.getParameter("searchInput");
        String searchOption = req.getParameter("searchOption");
        User current=new User().getInstance();
        int id=current.getId();
        List<Log> logs=new ArrayList<>();
        try {
            logs=logService.searchLog(searchInput,searchOption);
        } catch (ParseException e) {
            logService.addLogToFile(id,"转换失败","error","SearchLogServlet.java");
            throw new RuntimeException(e);
        }
        JSONArray jsonArray=new JSONArray();
        jsonArray=Trans(logs);
        System.out.println(jsonArray.toJSONString());
        PrintWriter out = resp.getWriter();
        out.print(jsonArray.toJSONString());
        out.flush();


        String history="管理员"+id+"搜索日志信息";
        userHistoryService.add(history,id);
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }
    public JSONArray Trans(List<Log> logs) {
        JSONArray jsonArray = new JSONArray();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Log log:logs){
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("id",log.getLogId());
            jsonObject.put("time",sdf.format(log.getDate()));
            jsonObject.put("rank",log.getLevel());
            jsonObject.put("source",log.getResource());
            jsonObject.put("content",log.getMessage());
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }
}
