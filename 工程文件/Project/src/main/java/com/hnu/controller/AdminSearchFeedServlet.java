package com.hnu.controller;


import com.hnu.entity.FeedBack;
import com.hnu.entity.User;
import com.hnu.service.FeedBackService;
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
@WebServlet(name = "AdminSearchFeedServlet", urlPatterns = {"/searchFeedBack-servlet"})
public class AdminSearchFeedServlet extends HttpServlet {
    private LogService logService=new LogService();
    User current=new User().getInstance();
    int id=current.getId();
    private UserHistoryService userHistoryService=new UserHistoryService();
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json; charset=UTF-8");
        String userid=req.getParameter("UserID");
        String searchKeyword = req.getParameter("searchKeyword");
        String searchBy = req.getParameter("searchBy");
        FeedBackService feedBackService=new FeedBackService();
        System.out.println(userid);
        System.out.println(searchBy);
        System.out.println(searchKeyword);
        List<FeedBack> feedBacks= new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            feedBacks=feedBackService.adminSearch(searchKeyword,searchBy);
        } catch (ParseException e) {
            logService.addLogToFile(id,"参数转换失败","error","AdminSearchFeedServlet");
            throw new RuntimeException(e);
        }
        JSONArray jsonArray = new JSONArray();
        for (FeedBack feedBack : feedBacks) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id",feedBack.getFeedid());
            jsonObject.put("userId",feedBack.getUserid());
            jsonObject.put("time",sdf.format(feedBack.getTime()));
            jsonObject.put("content",feedBack.getContent());
            String ishandle=isHandled(feedBack.getIshandle());
            jsonObject.put("isRead",ishandle);
            jsonArray.add(jsonObject);
        }
        System.out.println(userid);
        System.out.println(jsonArray.toJSONString());
        PrintWriter out = resp.getWriter();
        out.print(jsonArray.toJSONString());
        out.flush();
        String history="管理员"+id+"搜索用户反馈";
        userHistoryService.add(history,id);
        feedBackService.close();
        userHistoryService.close();
    }
    public String isHandled(Boolean ishandle){
        String ishanled=new String();
        if(ishandle==true){
            ishanled="已处理";

        }else {
            ishanled="未处理";
        }
        return ishanled;
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doPost(request, response);
    }
}
