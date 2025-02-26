package com.hnu.Mapper;

import com.hnu.entity.History;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface HistoryMapper {
    List<History> findAllUserHistory();
    List<History> findHistoryByUId(int id);
    History findHistoryByHId(int hisid);
    History findHistoryByHUId(@Param("hisid") int hisid,@Param("userid" ) int userid);
    List<History> findHistoryByDate(Date date);
    List<History> findHistoryByDateId(@Param("time") Date date,@Param("userid") int userid);
    List<History> findHistoryByContentID(@Param("content") String content,@Param("userid") int userid);
    List<History> findHistoryByContent(String content);
    int addHistory(History history);
    int deleteUserByUId(int userid);
    int deleteUserByHId(int hisid);

}
