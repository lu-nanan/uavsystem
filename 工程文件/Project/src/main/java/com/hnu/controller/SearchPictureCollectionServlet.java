package com.hnu.controller;

import com.hnu.entity.DataCollection;
import com.hnu.entity.PictureCollection;
import com.hnu.entity.User;
import com.hnu.service.LogService;
import com.hnu.service.PictureCollectionService;
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
import java.util.Base64;
import java.util.List;

@WebServlet(name = "SearchPictureCollectionServlet",urlPatterns = {"/searchPictureCollection-servlet"})
public class SearchPictureCollectionServlet extends HttpServlet {
    private String message;
    public void init() {}
    private LogService logService=new LogService();
    private UserHistoryService userHistoryService=new UserHistoryService();
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json,charset=UTF-8");
        String searchInput = request.getParameter("searchInput");
        String searchOption = request.getParameter("searchOption");
        System.out.println(searchOption);
        PrintWriter out = response.getWriter();
        List<PictureCollection> pictureCollections = new ArrayList<>();
        PictureCollectionService pictureCollectionService = new PictureCollectionService();
        JSONArray jsonArray;
        User current=new User().getInstance();
        int id=current.getId();
        if(searchInput==null){
            pictureCollections=pictureCollectionService.findAllPicture();
            jsonArray=Trans(pictureCollections);
            out.print(jsonArray.toJSONString());
            return;
        }
        if(searchOption.equals("uavId")) {
            pictureCollections = pictureCollectionService.findPictureByUavId(Integer.valueOf(searchInput));
            jsonArray = Trans(pictureCollections);
            out.print(jsonArray.toJSONString());
        }
        String history="用户"+id+"搜索返回文本数据";
        userHistoryService.add(history,id);
        userHistoryService.close();
        pictureCollectionService.close();

    }
    public JSONArray Trans(List<PictureCollection> pictureCollections){
        int length=pictureCollections.size();
        JSONArray jsonArray=new JSONArray();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(PictureCollection pictureCollection:pictureCollections){
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("totalPages",getPages(pictureCollections));
            jsonObject.put("id",pictureCollection.getId());
            jsonObject.put("uavId",pictureCollection.getUavId());
            byte[] imageBytes = pictureCollection.getImage();
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);
            jsonObject.put("content",base64Image);
            jsonObject.put("returnTime",sdf.format(pictureCollection.getReturnTime()));
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }
    public int getPages(List<PictureCollection> pictureCollections){
        int lengh=pictureCollections.size();
        if(lengh%4==0){
            return lengh/4;
        }else {
            return lengh/4+1;
        }
    }
}
