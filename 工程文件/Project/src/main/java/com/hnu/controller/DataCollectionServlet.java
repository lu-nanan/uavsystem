package com.hnu.controller;

import com.hnu.entity.DataCollection;
import com.hnu.service.DataCollectionService;
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

@WebServlet(name = "DataCollectionServlet",urlPatterns = {"/dataCollection-servlet"})
public class DataCollectionServlet extends HttpServlet {
    private String message;
    public void init() {}
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("dataCollectionGet");
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json,charset=UTF-8");
        String pageStr = request.getParameter("page");
        if (pageStr == null) {
            response.getWriter().write(new org.json.JSONObject().put("result","error").put("message","参数错位").toString());
            return;
        }
        DataCollectionService dataCollectionService = new DataCollectionService();
        List<DataCollection> dataCollections;
        dataCollections = dataCollectionService.findAllData();
        int length = dataCollections.size();
        int page = Integer.valueOf(pageStr);
        int totalPages=dataCollectionService.getPages(dataCollections);
        JSONArray jsonArray = new JSONArray();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(int i=(page-1)*8;i<min(page*8,length);i++){
            DataCollection dataCollection=dataCollections.get(i);
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("totalPages",totalPages);
            jsonObject.put("id",dataCollection.getDataId());
            jsonObject.put("uavId",dataCollection.getUavId());
            jsonObject.put("content",dataCollection.getContent());
            jsonObject.put("returnTime",sdf.format(dataCollection.getReturnTime()));
            jsonArray.add(jsonObject);
        }
        dataCollectionService.close();
        PrintWriter out = response.getWriter();
        out.print(jsonArray.toJSONString());
        out.flush();
    }
}
