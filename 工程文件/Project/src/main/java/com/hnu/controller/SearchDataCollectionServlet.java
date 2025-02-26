package com.hnu.controller;

import com.hnu.entity.DataCollection;
import com.hnu.entity.User;
import com.hnu.service.DataCollectionService;
import com.hnu.service.LogService;
import com.hnu.service.UserHistoryService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.min;
@WebServlet(name = "SearchDataCollectionServlet",urlPatterns = {"/searchDataCollection-servlet"})
public class SearchDataCollectionServlet extends HttpServlet {
    private String message;
    public void init() {}
    private LogService logService=new LogService();
    private UserHistoryService userHistoryService=new UserHistoryService();
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("dataCollectionGet");
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json,charset=UTF-8");
        String searchInput = request.getParameter("searchInput");
        String searchOption = request.getParameter("searchOption");
        System.out.println(searchOption);
        PrintWriter out = response.getWriter();
        List<DataCollection> dataCollections = new ArrayList<>();
        DataCollectionService dataCollectionService = new DataCollectionService();
        JSONArray jsonArray;
        User current=new User().getInstance();
        int id=current.getId();
        if(searchInput==null){
            dataCollections=dataCollectionService.findAllData();
            jsonArray=Trans(dataCollections);
            out.print(jsonArray.toJSONString());
            return;
        }
        if(searchOption.equals("uavId")) {
            dataCollections = dataCollectionService.findDataByUavId(Integer.valueOf(searchInput));
            jsonArray = Trans(dataCollections);
            out.print(jsonArray.toJSONString());
        }
        else if(searchOption.equals("time")) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                dataCollections = dataCollectionService.findDataByTime(sdf.parse(searchInput));
                jsonArray = Trans(dataCollections);
                out.print(jsonArray.toJSONString());
            }catch (ParseException e) {
                logService.addLogToFile(id,"转换错误","error","SearchDataCollectionServlet");
                throw new RuntimeException(e);
            }

        }


        String history="用户"+id+"搜索返回文本数据";
        userHistoryService.add(history,id);

    }
    public JSONArray Trans(List<DataCollection> dataCollections){
        int length=dataCollections.size();
        JSONArray jsonArray=new JSONArray();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(DataCollection dataCollection:dataCollections){
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("id",dataCollection.getDataId());
            jsonObject.put("uavId",dataCollection.getUavId());
            jsonObject.put("content",dataCollection.getContent());
            jsonObject.put("returnTime",sdf.format(dataCollection.getReturnTime()));
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }
}
