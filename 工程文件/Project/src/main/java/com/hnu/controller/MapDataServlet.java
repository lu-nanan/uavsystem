package com.hnu.controller;

import com.hnu.entity.MapData;
import com.hnu.service.MapDataService;
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

@WebServlet(name = "MapDataServlet", urlPatterns = {"/getMapData"})
public class MapDataServlet extends HttpServlet {

    private MapDataService mapDataService = new MapDataService();

    /**
     * 处理GET请求，用于获取地图数据列表并返回给客户端
     *
     * @param req  HttpServletRequest对象，包含请求相关信息
     * @param resp HttpServletResponse对象，用于设置响应相关信息
     * @throws ServletException 如果在Servlet处理过程中出现异常
     * @throws IOException      如果出现输入输出相关异常
     */
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json; charset=UTF-8");
        try {
            List<MapData> mapDataList = mapDataService.getAllMapData();
            // 使用JSON对象来构建响应数据结构
            JSONArray jsonArray = new JSONArray();
            for (MapData mapData : mapDataList)
            {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", mapData.getId());
                jsonObject.put("xCoordinate", mapData.getxCoordinate());
                jsonObject.put("yCoordinate", mapData.getyCoordinate());
                jsonObject.put("description", mapData.getDescription());
                jsonObject.put("isFlyable", mapData.isFlyable());
                jsonObject.put("isParkable", mapData.isParkable());
                jsonObject.put("blockId", mapData.getBlockId());
                jsonArray.put(jsonObject);
            }
            mapDataService.close();
            PrintWriter out = resp.getWriter();
            out.print(jsonArray.toString());
            out.flush();
        } catch (Exception e) {
            handleError(resp, "获取地图数据失败");
        }
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