package com.hnu.controller;

import com.hnu.dao.UserDaoIml;
import com.hnu.entity.BlackUser;
import com.hnu.entity.User;
import com.hnu.service.BlackUserService;
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
@WebServlet(name = "BlackUserServlet", urlPatterns = {"/blackUserlist-servlet"})
public class BlackUserServlet extends HttpServlet   {
    private BlackUserService blackUserService=new BlackUserService();
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json; charset=UTF-8");
        String pages=req.getParameter("page");
        int page=Integer.valueOf(pages);
        List<BlackUser> blackUsers=blackUserService.findAllBlackUsers();
        int length=blackUsers.size();
        int totalPages=blackUserService.getPages(blackUsers);
        System.out.println(length);
        JSONArray jsonArray = new JSONArray();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(int i=(page-1)*8;i<min(page*8,length);i++){
            BlackUser blackUser=blackUsers.get(i);
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("userid",blackUser.getUserid());
            jsonObject.put("adminid",blackUser.getAdminid());
            jsonObject.put("totalPages",totalPages);
            jsonObject.put("dotime",sdf.format(blackUser.getDotime()));
            jsonArray.add(jsonObject);
        }
        System.out.println(page+1);
        System.out.println(jsonArray.toJSONString());
        PrintWriter out = resp.getWriter();
        out.print(jsonArray.toJSONString());
        out.flush();
        blackUserService.close();

    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }
}
