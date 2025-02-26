package com.hnu.controller;

import com.hnu.entity.TaskExample;
import com.hnu.entity.TaskInfo;
import com.hnu.entity.User;
//import com.hnu.service.DeviceTaskService;
import com.hnu.service.TaskInfoService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.List;

import static java.lang.Math.min;

@WebServlet(name = "TaskInfoServlet", urlPatterns = {"/taskList-servlet"})
public class TaskInfoServlet extends HttpServlet {
    private TaskInfoService taskInfoService=new TaskInfoService();
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");

        resp.setContentType("application/json; charset=UTF-8");
        String pages=req.getParameter("page");
        User currentuser= new User().getInstance();
        HttpSession session = req.getSession(false);

        int userid=currentuser.getId();
        int page=Integer.valueOf(pages);
        List<TaskInfo> taskInfos=taskInfoService.findTaskByUId(userid);
        List<TaskExample> taskExamples=taskInfoService.selectTaskResultbyUid(userid);
        for(TaskExample t : taskExamples) {
            System.out.println(t.getId());
        }
        int length=taskInfos.size();
        int totalPages=taskInfoService.getPages(taskInfos);
        JSONArray jsonArray=new JSONArray();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(int i=(page-1)*6;i<min(page*6,length);i++){
            TaskInfo taskInfo=taskInfos.get(i);
            TaskExample taskExample=taskExamples.get(i);
            JSONObject jsonObject=new JSONObject();
            /*jsonObject.put("totalPages",totalPages);
            jsonObject.put("id",taskInfo.getId());
            jsonObject.put("assignTime",sdf.format(taskInfo.getTime()));
            jsonObject.put("taskStatus",taskInfo.getTaskstate(
            ));
            jsonObject.put("textContent",taskInfo.getContent());
            jsonObject.put("result",null);
            jsonObject.put("carryOutUav",null);
            jsonObject.put("taskEndingTime",null);
            jsonObject.put("dispatcher",taskInfo.getUserid());
            jsonArray.add(jsonObject);

             */
            jsonObject.put("totalPages",totalPages);
            jsonObject.put("id",taskExample.getId());
            jsonObject.put("assignTime",sdf.format(taskExample.getTime()));
            jsonObject.put("taskStatus",taskExample.getTaskstate());
            jsonObject.put("textContent",taskExample.getContent());
            if(taskExample.getDeviceid()==0){
                jsonObject.put("carryOutUav",null);
            }else {
                jsonObject.put("carryOutUav",taskExample.getDeviceid());
            }
            jsonObject.put("dispatcher",taskExample.getUserid());
            jsonArray.add(jsonObject);
        }
        taskInfoService.close();
        PrintWriter out = resp.getWriter();
        out.print(jsonArray.toJSONString());
        out.flush();
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }

}
