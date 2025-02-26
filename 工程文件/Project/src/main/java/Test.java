import com.hnu.Mapper.UserMapper;
import com.hnu.dao.UserDaoIml;
import com.hnu.entity.DeviceError;
import com.hnu.entity.DeviceRepair;
import com.hnu.entity.TaskExample;
import com.hnu.entity.User;
import com.hnu.service.DeviceErrorService;
import com.hnu.service.DeviceRepairService;
import com.hnu.service.LogService;
import com.hnu.service.TaskInfoService;
import com.hnu.util.SqlSessionUtil;
import org.apache.ibatis.session.SqlSession;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

public class Test {
    public static void main(String[] arg) throws IOException {
        DeviceErrorService deviceErrorService=new DeviceErrorService();
        List<DeviceError> errors=deviceErrorService.findAllDeviceErrors();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DeviceErrorService deviceErrorService1=new DeviceErrorService();
        System.out.println(deviceErrorService1.findDeviceActionByEID(10001).getIstorepair());
        JSONArray jsonArray=new JSONArray();
        for(DeviceError deviceError:errors){
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("breakdownId",deviceError.getErrorid());
            jsonObject.put("uavId",deviceError.getDeviceid());
            jsonObject.put("breakdownTime",sdf.format(deviceError.getTime()));
            System.out.println(Integer.valueOf(deviceError.getIstorepair()));
            String isSend=deviceErrorService.getIsRepair(deviceError.getIstorepair());
            jsonObject.put("isSendToRepair",isSend);
            jsonArray.add(jsonObject);
        }
        deviceErrorService.close();
        DeviceRepairService deviceRepairService=new DeviceRepairService();
        List<DeviceRepair> repairs=deviceRepairService.findAllDeviceRepair();
        System.out.println(repairs.size());
        System.out.println(jsonArray.toJSONString());
        TaskInfoService taskInfoService=new TaskInfoService();
        List<TaskExample> taskExamples=taskInfoService.selectTaskResult();
        System.out.println(taskExamples.size());
        UserDaoIml daoIml=new UserDaoIml();
        List<User> users= daoIml.selectUsersNotInBlacklist();
        System.out.println(users.size());
        LogService logService=new LogService();
        logService.addLogToFile(1,"user login","info","系统");

    }

}
