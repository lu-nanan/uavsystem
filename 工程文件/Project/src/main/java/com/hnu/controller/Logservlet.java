package com.hnu.controller;

import com.hnu.entity.FeedBack;
import com.hnu.entity.Log;
import com.hnu.entity.TaskInfo;
import com.hnu.entity.User;
import com.hnu.service.FeedBackService;
import com.hnu.service.LogService;
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
import java.util.Date;
import java.util.List;

import static java.lang.Math.min;

@WebServlet(name = "Logservlet", urlPatterns = {"/log-servlet"})
public class Logservlet extends HttpServlet {
    private LogService logService=new LogService();
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json; charset=UTF-8");
        String pages=req.getParameter("page");

//        try {
//            List<Log> logsToInsert = logService.importLogsFromFilesAndDelete();
//            System.out.println(logsToInsert.get(0).getUserId());
//            if (logsToInsert != null){
//                for (Log log : logsToInsert) {
//                    logService.addLog(log);
//                }
//            }
//        } catch (ParseException e) {
//            throw new RuntimeException(e);
//        }

        List<Log> logs=logService.findAllLog();
        //pages="1";
        int page=Integer.valueOf(pages);
        int length=logs.size();
        int totalPages=logService.getPages(logs);
        JSONArray jsonArray=new JSONArray();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(int i=(page-1)*8;i<min(page*8,length);i++){
            Log log=logs.get(i);
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("totalPages",totalPages);
            jsonObject.put("id",log.getLogId());
            jsonObject.put("time",sdf.format(log.getDate()));
            jsonObject.put("rank",log.getLevel());
            jsonObject.put("source",log.getResource());
            jsonObject.put("content",log.getMessage());
            jsonArray.add(jsonObject);
        }
        PrintWriter out = resp.getWriter();
        out.print(jsonArray.toJSONString());
        out.flush();

        logService.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doPost(request, response);
    }
}
