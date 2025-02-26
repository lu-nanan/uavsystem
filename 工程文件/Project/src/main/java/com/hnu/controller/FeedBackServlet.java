package com.hnu.controller;
import com.hnu.entity.FeedBack;
import com.hnu.entity.User;
import com.hnu.service.FeedBackService;
import com.hnu.service.UserHistoryService;
import jakarta.servlet.ServletException;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import org.json.simple.JSONArray;

import org.json.JSONException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.json.simple.JSONObject;


@WebServlet(name = "FeedBackServlet", urlPatterns = {"/userSea-servlet"})
public class FeedBackServlet extends HttpServlet{
    private UserHistoryService userHistoryService=new UserHistoryService();
    public FeedBackServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse  resp) throws ServletException, IOException {
        System.out.println("Feedabcksearchget");
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json; charset=UTF-8");
        String userid=req.getParameter("userID");
        String searchKeyword = req.getParameter("searchKeyword");
        String searchBy = req.getParameter("searchBy");
        FeedBackService feedBackService=new FeedBackService();
        System.out.println(userid+searchKeyword+searchBy);
        FeedBack feedBacks= null;
        /*if(userid==null){
            userid="1";
        }*/
        feedBacks=feedBackService.getUserFeedbacks(userid,searchKeyword,searchBy);
        System.out.println(feedBacks.getContent());
        JSONArray jsonArray = new JSONArray();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        /*for (FeedBack feedBack : feedBacks) {
            JSONObject obj = new JSONObject();
            obj.put("id", feedBack.getFeedid());
            obj.put("time",feedBack.getTime());
            obj.put("content",feedBack.getContent());
            jsonArray.add(obj);
        }

         */
        JSONObject obj = new JSONObject();
        obj.put("id", feedBacks.getFeedid());
        obj.put("time",sdf.format(feedBacks.getTime()));
        obj.put("content",feedBacks.getContent());
        System.out.println(userid);
        PrintWriter out = resp.getWriter();
        out.print(obj.toString());
        out.flush();
        User current=new User().getInstance();
        int id=current.getId();
        String history="用户"+id+"搜索反馈信息";
        userHistoryService.add(history,id);
        feedBackService.close();
        userHistoryService.close();
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doPost(request, response);
    }
}


