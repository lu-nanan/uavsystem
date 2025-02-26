package com.hnu.controller;

import com.hnu.entity.DataCollection;
import com.hnu.entity.PictureCollection;
import com.hnu.service.PictureCollectionService;
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
import java.util.Base64;
import java.util.List;

import static java.lang.Math.min;

@WebServlet(name = "PictureCollectionServlet",urlPatterns = {"/pictureCollection-servlet"})
public class PictureCollectionServlet extends HttpServlet {
    private String message;
    public void init() {}
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json,charset=UTF-8");
        String pageStr = request.getParameter("page");
        if (pageStr == null) {
            response.getWriter().write(new org.json.JSONObject().put("result","error").put("message","参数错位").toString());
            return;
        }
        PictureCollectionService PictureCollectionService = new PictureCollectionService();
        List<PictureCollection> pictureCollections;
        pictureCollections = PictureCollectionService.findAllPicture();
        int length = pictureCollections.size();
        int page = Integer.valueOf(pageStr);
        JSONArray jsonArray = new JSONArray();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(int i=(page-1)*4;i<min(page*4,length);i++){
            PictureCollection pictureCollection=pictureCollections.get(i);
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("totalPages",getPages(pictureCollections));
            System.out.println(getPages(pictureCollections));
            jsonObject.put("id",pictureCollection.getId());
            jsonObject.put("uavId",pictureCollection.getUavId());
            byte[] imageBytes = pictureCollection.getImage();
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);
            jsonObject.put("content",base64Image);
            jsonObject.put("returnTime",sdf.format(pictureCollection.getReturnTime()));
            jsonArray.add(jsonObject);
        }
        PrintWriter out = response.getWriter();
        PictureCollectionService.close();
        out.print(jsonArray.toJSONString());
        out.flush();
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
