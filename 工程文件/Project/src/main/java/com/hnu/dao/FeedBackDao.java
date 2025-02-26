package com.hnu.dao;

import com.hnu.entity.FeedBack;

import java.util.Date;
import java.util.List;

public interface FeedBackDao {
    List<FeedBack> findAllFeed();
    List<FeedBack> findFeedById(int userid);
    FeedBack findUserByFId(int feedid);
    List<FeedBack> findUserByDate(Date date);
    int addFeed(FeedBack feedBack);
    void deleteUserById(int userid);
    void deleteUserByFId(int feedid);
    void updateFeedByFId(int feedid,boolean ishandle);
}
