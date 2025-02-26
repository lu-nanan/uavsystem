package com.hnu.controller;

import com.hnu.entity.DeviceRepair;
import com.hnu.entity.TaskExample;
import com.hnu.entity.User;
import com.hnu.service.*;
import com.hnu.service.DeviceRepairService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@WebServlet(name = "CheckProblemServlet", urlPatterns = {"/uacCheck-servlet"})
public class CheckProblemServlet extends HttpServlet {
    //uavOmId,problem。
    private com.hnu.service.DeviceRepairService deviceRepairService=new DeviceRepairService();
    private UserHistoryService userHistoryService=new UserHistoryService();
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json; charset=UTF-8");
        String id=req.getParameter("id");
        String problem=req.getParameter("problem");
        User user=new User().getInstance();
        int i=deviceRepairService.updatePepairbyProblem(problem,user.getId(),Integer.valueOf(id));
        /*
        1.无人机运维：检修时，如果问题是”无法与中心通讯“，就将当前动作改为”故障，需要信息转传输“
如果是其他问题，就将当前动作改为”故障“，相应任务(如果有的话)状态改为”未分配“
         */
        int j=0;
        DeviceErrorService deviceErrorService=new DeviceErrorService();
        int uavid=deviceErrorService.findDeviceActionByEID(Integer.valueOf(id)).getDeviceid();
        DeviceStateService deviceStateService=new DeviceStateService();
        //无人机状态修改
        if(problem.equals("无法与中心通讯")){
            j= deviceStateService.updateActionById("故障，需要信息转传输",uavid);
        }else {
            j=deviceStateService.updateActionById("故障",uavid);
            TaskInfoService  taskInfoService=new TaskInfoService();
            List<TaskExample> taskExamples=taskInfoService.selectTaskResult();
            for(TaskExample taskExample:taskExamples){
                if(taskExample.getDeviceid()==uavid && taskExample.getTaskstate().equals("进行中")){
                    taskInfoService.updateTaskStatByTId("未分配",taskExample.getId());
                }
            }
        }

        if(i > 0 && j > 0){
            String history="无人机故障"+id+"由用户"+user.getId()+"送修";
            userHistoryService.add(history,user.getId());
            System.out.println("ok");
            resp.getWriter().write(new JSONObject().put("result", "200").put("message", "处理成功").toString());
        }else{
            System.out.println("s");
            resp.getWriter().write(new JSONObject().put("result", "error").put("message", "处理失败").toString());
        }
        userHistoryService.close();
        deviceRepairService.close();

    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doPost(request, response);
    }

}
