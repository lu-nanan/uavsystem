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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static java.lang.Math.min;

@WebServlet(name = "UserFeedbackServlet", urlPatterns = {"/userFeedback-servlet"})
public class UserFeedbackServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json; charset=UTF-8");
        String userid=req.getParameter("userID");
        String pages = req.getParameter("page");
        System.out.println("userID:"+userid+" page:"+pages);
        if(userid==null||pages==null){
            resp.getWriter().write(new org.json.JSONObject().put("result", "error").put("message", "参数错误").toString());
            System.out.println("错误");
            return;
        }
        int page=Integer.valueOf(pages);
        FeedBackService feedBackService=new FeedBackService();
        System.out.println(userid);
        List<FeedBack> feedBacks= null;
        /*if(userid==null){
            userid="1";
        }*/
        feedBacks = feedBackService.findFeedById(Integer.valueOf(userid));
        int length=feedBacks.size();
        int totalPages=feedBackService.getPages(feedBacks);
        System.out.println(length);
        JSONArray jsonArray = new JSONArray();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(int i=(page-1)*8;i<min(page*8,length);i++){
            FeedBack feedBack=feedBacks.get(i);
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("totalPages",totalPages);
            jsonObject.put("id",feedBack.getFeedid());
            jsonObject.put("time",sdf.format(feedBack.getTime()));
            jsonObject.put("content",feedBack.getContent());
            jsonArray.add(jsonObject);
            System.out.println(i);
        }
        System.out.println(userid);
        PrintWriter out = resp.getWriter();
        feedBackService.close();
        out.print(jsonArray.toJSONString());
        out.flush();
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doPost(request, response);
    }
}
