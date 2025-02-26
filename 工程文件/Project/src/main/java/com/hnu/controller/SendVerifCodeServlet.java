package com.hnu.controller;

import com.hnu.dao.UserDaoIml;
import com.hnu.entity.CunChu;
import com.hnu.service.sendCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import org.json.JSONObject;

@WebServlet(name = "SendVerifCodeServlet", urlPatterns = {"/sendVerifCode-servlet"})
public class SendVerifCodeServlet extends HttpServlet {
    private UserDaoIml userDaoIml=new UserDaoIml();

    public SendVerifCodeServlet() throws IOException {
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json; charset=UTF-8");
        System.out.println("get");
        String email=req.getParameter("email");
        String phone=req.getParameter("telephone");
        System.out.println(email);
        System.out.println(phone);
        JSONObject response = new JSONObject();
        sendCode sendCode=new sendCode();
        CunChu cunChu=new CunChu();
        cunChu.setCode(sendCode.getRans());
        if(!sendCode.checkEmail(email)|| !sendCode.checkPhone(phone)){
            resp.getWriter().write(new JSONObject().put("result", "error").put("message", "邮箱或手机号错误").toString());
            return;
        }
        if(userDaoIml.findUserByPhone(Long.valueOf(phone))!=null || userDaoIml.findUserByEmail(email)!=null){
            resp.getWriter().write(new JSONObject().put("result", "error").put("message", "用户已存在").toString());
            return;
        }
        sendCode.SendByEmail(email);
        resp.getWriter().write(new JSONObject().put("result", "success").put("message", "发送成功").toString());
        userDaoIml.close();
        cunChu.setInstance(cunChu);
    }
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 重定向到POST方法处理
        doPost(req, resp);
    }
}
