package com.hnu.controller;

import com.hnu.dao.UserDaoIml;
import com.hnu.entity.User;
import com.hnu.service.LogService;
import com.hnu.service.UserHistoryService;
import com.hnu.service.sendCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import java.io.IOException;

@WebServlet(name = "EditUserServlet", urlPatterns = {"/editUser-servlet"})
public class EditUserServlet extends HttpServlet {
    private UserDaoIml userDaoIml=new UserDaoIml();

    private UserHistoryService userHistoryService=new UserHistoryService();

    public EditUserServlet() throws IOException {
    }
    private LogService logService=new LogService();
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json; charset=UTF-8");
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json; charset=UTF-8");
        String userid=req.getParameter("id");
        int id=Integer.valueOf(userid);
        User current=new User().getInstance();
        int adminid=current.getId();
        int i=userDaoIml.updatePasswordById("123456",id);
        if(i>0){
            String history="管理员"+adminid+"重置用户"+userid+"密码";
            userHistoryService.add(history,adminid);
            resp.getWriter().write(new JSONObject().put("result", "200").put("message", "初始化成功").toString());
        }else{
            logService.addLogToFile(adminid,"重置用户密码失败","error","EditUserServlet.java");
            resp.getWriter().write(new JSONObject().put("result", "error").put("message", "初始化失败").toString());
        }
        userDaoIml.close();
        logService.close();
        userHistoryService.close();

    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }
}
