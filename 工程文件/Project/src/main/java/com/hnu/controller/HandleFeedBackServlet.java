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
@WebServlet(name = "HandleFeedBackServlet", urlPatterns = {"/readFeedBack-servlet"})
public class HandleFeedBackServlet extends HttpServlet {
    private LogService logService=new LogService();
    private UserHistoryService userHistoryService=new UserHistoryService();
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json; charset=UTF-8");
        String feedid=req.getParameter("id");
        int id=Integer.valueOf(feedid);
        FeedBackService feedBackService=new FeedBackService();
        int i=feedBackService.updateFeedByFId(true,id);
        User current=new User().getInstance();
        int userid=current.getId();
        if(i>0){
            String history="管理员"+userid+"处理反馈"+feedid;
            userHistoryService.add(history,userid);
            resp.getWriter().write(new JSONObject().put("result", "200").put("message", "处理成功").toString());
            resp.sendRedirect("/UVASystem_war_exploded/html/admin/userFeedback.html");
        }else{
            logService.addLogToFile(userid,"处理反馈失败","error","HandleFeedBackServlet.java");
            resp.getWriter().write(new JSONObject().put("result", "error").put("message", "处理失败").toString());
            resp.sendRedirect("/UVASystem_war_exploded/html/admin/userFeedback.html");
        }
        feedBackService.close();
        userHistoryService.close();
        logService.close();

    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doPost(request, response);
    }
}
