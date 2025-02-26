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
import java.util.List;

@WebServlet(name = "UavPathInfoServlet", urlPatterns = {"/getUavPathInfo"})
public class UavPathInfoServlet extends HttpServlet {

    private UavPathInfoService uavPathInfoService = new UavPathInfoService();

    /**
     * 处理GET请求，根据不同条件获取无人机路径信息并返回给客户端
     * 若没有传递搜索条件，则返回所有路径信息；若通过路径id搜索，则返回对应单个路径信息；
     * 可根据实际情况扩展更多搜索条件的处理逻辑
     *
     * @param req  HttpServletRequest对象，包含请求相关信息
     * @param resp HttpServletResponse对象，用于设置响应相关信息
     * @throws ServletException 如果在Servlet处理过程中出现异常
     * @throws IOException      如果出现输入输出相关异常
     */
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json; charset=UTF-8");
        System.out.println("55");
        try {
            // 获取前端传递的搜索条件（searchBy）和关键词（searchKeyword）
            String searchBy = req.getParameter("searchBy");
            String searchKeyword = req.getParameter("searchKeyword");
            int page = Integer.valueOf(req.getParameter("page"));
            System.out.println(page);
            Object result;
            if (searchBy == null || searchKeyword == null) {
                // 如果没有搜索条件，获取所有路径信息（这里假设UavPathInfoService有获取所有信息的方法，返回List<UavPathInfo>）
                result = uavPathInfoService.getAllUavPathInfo();
            } else if ("pathId".equals(searchBy)) {
                // 根据路径id查询单个路径信息，返回UavPathInfo对象
                int pathId = Integer.parseInt(searchKeyword);
                result = uavPathInfoService.getUavPathInfoByPathId(pathId);
            } else if ("uavId".equals(searchBy)) {
                // 根据无人机id查询多个路径信息，返回List<UavPathInfo>对象
                int uavId = Integer.parseInt(searchKeyword);
                result = uavPathInfoService.getUavPathInfoByUavId(uavId);
            } else {
                // 如果搜索条件不合法，返回空对象或者合适的错误提示信息（可根据实际需求调整错误处理方式）
                result = null;
            }

            // 根据查询结果类型进行相应的JSON格式构建和返回
            if (result instanceof UavPathInfo) {
                // 如果是单个UavPathInfo对象，构建单个对象的JSON格式返回
                UavPathInfo uavPathInfo = (UavPathInfo) result;
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("pathId", uavPathInfo.getPathId());
                jsonObject.put("uavId", uavPathInfo.getUavId());
                jsonObject.put("startX", uavPathInfo.getStartX());
                jsonObject.put("startY", uavPathInfo.getStartY());
                jsonObject.put("endX", uavPathInfo.getEndX());
                jsonObject.put("endY", uavPathInfo.getEndY());
                jsonObject.put("path", uavPathInfo.getPath());
                jsonObject.put("totalPages",1);
//                jsonObject.put("totalPages",uavPathInfoService.getPages(uavPathInfoService.getAllUavPathInfo()));
//                System.out.println(uavPathInfoService.getPages(uavPathInfoService.getAllUavPathInfo()));
                JSONArray jsonArray = new JSONArray();
                jsonArray.put(jsonObject);
                PrintWriter out = resp.getWriter();
                out.print(jsonArray.toString());
                out.flush();
            } else if (result instanceof java.util.List) {
                // 如果是多个路径信息的列表（List<UavPathInfo>），构建包含多个对象的JSON数组格式返回
                List<UavPathInfo> uavPathInfoList = (List<UavPathInfo>) result;
                int sum = uavPathInfoList.size();
                int totalPage;
                if (sum % 8 == 0) {
                    totalPage = sum / 8;
                } else {
                    totalPage = (sum - (sum % 8)) / 8 + 1;
                }
                JSONArray jsonArray = new JSONArray();
                for (int i = ((page - 1) * 8); i < (((page - 1) * 8) + 8) && i < sum; i ++)
                {
                    UavPathInfo uavPathInfo = uavPathInfoList.get(i);
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("pathId", uavPathInfo.getPathId());
                    jsonObject.put("uavId", uavPathInfo.getUavId());
                    jsonObject.put("startX", uavPathInfo.getStartX());
                    jsonObject.put("startY", uavPathInfo.getStartY());
                    jsonObject.put("endX", uavPathInfo.getEndX());
                    jsonObject.put("endY", uavPathInfo.getEndY());
                    jsonObject.put("path", uavPathInfo.getPath());
                    jsonObject.put("totalPages",totalPage);
                    jsonArray.put(jsonObject);
                }
                PrintWriter out = resp.getWriter();
                out.print(jsonArray.toString());
                out.flush();
            } else {
                // 如果结果不符合预期，返回错误提示信息（可根据实际需求完善错误处理逻辑）
                handleError(resp, "查询结果不符合预期");
            }
            uavPathInfoService.close();
        } catch (Exception e) {
            handleError(resp, "获取无人机路径信息失败");
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