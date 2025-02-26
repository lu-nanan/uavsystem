package com.hnu.controller;

import com.hnu.entity.TransMessage;
import com.hnu.entity.User;
import com.hnu.service.TransService;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.min;
@WebServlet(name = "searchTransServlet", urlPatterns = {"/searchTrans-servlet"})
public class SearchTransServlet extends HttpServlet {
    private TransService transService=new TransService();
    private UserHistoryService userHistoryService=new UserHistoryService();
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json; charset=UTF-8");
        String searchOption=req.getParameter("searchOption");
        String searchInput=req.getParameter("searchInput");
        List<TransMessage> transMessages=new ArrayList<>();
        transMessages=transService.searchTrans(searchOption,searchInput);
        JSONArray jsonArray=new JSONArray();
        jsonArray=Trans(transMessages);
        System.out.println(jsonArray.toJSONString());
        PrintWriter out = resp.getWriter();
        out.print(jsonArray.toJSONString());
        out.flush();

        User current=new User().getInstance();
        int id=current.getId();
        String history="用户"+id+"搜索信息转传输情况";
        userHistoryService.add(history,id);
        transService.close();
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }
    public JSONArray Trans(List<TransMessage> transMessages){
        JSONArray jsonArray=new JSONArray();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(TransMessage transMessage:transMessages){
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("transId",transMessage.getTransId());//转传输ID
            jsonObject.put("send",transMessage.getSend());//发送方
            jsonObject.put("trans",transMessage.getTrans());//转送方
            jsonObject.put("time",sdf.format(transMessage.getDate()));//时间
            jsonObject.put("isSuccess",transMessage.getIsSuccess());//是否成功
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }
}
