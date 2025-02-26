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
import java.text.SimpleDateFormat;
import java.util.Date;
@WebServlet(name = "AddBlackUserServlet", urlPatterns = {"/addBlack-servlet"})
public class AddBlackUserServlet extends HttpServlet {
    private BlackUserService blackUserService=new BlackUserService();
    private UserHistoryService userHistoryService=new UserHistoryService();
    private LogService logService=new LogService();
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json; charset=UTF-8");
        String userId=req.getParameter("id");
        User user=new User().getInstance();
        int adminid=user.getId();
        //int adminid=546546;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        BlackUser blackUser=new BlackUser();
        blackUser.setUserid(Integer.valueOf(userId));
        blackUser.setAdminid(adminid);
        blackUser.setDotime(new Date());
        User current=new User().getInstance();
        int id=current.getId();
        int i=blackUserService.addBlack(blackUser);
        if(i>0){
            String history="管理员"+id+"拉黑用户"+userId;
            System.out.println(history);
            userHistoryService.add(history,id);
            resp.getWriter().write(new JSONObject().put("result", "200").put("message", "拉黑成功").toString());
        }else{
            logService.addLogToFile(user.getId(), "管理员拉黑失败","error","系统");
            resp.getWriter().write(new JSONObject().put("result", "error").put("message", "拉黑失败").toString());
        }
        userHistoryService.close();
        logService.close();
        blackUserService.close();
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }
}
