package com.hnu.controller;

import com.hnu.entity.User;
import com.hnu.service.LogService;
import com.hnu.service.UserHistoryService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import java.io.IOException;

@WebServlet(name = " DeletLogServlet", urlPatterns = {"/logDel-servlet"})
public class DeletLogServlet extends HttpServlet {
    private LogService logService=new LogService();
    private UserHistoryService userHistoryService=new UserHistoryService();
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json; charset=UTF-8");
        String logID=req.getParameter("id");
        System.out.println(logID);
        int i=logService.deleteUserByLId(Integer.valueOf(logID));
        if(i>0){
            User current=new User().getInstance();
            int id=current.getId();
            String history="管理员"+id+"删除日志"+logID;
            userHistoryService.add(history,id);
            resp.getWriter().write(new JSONObject().put("result", "200").put("message", "删除成功").toString());
            resp.sendRedirect("/UVASystem_war_exploded/html/log.html");
        }else{
            resp.getWriter().write(new JSONObject().put("result", "error").put("message", "删除失败").toString());
            resp.sendRedirect("/UVASystem_war_exploded/html/log.html");
        }
        logService.close();
        userHistoryService.close();

    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doPost(request, response);
    }
}
