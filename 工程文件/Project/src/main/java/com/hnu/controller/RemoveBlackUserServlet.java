package com.hnu.controller;

import com.hnu.entity.BlackUser;
import com.hnu.entity.User;
import com.hnu.service.BlackUserService;
import com.hnu.service.LogService;
import com.hnu.service.UserHistoryService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import java.io.IOException;
@WebServlet(name = "RemoveBlackUserServlet", urlPatterns = {"/removeBlack-servlet"})
public class RemoveBlackUserServlet extends HttpServlet {
    private LogService logService=new LogService();
    private BlackUserService blackUserService=new BlackUserService();
    private UserHistoryService userHistoryService=new UserHistoryService();
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json; charset=UTF-8");
        String userId=req.getParameter("userId");
        User user=new User().getInstance();
        int i=blackUserService.deleteBlackByUId(Integer.valueOf(userId));
        if(i>0){
            User current=new User().getInstance();
            int id=current.getId();
            String history="管理员"+id+"将用户"+userId+"移出黑名单";
            userHistoryService.add(history,id);
            System.out.println(history);
            resp.getWriter().write(new JSONObject().put("result", "200").put("message", "移除成功").toString());
        }else{
            logService.addLogToFile(Integer.valueOf(userId),"移除失败","error","RemoveBlackUserServlet");
            resp.getWriter().write(new JSONObject().put("result", "error").put("message", "移除失败").toString());
        }
        blackUserService.close();
        userHistoryService.close();
        logService.close();

    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }
}
