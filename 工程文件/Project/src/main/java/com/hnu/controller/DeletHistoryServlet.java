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

@WebServlet(name = "DeletHistoryServlet", urlPatterns = {"/historyDel-servlet"})
public class DeletHistoryServlet extends HttpServlet {
    private LogService logService=new LogService();
    private UserHistoryService userHistoryService=new UserHistoryService();
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json; charset=UTF-8");
        String id=req.getParameter("id");
        int hisid=Integer.valueOf(id);
        int i=userHistoryService.deleteUserByHId(hisid);
        User current=new User().getInstance();
        int userid=current.getId();
        if(i>0){
            String history="用户"+userid+"删除历史记录"+id;
            userHistoryService.add(history,userid);
            resp.getWriter().write(new JSONObject().put("result", "200").put("message", "删除成功").toString());
//            resp.sendRedirect("/UVASystem_war_exploded/html/history.html");
        }else{
            logService.addLogToFile(userid,"删除历史记录失败","error","DeletHistoryServlet");
            resp.getWriter().write(new JSONObject().put("result", "error").put("message", "删除失败").toString());
//            resp.sendRedirect("/UVASystem_war_exploded/html/history.html");
        }
        userHistoryService.close();
        logService.close();
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doPost(request, response);
    }

}
