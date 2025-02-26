package com.hnu.controller;

import com.hnu.entity.TaskExample;
import com.hnu.entity.TaskInfo;
import com.hnu.entity.User;
import com.hnu.service.LogService;
import com.hnu.service.TaskInfoService;
import com.hnu.service.UserHistoryService;
import com.hnu.util.SqlSessionUtil;
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
@WebServlet(name = "SearchTaskServlet", urlPatterns = {"/SearchtaskList-servlet"})
public class SearchTaskServlet extends HttpServlet {
    private TaskInfoService taskInfoService=new TaskInfoService();
    private LogService logService=new LogService();
    private UserHistoryService userHistoryService=new UserHistoryService();
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("sear");
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json; charset=UTF-8");
        String searchInput=req.getParameter("searchInput");
        String searchOption=req.getParameter("searchOption");
        System.out.println(searchOption);
        System.out.println(searchInput);
        User currentuser= new User().getInstance();
        int userid=currentuser.getId();
        List<TaskInfo> taskInfos=new ArrayList<>();
        List<TaskExample> taskExamples=new ArrayList<>();
        JSONArray jsonArray=new JSONArray();
        PrintWriter out = resp.getWriter();
        if(searchOption==null){
            taskExamples=taskInfoService.selectTaskResultbyUid(userid);
            jsonArray=Trans(taskInfos);
            out.print(jsonArray.toJSONString());
            out.flush();
            taskInfoService.close();
            return;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(searchOption.equals("assignTime")){
            try {
                //taskInfos.add(taskInfoService.findTaskByDateID(sdf.parse(searchInput),userid));
                taskExamples=taskInfoService.selectTaskResultbyDid(sdf.parse(searchInput),userid);
                System.out.println("success");
            } catch (ParseException e) {
                logService.addLogToFile(userid,"时间转换失败","error","SearchTaskServlet.java");
                throw new RuntimeException(e);
            }
        } else if (searchOption.equals("dispatcher")) {
            taskExamples=taskInfoService.selectTaskResultbyUid(userid);
            //taskInfos=taskInfoService.findTaskByUId(userid);
        } else if (searchOption.equals("taskContent")) {
            taskExamples=taskInfoService.selectTaskResultbyCid(searchInput,userid);
            //taskInfos=taskInfoService.findTaskByContentID(searchInput,userid);
        } else if (searchOption.equals("taskStatus")) {
            taskExamples=taskInfoService.selectTaskResultbySid(searchInput,userid);
            // taskInfos=taskInfoService.findTaskBystateID(searchInput,userid);
        }else if (searchOption.equals("carryOutUav")){
            taskExamples=taskInfoService.selectTaskResultbyUav(Integer.valueOf(searchInput),userid);
        }else {
            taskExamples=taskInfoService.selectTaskResultbyUid(userid);
            //taskInfos=taskInfoService.findTaskByUId(userid);
        }
        //jsonArray=Trans(taskInfos);

        User current=new User().getInstance();
        int id=current.getId();
        String history="用户"+id+"搜索任务信息";
        userHistoryService.add(history,id);
        jsonArray=Trans1(taskExamples);
        System.out.println(jsonArray.toJSONString());
        out.print(jsonArray.toJSONString());
        out.flush();
        taskInfoService.close();
        userHistoryService.close();
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }
    public JSONArray Trans(List<TaskInfo> taskInfos){
        JSONArray jsonArray=new JSONArray();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(TaskInfo taskInfo:taskInfos){
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("id",taskInfo.getId());
            jsonObject.put("assignTime",sdf.format(taskInfo.getTime()));
            jsonObject.put("taskStatus",taskInfo.getTaskstate());
            jsonObject.put("carryOutUav",null);
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }
    public JSONArray Trans1(List<TaskExample> taskExamples){
        JSONArray jsonArray=new JSONArray();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(TaskExample taskExample:taskExamples){
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("id",taskExample.getId());
            jsonObject.put("assignTime",sdf.format(taskExample.getTime()));
            jsonObject.put("taskStatus",taskExample.getTaskstate());
            jsonObject.put("textContent",taskExample.getContent());
            jsonObject.put("carryOutUav",taskExample.getDeviceid());
            jsonObject.put("dispatcher",taskExample.getUserid());
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

}
