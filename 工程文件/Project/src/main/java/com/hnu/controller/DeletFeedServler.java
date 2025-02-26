package com.hnu.controller;

import com.hnu.entity.User;
import com.hnu.service.FeedBackService;
import com.hnu.service.LogService;
import com.hnu.service.UserHistoryService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import java.io.IOException;

@WebServlet(name = "DeletFeedServler", urlPatterns = {"/userDel-servlet"})
public class DeletFeedServler extends HttpServlet {
    private LogService logService=new LogService();
    private UserHistoryService userHistoryService=new UserHistoryService();
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json; charset=UTF-8");
        System.out.println("get");
        String feedid=req.getParameter("id");
        FeedBackService feedBackService=new FeedBackService();
        feedBackService.deleteUserByFId(Integer.valueOf(feedid));
        User current=new User().getInstance();
        int id=current.getId();
        String history="用户"+id+"删除反馈"+feedid;
        userHistoryService.add(history,id);
        userHistoryService.close();
        feedBackService.close();
        resp.sendRedirect("/UVASystem_war_exploded/html/userFeedback.html");
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doPost(request, response);
    }
}
