package com.hnu.controller;

import com.hnu.dao.UserDaoIml;
import com.hnu.entity.FeedBack;
import com.hnu.entity.User;
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
import java.util.List;

import static java.lang.Math.min;

@WebServlet(name = "UserInformationServlet", urlPatterns = {"/userInformation-servlet"})
public class UserInformationServlet extends HttpServlet {
    private UserDaoIml userDaoIml=new UserDaoIml();

    public UserInformationServlet() throws IOException {
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json; charset=UTF-8");
        String userid=req.getParameter("id");
        User user=userDaoIml.findUserById(Integer.valueOf(userid));
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("email",user.getEmail());
        jsonObject.put("telephone",user.getPhone());
        PrintWriter out = resp.getWriter();
        out.print(jsonObject.toString());
        userDaoIml.close();
        out.flush();
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }
}
