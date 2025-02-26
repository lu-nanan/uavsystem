package com.hnu.controller;

import com.hnu.dao.UserDaoIml;
import com.hnu.entity.BlackUser;
import com.hnu.entity.User;
import com.hnu.service.BlackUserService;
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
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet(name = "AddUserServlet", urlPatterns = {"/addUser-servlet"})
public class AddUserServlet extends HttpServlet {
    private UserDaoIml userDaoIml=new UserDaoIml();

    public AddUserServlet() throws IOException {
    }
    private sendCode sendCode=new sendCode();
    private LogService logService=new LogService();
    private UserHistoryService userHistoryService=new UserHistoryService();
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json; charset=UTF-8");
        String email=req.getParameter("email");
        String phone=req.getParameter("telephone");
        String password=req.getParameter("password");
        if(userDaoIml.findUserByPhone(Long.valueOf(phone))!=null|| userDaoIml.findUserByEmail(email)!=null){
            resp.getWriter().write(new JSONObject().put("result", "error").put("message", "用户已存在").toString());
            return;
        }
        if(!sendCode.checkEmail(email)|| !sendCode.checkPhone(phone)){
            resp.getWriter().write(new JSONObject().put("result", "error").put("message", "邮箱或手机号错误").toString());
            return;
        }
        User  newUser=new User();
        newUser.setEmail(email);
        newUser.setType(1);
        newUser.setPassword(password);
        newUser.setPhone(Long.valueOf(phone));
        int i=userDaoIml.addUser(newUser);
        User current=new User().getInstance();
        int id=current.getId();
        if(i>0){
            String history="管理员"+id+"添加用户";
            userHistoryService.add(history,id);
            resp.getWriter().write(new JSONObject().put("result", "200").put("message", "添加成功").toString());
        }else{
            logService.addLogToFile(id,"添加用户失败","error","AddUserServlet");
            resp.getWriter().write(new JSONObject().put("result", "error").put("message", "添加失败").toString());
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
