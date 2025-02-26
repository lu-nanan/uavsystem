package com.hnu.controller;

import com.hnu.Mapper.UserMapper;
import com.hnu.dao.UserDaoIml;
import com.hnu.entity.CunChu;
import com.hnu.entity.User;
import com.hnu.service.UserHistoryService;
import com.hnu.service.UserService;
import com.hnu.util.SqlSessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.ibatis.session.SqlSession;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import com.hnu.service.sendCode;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "registerServlet", value = {"/register-servlet"})
public class RegisterServlet  extends HttpServlet {


    @Autowired
    private UserDaoIml userDaoIml=new UserDaoIml();
    private UserHistoryService userHistoryService=new UserHistoryService();
    private CunChu cunChu=new CunChu().getInstance();


    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterServlet() throws IOException {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest req, HttpServletResponse  resp) throws ServletException, IOException {
//
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json; charset=UTF-8");
        String email=req.getParameter("email");
        String phone=req.getParameter("telephone");
        String password=req.getParameter("password");
        String confirmPassword=req.getParameter("confirmPassword");
        String verificationCode=req.getParameter("verifCode");
        sendCode sendCode=new sendCode();
        System.out.println(email+phone+password+confirmPassword+verificationCode);
        System.out.println("regis1");
        User user=userDaoIml.findUserByPhone(Long.valueOf(phone));
        User newUser= new User().getInstance();
        if (!password.equals(confirmPassword)){
            resp.getWriter().write(new JSONObject().put("result", "error").put("message", "密码不一致").toString());
            return;
        }
        System.out.println("regis2");
        System.out.println("regis3");
        if(cunChu.getCode().equals(verificationCode)){
            newUser.setEmail(email);
            newUser.setType(1);
            newUser.setPassword(password);
            newUser.setPhone(Long.valueOf(phone));
            newUser.setInstance(newUser);
            int i=userDaoIml.addUser(newUser);
            String history="新用户注册";

            HttpSession session=req.getSession();
            session.setAttribute("userid",newUser.getId());
            int id=newUser.getId();
            userHistoryService.add(history,id);
            User.setInstance(newUser);
            if(i>0){
                resp.getWriter().write(new JSONObject().put("result", "200").put("message", "注册成功").toString());
            }else {
                resp.getWriter().write(new JSONObject().put("result", "error").put("message", "注册失败").toString());
            }
        }else{
            resp.getWriter().write(new JSONObject().put("result", "error").put("message", "验证码或其他错误").toString());
        }
        String history="新用户注册";
        User current=new User().getInstance();
        int id=current.getId();
        System.out.println(id);
        userHistoryService.add(history,id);
        userDaoIml.close();
        userHistoryService.close();
        System.out.println("regis3");
    }


    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }
}
