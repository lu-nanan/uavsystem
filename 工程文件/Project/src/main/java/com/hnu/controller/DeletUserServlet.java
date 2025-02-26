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

@WebServlet(name = " DeletUserServlet", urlPatterns = {"/AdminUserDel-servlet"})
public class DeletUserServlet extends HttpServlet {
    private UserDaoIml userDaoIml=new UserDaoIml();
    private LogService logService=new LogService();
    private UserHistoryService userHistoryService=new UserHistoryService();
    public DeletUserServlet() throws IOException {
    }
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json; charset=UTF-8");
        String userid=req.getParameter("id");
        int id=Integer.valueOf(userid);
        User user=userDaoIml.findUserById(id);
        int i=userDaoIml.deleteUserById(id);
        User current=new User().getInstance();
        int adminid=current.getId();
        if(i>0){
            String history="管理员"+adminid+"删除用户"+userid;
            userHistoryService.add(history,adminid);
            resp.getWriter().write(new JSONObject().put("result", "200").put("message", "删除成功").toString());

        }else{
            logService.addLogToFile(adminid,"删除用户失败","error","DeletUserServlet.java");
            resp.getWriter().write(new JSONObject().put("result", "error").put("message", "删除失败").toString());
        }
        userDaoIml.close();
        logService.close();
        userHistoryService.close();

    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doPost(request, response);
    }
}
