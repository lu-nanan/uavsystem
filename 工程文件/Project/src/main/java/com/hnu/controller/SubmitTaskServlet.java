package com.hnu.controller;

import com.hnu.entity.FeedBack;
import com.hnu.entity.History;
import com.hnu.entity.TaskInfo;
import com.hnu.entity.User;
import com.hnu.service.LogService;
import com.hnu.service.TaskInfoService;
import com.hnu.service.UserHistoryService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.json.simple.JSONArray;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static java.lang.Math.min;
@WebServlet(name = "SubmitTaskServlet", urlPatterns = {"/submitTask-servlet"})
public class SubmitTaskServlet extends HttpServlet {
    private TaskInfoService taskInfoService=new TaskInfoService();
    private UserHistoryService userHistoryService=new UserHistoryService();
    private LogService logService=new LogService();
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json; charset=UTF-8");
        String textInput=req.getParameter("textInput");
        System.out.println(textInput);
        String userID=req.getParameter("userID");
        Boolean flag=check(textInput);
        System.out.println(flag);
        if(!flag){
            resp.getWriter().write(new JSONObject().put("result", "error").put("message", "任务格式错误").toString());
            System.out.println("任务格式错误");
            return;
        }
        System.out.println(textInput);
        TaskInfo taskInfo=new TaskInfo();
        taskInfo.setContent(textInput);
        taskInfo.setUserid(Integer.valueOf(userID));
        taskInfo.setTaskstate("未分配");
        taskInfo.setTime(new Date());
        int i=taskInfoService.addTask(taskInfo);
        if (i>0){
            String history="用户"+userID+"下达任务"+textInput;
            userHistoryService.add(history,Integer.valueOf(userID));
            resp.getWriter().write(new JSONObject().put("result", "200").put("message", "添加成功").toString());
        }else {
            logService.addLogToFile(Integer.valueOf(userID),"提交任务错误","error","SubmitTaskServlet.java");
            resp.getWriter().write(new JSONObject().put("result", "error").put("message", "添加失败").toString());
        }
        taskInfoService.close();
        System.out.println(i);
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doPost(request, response);
    }
    public Boolean check(String content){
        Boolean flag=false;
        String[] parts = content.split(" ");
        String[] tasks = {"浇水","施肥","虫害情况勘察","农作物生长状态勘察"};
        if (parts.length < 3) {
            System.out.println(1);
            return false;
        }
        int i=Integer.valueOf(parts[0]);
        int j=Integer.valueOf(parts[1]);
        if(parts.length!=3){
            System.out.println(1);
            return false;
        }
        if(i<0||i>100){
            System.out.println(2);
            return false;
        }
        if(j<0||j>100){
            System.out.println(3);
            return false;
        }
        for (String task :tasks){
            if(task.equals(parts[2])){
                System.out.println(4);
                return true;
            }
        }
        return false;
    }
}
