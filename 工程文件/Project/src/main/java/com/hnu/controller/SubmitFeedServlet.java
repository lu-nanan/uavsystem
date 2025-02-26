package com.hnu.controller;
import com.hnu.entity.FeedBack;
import com.hnu.entity.User;
import com.hnu.service.FeedBackService;
import com.hnu.service.LogService;
import com.hnu.service.UserHistoryService;
import jakarta.servlet.ServletException;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

@WebServlet(name = "SubmitFeedServlet", urlPatterns = {"/submitFeedback-servlet"})
public class SubmitFeedServlet extends  HttpServlet{
    private FeedBackService feedBackService=new FeedBackService();
    private UserHistoryService userHistoryService=new UserHistoryService();
    private LogService logService=new LogService();
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 设置响应内容类型
        request.setCharacterEncoding("utf-8");
        response.setContentType("application/json; charset=UTF-8");
        // 获取请求参数
        String textInput = request.getParameter("textInput");
        System.out.println(textInput);
        FeedBack feedBack=createFeedback();
        feedBack.setContent(textInput);
        int i=feedBackService.addFeed(feedBack);
        System.out.println(i);
        if(i>0){
            String history="用户"+feedBack.getUserid()+"提交反馈:"+textInput;
            userHistoryService.add(history,feedBack.getUserid());
            response.getWriter().write(new JSONObject().put("result", "200").put("message", "提交成功").toString());

        }
        else{
            logService.addLogToFile(feedBack.getUserid(),"提交反馈失败","error","SubmitFeedServlet.java");
            response.getWriter().write(new JSONObject().put("result", "error").put("message", "提交错误").toString());
            return;
        }
        feedBackService.close();
    }
    private FeedBack createFeedback() {
        User currentUser=new User().getInstance();
        FeedBack feedBack=new FeedBack();
        feedBack.setIshandle(false);
        feedBack.setTime(new Date());
        feedBack.setUserid(currentUser.getId());
        return feedBack;
    }
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 重定向到POST方法处理
        doPost(req, resp);
    }

}
