package com.hnu.controller;

import com.hnu.dao.UserDaoIml;
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
import java.io.PrintWriter;

@WebServlet(name = "SetPasswordServlet", urlPatterns = {"/userChangePassword-servlet"})
public class SetPasswordServlet extends HttpServlet {
    private UserDaoIml userDaoIml=new UserDaoIml();
    private LogService logService=new LogService();
    private UserHistoryService userHistoryService=new UserHistoryService();
    public SetPasswordServlet() throws IOException {
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json; charset=UTF-8");
        String userid=req.getParameter("userID");
        String password=req.getParameter("password");
        String confirmPassword=req.getParameter("confirmPassword");
        /*System.out.println(userid);
        System.out.println(password);
        System.out.println(confirmPassword);*/
        if (!password.equals(confirmPassword)){
            resp.getWriter().write(new JSONObject().put("result", "error").put("message", "密码不一致").toString());
            System.out.println("one");
            return;
        }
        int i=userDaoIml.updatePasswordById(password,Integer.valueOf(userid));
        System.out.println(i);
        if(i>0){
            User current=new User().getInstance();
            System.out.println(userid);
            int id=Integer.valueOf(userid);
            String history="用户"+id+"修改密码";
            userHistoryService.add(history,id);
            resp.getWriter().write(new JSONObject().put("result", "200").put("message", "修改成功").toString());
            System.out.println("two");
        }
        else {
            logService.addLogToFile(Integer.valueOf(userid),"重设密码失败","error","SetPasswordServlet");
            resp.getWriter().write(new JSONObject().put("result", "200").put("result", "error").put("message", "修改失败").toString());
            System.out.println("three");
        }
        userHistoryService.close();
        userDaoIml.close();
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doPost(request, response);
    }
}
