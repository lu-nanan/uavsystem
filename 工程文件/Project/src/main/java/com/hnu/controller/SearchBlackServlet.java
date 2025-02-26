package com.hnu.controller;

import com.hnu.entity.BlackUser;
import com.hnu.entity.User;
import com.hnu.service.BlackUserService;
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

@WebServlet(name = "SearchBlackServlet", urlPatterns = {"/searchBlack-servlet"})
public class SearchBlackServlet extends HttpServlet {
    private BlackUserService blackUserService=new BlackUserService();
    private UserHistoryService userHistoryService=new UserHistoryService();
    private LogService logService=new LogService();
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json; charset=UTF-8");
        String searchInput=req.getParameter("searchInput");
        String searchOption=req.getParameter("searchOption");
        List<BlackUser> blackUsers=new ArrayList<>();
        User current=new User().getInstance();
        int id=current.getId();
        try {
            blackUsers =blackUserService.searchBlack(searchInput,searchOption);
        } catch (ParseException e) {
            logService.addLogToFile(id,"转换失败","error","SearchBlackServlet");
            throw new RuntimeException(e);
        }
        JSONArray jsonArray=Trans(blackUsers);
        PrintWriter out = resp.getWriter();
        out.print(jsonArray.toJSONString());
        out.flush();

        String history="管理员"+id+"搜索黑名单成员信息";
        userHistoryService.add(history,id);
        blackUserService.close();
        logService.close();
        userHistoryService.close();

    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }
    public JSONArray Trans(List<BlackUser> blackUsers){
        JSONArray jsonArray = new JSONArray();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(BlackUser blackUser:blackUsers){
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("userid",blackUser.getUserid());
            jsonObject.put("adminid",blackUser.getAdminid());
            jsonObject.put("dotime",sdf.format(blackUser.getDotime()));
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }
}
