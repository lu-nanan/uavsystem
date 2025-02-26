package com.hnu.controller;

import com.hnu.dao.UserDaoIml;
import com.hnu.entity.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter(urlPatterns = "/html/admin/*")
public class AddminFilter implements Filter {

    public AddminFilter() throws IOException {
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 初始化操作（如果需要）
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false); // false表示不创建新的session
        // 如果session不存在或者用户不是管理员，则重定向到登录页面或主页
        if (session == null||session.getAttribute("userid") == null||session.getAttribute("type") == null) {
            //httpResponse.sendRedirect("/UVASystem_war_exploded/html/newLog.html"); // 重定向到主页或登录页
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/html/newLog.html");
        } else if(session.getAttribute("type").equals("admin")) {
            chain.doFilter(request, response);
        }else {
            //httpResponse.sendRedirect("/UVASystem_war_exploded/html/newLog.html");
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/html/newLog.html");
        }
    }

    @Override
    public void destroy() {
        // 清理资源（如果需要）
    }
}












