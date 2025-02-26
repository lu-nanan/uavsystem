package com.hnu.Mapper;

import com.hnu.entity.TransMessage;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface TransMassageMapper {
    List<TransMessage> findTAllTrans();
    List<TransMessage> findTransByTId(int transId);
    List<TransMessage> findTransBySend(int send);
    List<TransMessage> findTransByTrans(int trans);
    List<TransMessage> findTransByDate(Date date);
    List<TransMessage> findTransBySuccess(int isSuccess);
    int addTrans(TransMessage transMessage);
    int deleteTransByTid(int transid);
    int updateTransByTId(@Param("isSuccess") int isSuccess,@Param("transId") int transId);
}
