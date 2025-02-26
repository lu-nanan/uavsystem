package com.hnu.controller;

import com.hnu.entity.UavPathInfo;
import com.hnu.service.UavPathInfoService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "UavPathInfoDetailServlet", urlPatterns = {"/getUavPathInfoDetail"})
public class UavPathInfoDetailServlet extends HttpServlet {
    private UavPathInfoService uavPathInfoService = new UavPathInfoService();

    /**
     * 处理POST请求，用于获取指定路径ID的详细信息
     *
     * @param req  HttpServletRequest对象，包含请求相关信息
     * @param resp HttpServletResponse对象，用于设置响应相关信息
     * @throws ServletException 如果在Servlet处理过程中出现异常
     * @throws IOException      如果出现输入输出相关异常
     */
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json; charset=UTF-8");

        try {
            String searchKeyword = req.getParameter("pathId");
            Object result = uavPathInfoService.getUavPathInfoByPathId(Integer.parseInt(searchKeyword));

            UavPathInfo uavPathInfo = (UavPathInfo) result;

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("pathId", uavPathInfo.getPathId());
            jsonObject.put("uavId", uavPathInfo.getUavId());
            jsonObject.put("startX", uavPathInfo.getStartX());
            jsonObject.put("startY", uavPathInfo.getStartY());
            jsonObject.put("endX", uavPathInfo.getEndX());
            jsonObject.put("endY", uavPathInfo.getEndY());
            jsonObject.put("path", uavPathInfo.getPath());
            PrintWriter out = resp.getWriter();
            out.print(jsonObject.toString());
            out.flush();
        } catch (Exception e) {
            handleError(resp, "获取无人机详细路径信息失败");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doPost(request, response);
    }

    /**
     * 辅助方法，用于处理出现异常时的错误响应
     *
     * @param response HttpServletResponse对象，用于设置错误响应信息
     * @param message  错误提示信息
     * @throws IOException 如果出现写入响应输出流相关的异常
     */
    private void handleError(HttpServletResponse response, String message) throws IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", "error");
        jsonObject.put("message", message);
        PrintWriter out = response.getWriter();
        out.print(jsonObject.toString());
        out.flush();
    }
}
