package com.hnu.service;

import com.hnu.Mapper.FeedBackMapper;
import com.hnu.entity.FeedBack;
import com.hnu.entity.User;
import com.hnu.util.SqlSessionUtil;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.SqlSession;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FeedBackService {

    public List<FeedBack> findAllFeed(){
        SqlSession session = SqlSessionUtil.openSession();
        FeedBackMapper feedBackMapper=session.getMapper(FeedBackMapper.class);
        List<FeedBack> feedBacks=feedBackMapper.findAllFeed();
        session.close();
        return feedBacks;

    }
    public List<FeedBack> findFeedById(int userid){
        SqlSession session = SqlSessionUtil.openSession();
        FeedBackMapper feedBackMapper=session.getMapper(FeedBackMapper.class);
        List<FeedBack> feedBacks=feedBackMapper.findFeedById(userid);
        session.close();
        return feedBacks;

    }
    public FeedBack findUserByFId(int feedid){
        SqlSession session = SqlSessionUtil.openSession();
        FeedBackMapper feedBackMapper=session.getMapper(FeedBackMapper.class);

        FeedBack feedBacks=feedBackMapper.findUserByFId(feedid);
        session.close();
        return feedBacks;
    }
    public List<FeedBack> findUserByDate(Date date) {
        SqlSession session = SqlSessionUtil.openSession();
        FeedBackMapper feedBackMapper = session.getMapper(FeedBackMapper.class);
        List<FeedBack> feedBacks = feedBackMapper.findUserByDate(date);
        session.close();
        return feedBacks;
    }
    public int addFeed(FeedBack feedBack){
        SqlSession session = SqlSessionUtil.openSession();
        FeedBackMapper feedBackMapper=session.getMapper(FeedBackMapper.class);

        int i=feedBackMapper.addFeed(feedBack);
        session.commit();
        session.close();
        return i;
    }
    public int deleteUserById(int userid){
        SqlSession session = SqlSessionUtil.openSession();
        FeedBackMapper feedBackMapper=session.getMapper(FeedBackMapper.class);
        int i=feedBackMapper.deleteUserById(userid);
        session.commit();
        session.close();
        return i;
    }
    public int deleteUserByFId(int feedid){
        SqlSession session = SqlSessionUtil.openSession();
        FeedBackMapper feedBackMapper=session.getMapper(FeedBackMapper.class);
        int i=feedBackMapper.deleteUserByFId(feedid);
        session.commit();
        session.close();
        return i;
    }
    public int updateFeedByFId(boolean ishandle,int feedid){
        SqlSession session = SqlSessionUtil.openSession();
        FeedBackMapper feedBackMapper=session.getMapper(FeedBackMapper.class);
        int i=feedBackMapper.updateFeedByFId(ishandle,feedid);
        session.commit();
        session.close();
        return i;

    }
    public FeedBack getUserFeedbacks(String userID, String searchKeyword, String searchBy) {
        SqlSession session = SqlSessionUtil.openSession();
        FeedBackMapper feedBackMapper=session.getMapper(FeedBackMapper.class);
        if(searchBy.equals("ID")){
            System.out.println(searchBy);
            FeedBack feedBack=feedBackMapper.findUserByFId(Integer.valueOf(searchKeyword));
            session.close();
            return feedBack;
        } else if (searchBy.equals("date")) {
            System.out.println(searchBy);
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            try {
                date = simpleDateFormat.parse(searchKeyword);
            } catch (ParseException e) {
                System.out.println("error");
                throw new RuntimeException(e);
            }
            FeedBack feedBack=feedBackMapper.findUserByDateId(date, Integer.valueOf(userID));
            session.close();
            return feedBack;
        }
        else {
            return null;
        }

    }
    public FeedBack findUserByDateId(@Param("time") Date time, @Param("id") int id){
        SqlSession session = SqlSessionUtil.openSession();
        FeedBackMapper feedBackMapper=session.getMapper(FeedBackMapper.class);
        FeedBack feedBack=feedBackMapper.findUserByDateId(time,id);
        session.close();
        return feedBack;
    }
    public List<FeedBack> adminSearch(String searchKeyword, String searchBy) throws ParseException {
        SqlSession session = SqlSessionUtil.openSession();
        FeedBackMapper feedBackMapper=session.getMapper(FeedBackMapper.class);
        List<FeedBack> feedBacks=new ArrayList<>();
        if(searchKeyword==null){
            feedBacks=feedBackMapper.findAllFeed();
            session.close();
            return feedBacks;
        }
        if(searchBy.equals("id")){
            feedBacks.add(feedBackMapper.findUserByFId(Integer.valueOf(searchKeyword)));
            session.close();
        } else if (searchBy.equals("date")) {
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = simpleDateFormat.parse(searchKeyword);
            feedBacks=feedBackMapper.findUserByDate(date);
            session.close();
        }else if(searchBy.equals("userId")){
            feedBacks=feedBackMapper.findFeedById(Integer.valueOf(searchKeyword));
            session.close();
        } else if (searchBy.equals("isRead")) {
            if(searchKeyword.equals("已处理")){
                feedBacks=feedBackMapper.findUserByisHandle(true);
                session.close();
            }else {
                feedBacks=feedBackMapper.findUserByisHandle(false);
                session.close();
            }
        }
        return feedBacks;
    }
    public List<FeedBack> findUserByisHandle(Boolean ishandle) {
        SqlSession session = SqlSessionUtil.openSession();
        FeedBackMapper feedBackMapper=session.getMapper(FeedBackMapper.class);
        List<FeedBack> feedBacks= feedBackMapper.findUserByisHandle(ishandle);
        session.close();
        return feedBacks;
    }
    public int getPages(List<FeedBack> feedBacks){
        int lengh=feedBacks.size();
        if(lengh%8==0){
            return lengh/8;
        }else {
            return lengh/8+1;
        }
    }
    public void close(){

    }

}
