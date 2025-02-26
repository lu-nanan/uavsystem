package com.hnu.controller;
import com.hnu.Mapper.UserMapper;
import com.hnu.dao.UserDaoIml;
import com.hnu.entity.BlackUser;
import com.hnu.entity.User;
import com.hnu.service.BlackUserService;
import com.hnu.service.LogService;
import com.hnu.service.UserHistoryService;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.json.JSONException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.io.BufferedReader;
import java.io.*;
import org.json.JSONObject;
import com.hnu.service.sendCode;


@WebServlet(name = "LoginServlet", urlPatterns = {"/login-servlet"})
public class LoginServlet extends HttpServlet {
    private UserDaoIml userDaoIml=new UserDaoIml();
    private UserHistoryService userHistoryService=new UserHistoryService();
    private BlackUserService blackUserService=new BlackUserService();
    private LogService logService=new LogService();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() throws IOException {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */

    /*protected void doGet(HttpServletRequest req, HttpServletResponse  resp) throws ServletException, IOException {
//        // TODO Auto-generated method stub
//        response.getWriter().append("Served at: ").append(request.getContextPath());
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json; charset=UTF-8");
        User currentUser=new User().getInstance();
        String logintype = req.getParameter("loginType");//登录方式
        String phone =req.getParameter("phoneNumber");//密码
        String password = req.getParameter("password");//邮箱或手机号
        UserServiceIml userServiceIml=new UserServiceIml();
        System.out.println(logintype);//输出为null
        // 检查是否所有字段都存在
        if (logintype == null || password == null ||phone == null) {
            resp.getWriter().write(new JSONObject().put("status", "error").put("message", "JSON数据不完整").toString());
            return;
        }
        User user = userServiceIml.findUserByPhone(Long.parseLong(phone)) ;//调用loginservice的getUser方法，根据类型和值获取用户信息。
        if (user != null && user.getPassword().equals(password)) {
            String uid= String.valueOf(user.getId());
            if(logintype.equals("admin")&&user.getType()==1){
                resp.getWriter().write(new JSONObject().put("status", "200").put("uid",uid).toString());
                //resp.sendRedirect("admin.html");
            } else if (logintype.equals("user")&& user.getType()==0) {
                resp.getWriter().write(new JSONObject().put("status", "200").put("uid",uid).toString());
                //resp.sendRedirect("user.html")
            }else{
                resp.getWriter().write(new JSONObject().put("status", "error").put("message", "错误").toString());
            }

            currentUser.setInstance(user);//如果用户存在且密码匹配，则返回一个包含成功状态的JSON对象，并将当前用户实例设置为登录的用户。
        } else {
            resp.getWriter().write(new JSONObject().put("status", "error").put("message", "用户不存在或密码错误").toString());
            //req.getRequestDispatcher("index.html").forward(req, resp);

            //如果用户不存在或密码不匹配，则返回一个包含错误信息的JSON对象，并转发请求到login.jsp页面。
        }
    }

     */

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */





    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 2.从SqlSessionFactory中获取SqlSession，用它来执行sql语句
       /* SqlSession session = SqlSessionUtil.createSqlSesstion();
        UserMapper userMapper = session.getMapper(UserMapper.class);
        */
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json; charset=UTF-8");
        String account = req.getParameter("account");
        String password = req.getParameter("password");
        System.out.println(", account: " + account+ ", Password: " + password);
        sendCode sendCode=new sendCode();
        logService.close();
        User user=new User();
        if(sendCode.checkEmail(account)==true){
            user=userDaoIml.findUserByEmail(account);
        } else if (sendCode.checkPhone(account)==true) {
            Long phone= Long.valueOf(account);
            user=userDaoIml.findUserByPhone(phone);
        }
        else {
            //logService.addLogToFile(user.getId(), "用户登录失败","error","LoginServlet.java");
            System.out.println("error");
            resp.getWriter().write(new JSONObject().put("result", "error").put("message", "账号格式错误").toString());
            return;
        }

        if (user == null) {
            //logService.addLogToFile(user.getId(), "用户登录失败","error","LoginServlet.java");
            resp.getWriter().write(new JSONObject().put("result", "error").put("message", "用户不存在").toString());
            System.out.println("User not found");
        } else if(!blackUserService.findBlackByUId(user.getId()).isEmpty()){
            logService.addLogToFile(user.getId(), "用户登录失败","error","LoginServlet.java");
            resp.getWriter().write(new JSONObject().put("result", "error").put("message", "用户已禁用").toString());
            System.out.println(" error");
        }
        else if (!user.getPassword().equals(password)) {
            logService.addLogToFile(user.getId(), "用户登录失败","error","LoginServlet.java");
            resp.getWriter().write(new JSONObject().put("result", "error").put("message", "密码错误").toString());
            System.out.println("Password error");
        } else {
            String uid = String.valueOf(user.getId());
            User currentUser=new User().getInstance();
            currentUser.setInstance(user);
            HttpSession session = req.getSession();
            session.setAttribute("userid",uid);
            if(user.getType()==0){
                session.setAttribute("type","admin");
            }else {
                session.setAttribute("type","user");
            }
            String history="用户"+uid+"登录系统";
            userHistoryService.add(history,user.getId());
            System.out.println(user.getInstance().getId());
            logService.addLogToFile(user.getId(), "用户登录成功","info","LoginServlet.java");
            resp.getWriter().write(new JSONObject().put("result", "200").put("userID", uid).put("type",user.getType()).toString());
            System.out.println("yes1");

        }
        userDaoIml.close();
        blackUserService.close();
        logService.close();
        userHistoryService.close();
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 重定向到POST方法处理
        doPost(req, resp);
    }//你运行一下，我看不到运行日志和web页面；

}

