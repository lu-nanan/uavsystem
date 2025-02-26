package com.hnu.controller;

import com.hnu.entity.DeviceInfo;
import com.hnu.entity.History;
import com.hnu.service.DeviceInfoService;
import com.hnu.service.UserHistoryService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.List;

import static java.lang.Math.min;

@WebServlet(name = "UserHistoryServlet", urlPatterns = {"/history-servlet"})
public class UserHistoryServlet extends HttpServlet {
    private UserHistoryService userHistoryService=new UserHistoryService();
    public UserHistoryServlet() {
        // 无参构造函数
    }
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("historyGet");
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json; charset=UTF-8");
        String pages=req.getParameter("page");
        String userID=req.getParameter("userID");
        int page=Integer.valueOf(pages);
        int uid=Integer.valueOf(userID);
        //uid=1;
        List<History> histories=userHistoryService.getHistory(uid);
        int length=histories.size();
        int totalPages=userHistoryService.getPages(histories);
        System.out.println(length);
        JSONArray jsonArray = new JSONArray();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(int i=(page-1)*8;i<min(page*8,length);i++){
            History history=histories.get(i);
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("totalPages",totalPages);
            jsonObject.put("id",history.getHisid());
            jsonObject.put("content",history.getContent());
            jsonObject.put("time",sdf.format(history.getTime()));
            jsonArray.add(jsonObject);
        }
        System.out.println(page+1);
        System.out.println(jsonArray.toJSONString());
        PrintWriter out = resp.getWriter();
        out.print(jsonArray.toJSONString());
        out.flush();
        userHistoryService.close();

    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }
}
