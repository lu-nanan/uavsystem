package com.hnu.controller;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
@WebFilter(urlPatterns = "/*")
public class AuthenticationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 初始化操作
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);
        // 获取请求的URL
        String requestURI = httpRequest.getRequestURI();
        // 检查用户是否已登录
        boolean isLoggedIn = (session != null && session.getAttribute("userid") != null);

        // 定义不需要登录即可访问的路径
        String[] allowedPaths = {"/html/newLog.html","/login-servlet", "/register-servlet","/html/newregister.html","/css/style.css","sendVerifCode-servlet","/html/image/img.png"};

        // 检查当前请求是否需要登录
        boolean isAllowed = false;
        for (String path : allowedPaths) {
            if (requestURI.contains(path)) {
                isAllowed = true;
                break;
            }
        }

        if (isLoggedIn || isAllowed) {
            // 用户已登录或请求的是允许的路径，继续处理请求
            chain.doFilter(request, response);
        } else {
            // 用户未登录，重定向到登录页面
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/html/newLog.html");
        }
    }

    @Override
    public void destroy() {
        // 清理资源
    }
}










