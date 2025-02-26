package com.hnu.controller;

import com.hnu.entity.FeedBack;
import com.hnu.service.FeedBackService;
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
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.min;
@WebServlet(name = "AdminFeedBackServlet", urlPatterns = {"/adminUserFeedback-servlet"})
public class AdminFeedBackServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json; charset=UTF-8");
        String pages = req.getParameter("page");
        int page=Integer.valueOf(pages);
        FeedBackService feedBackService=new FeedBackService();
        List<FeedBack> feedBacks= new ArrayList<>();
        feedBacks = feedBackService.findAllFeed();
        int length=feedBacks.size();
        int totalPages=feedBackService.getPages(feedBacks);
        System.out.println(length);
        JSONArray jsonArray = new JSONArray();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(int i=(page-1)*8;i<min(page*8,length);i++){
            FeedBack feedBack=feedBacks.get(i);
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("id",feedBack.getFeedid());
            jsonObject.put("userId",feedBack.getUserid());
            jsonObject.put("totalPages",totalPages);
            jsonObject.put("time",sdf.format(feedBack.getTime()));
            jsonObject.put("content",feedBack.getContent());
            String ishandle=isHandled(feedBack.getIshandle());
            jsonObject.put("isRead",ishandle);
            jsonArray.add(jsonObject);
        }
        feedBackService.close();
        PrintWriter out = resp.getWriter();
        out.print(jsonArray.toJSONString());
        out.flush();
    }
    public String isHandled(Boolean ishandle){
        String ishanled=new String();
        if(ishandle==true){
            ishanled="已处理";

        }else {
            ishanled="未处理";
        }
        return ishanled;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }
}
