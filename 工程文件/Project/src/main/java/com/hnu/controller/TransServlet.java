package com.hnu.controller;

import com.hnu.entity.TaskInfo;
import com.hnu.entity.TransMessage;
import com.hnu.entity.User;
import com.hnu.service.TaskInfoService;
import com.hnu.service.TransService;
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

@WebServlet(name = "TransServlet", urlPatterns = {"/trans-servlet"})
public class TransServlet extends HttpServlet {
    private TransService transService=new TransService();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json; charset=UTF-8");
        String pages=req.getParameter("page");
        int page=Integer.valueOf(pages);
        List<TransMessage> transMessages=transService.findTAllTrans();
        int length=transMessages.size();
        int totalPage=transService.getPage(transMessages);
        JSONArray jsonArray=new JSONArray();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(int i=(page-1)*8;i<min(page*8,length);i++){
            TransMessage transMessage=transMessages.get(i);
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("totalPage",totalPage);//
            jsonObject.put("transId",transMessage.getTransId());//转传输ID
            jsonObject.put("send",transMessage.getSend());//发送方
            jsonObject.put("trans",transMessage.getTrans());//转送方
            jsonObject.put("time",sdf.format(transMessage.getDate()));//时间
            jsonObject.put("isSuccess",transMessage.getIsSuccess());//是否成功
            jsonArray.add(jsonObject);
        }
        System.out.println(jsonArray.toJSONString());
        PrintWriter out = resp.getWriter();
        out.print(jsonArray.toJSONString());
        out.flush();
        transService.close();
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }

}
