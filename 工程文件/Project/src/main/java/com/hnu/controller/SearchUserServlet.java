package com.hnu.controller;

import com.hnu.dao.UserDaoIml;
import com.hnu.entity.BlackUser;
import com.hnu.entity.Log;
import com.hnu.entity.User;
import com.hnu.service.BlackUserService;
import com.hnu.service.UserHistoryService;
import com.hnu.service.sendCode;
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
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "SearchUserServlet", urlPatterns = {"/userManageSea-servlet"})
public class SearchUserServlet extends HttpServlet {
    private UserDaoIml userDaoIml=new UserDaoIml();
    private BlackUserService blackUserService=new BlackUserService();
    private UserHistoryService userHistoryService=new UserHistoryService();
    public SearchUserServlet() throws IOException {
    }
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json; charset=UTF-8");
        String searchInput = req.getParameter("searchInput");
        String searchOption = req.getParameter("searchOption");
        sendCode sendCode=new sendCode();
        List<User> users=new ArrayList<>();
        users=userDaoIml.searchUser(searchInput,searchOption);
        JSONArray jsonArray=new JSONArray();
        jsonArray=Trans(users);
        User current=new User().getInstance();
        int id=current.getId();
        String history="管理员"+id+"搜索用户";
        userHistoryService.add(history,id);
        userDaoIml.close();
        System.out.println(jsonArray.toJSONString());
        PrintWriter out = resp.getWriter();
        out.print(jsonArray.toJSONString());
        out.flush();
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }
    public JSONArray Trans(List<User> users) {
        JSONArray jsonArray = new JSONArray();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (User user:users){
            List<BlackUser> blacklist = blackUserService.findBlackByUId(user.getId());
            blackUserService.close();
            if(blacklist.isEmpty()){
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("id",user.getId());
                jsonObject.put("email",user.getEmail());
                jsonObject.put("telephone",user.getPhone());
                jsonObject.put("password",user.getPassword());
                jsonArray.add(jsonObject);
            }

        }
        return jsonArray;
    }
}
