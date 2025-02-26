package com.hnu.controller;

import com.hnu.dao.UserDaoIml;
import com.hnu.entity.BlackUser;
import com.hnu.entity.History;
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

@WebServlet(name = "UserManageServlet", urlPatterns = {"/userManage-servlet"})
public class UserManageServlet extends HttpServlet {
    private BlackUserService blackUserService=new BlackUserService();
    private UserDaoIml userDaoIml=new UserDaoIml();

    public UserManageServlet() throws IOException {
    }
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json; charset=UTF-8");
        String pages=req.getParameter("page");
        //pages="1";
        int page=Integer.valueOf(pages);
        List<User> users=userDaoIml.selectUsersNotInBlacklist();
        int length=users.size();
        int totalPages=userDaoIml.getPages(users);
        System.out.println(length);
        JSONArray jsonArray = new JSONArray();
        System.out.println(totalPages);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(int i=(page-1)*8;i<min(page*8,length);i++){
            User user=users.get(i);
            List<BlackUser> blacklist = blackUserService.findBlackByUId(user.getId());
            blackUserService.close();
            if(blacklist.isEmpty()){
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("id",user.getId());
                jsonObject.put("totalPages",totalPages);
                jsonObject.put("email",user.getEmail());
                jsonObject.put("telephone",user.getPhone());
                jsonObject.put("password",user.getPassword());
                jsonArray.add(jsonObject);
            }
            /*
            JSONObject jsonObject=new JSONObject();

            jsonObject.put("id",user.getId());
            jsonObject.put("email",user.getEmail());
            jsonObject.put("telephone",user.getPhone());
            jsonObject.put("password",user.getPassword());
            jsonArray.add(jsonObject);

             */

        }
        System.out.println(page+1);
        System.out.println(jsonArray.toJSONString());
        PrintWriter out = resp.getWriter();
        out.print(jsonArray.toJSONString());
        out.flush();
        userDaoIml.close();

    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }
}