package com.hnu.Mapper;

import com.hnu.entity.FeedBack;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;

@Mapper
public interface FeedBackMapper {
    List<FeedBack> findAllFeed();
    List<FeedBack> findFeedById(int userid);
    FeedBack findUserByFId(int feedid);
    List<FeedBack> findUserByDate(Date date);
    int addFeed(FeedBack feedBack);
    int deleteUserById(int userid);
    int deleteUserByFId(int feedid);
    int updateFeedByFId(@Param("ishandle") boolean ishandle,@Param("feedid") int feedid);
    FeedBack findUserByDateId(@Param("time") Date time, @Param("userid") int userid);
    List<FeedBack> findUserByisHandle(Boolean ishandle);
}
