package com.hnu.Mapper;

import java.util.Date;
import java.util.List;
import com.hnu.entity.Log;

public interface LogMapper {
    List<Log> findAllLog();
    List<Log> findLogByUId(int userId);
    Log findLogByLID(int LogID);
    List<Log> findLogByLevel(String level);
    List<Log> findLogByResource(String reource);
    List<Log> findLogByMessage(String message);
    List<Log> findLogByDate(Date date);
    int addLog(Log log);
    int deleteLogByUId(int uesrId);
    int deleteUserByLId(int LogID);


}
