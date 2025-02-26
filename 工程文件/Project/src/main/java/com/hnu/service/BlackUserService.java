package com.hnu.service;

import com.hnu.Mapper.BlackUserMapper;
import com.hnu.entity.BlackUser;
import com.hnu.entity.User;
import com.hnu.util.SqlSessionUtil;
import org.apache.ibatis.session.SqlSession;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/*
public class BlackUserService {
    private SqlSession session = SqlSessionUtil.openSession();
    private BlackUserMapper blackUserMapper =session.getMapper(BlackUserMapper.class);
    public List<BlackUser> findAllBlackUsers(){
        return blackUserMapper.findAllBlackUsers();
    }
    public List<BlackUser> findBlackByUId(int userid){
        return blackUserMapper.findBlackByUId(userid);
    }
    public List<BlackUser> findBlackByAdmin(int adminid){
        return blackUserMapper.findBlackByAdmin(adminid);
    }
    public List<BlackUser> findBlackByDate(Date date){
        return blackUserMapper.findBlackByDate(date);
    }
    public int addBlack(BlackUser blackUser){
        int i= blackUserMapper.addBlack(blackUser);
        session.commit();
        return i;
    }
    public int deleteBlackByUId(int usrid){
        int i= blackUserMapper.deleteBlackByUId(usrid);
        session.commit();
        return i;
    }
    public void close(){
        session.commit();
    }
    public List<BlackUser> searchBlack(String searchInput,String searchOption) throws ParseException {
        List<BlackUser> blackUsers=new ArrayList<>();
        if(searchInput==null){
            return blackUserMapper.findAllBlackUsers();
        }
        if(searchOption.equals("userID")){
            blackUsers= blackUserMapper.findBlackByUId(Integer.valueOf(searchInput));
        }else if(searchOption.equals("adminid")){
            blackUsers= blackUserMapper.findBlackByAdmin(Integer.valueOf(searchInput));
        }else if (searchOption.equals("time")){
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date=simpleDateFormat.parse(searchInput);
            blackUsers= blackUserMapper.findBlackByDate(date);
        }
        return blackUsers;
    }
    public int getPages(List<BlackUser> blackUsers){
        int lengh=blackUsers.size();
        if(lengh%8==0){
            return lengh/8;
        }else {
            return lengh/8+1;
        }
    }
}

 */


import com.hnu.Mapper.BlackUserMapper;
import com.hnu.entity.BlackUser;
import com.hnu.entity.User;
import com.hnu.util.SqlSessionUtil;
import org.apache.ibatis.session.SqlSession;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BlackUserService {
    public List<BlackUser> findAllBlackUsers(){
        SqlSession session = SqlSessionUtil.openSession();
        BlackUserMapper blackUserMapper =session.getMapper(BlackUserMapper.class);
        List<BlackUser> blackUsers=blackUserMapper.findAllBlackUsers();
        session.close();
        return blackUsers;
    }
    public List<BlackUser> findBlackByUId(int userid){
        SqlSession session = SqlSessionUtil.openSession();
        BlackUserMapper blackUserMapper =session.getMapper(BlackUserMapper.class);
        List<BlackUser> blackUsers=blackUserMapper.findBlackByUId(userid);
        session.close();
        return blackUsers;
    }
    public List<BlackUser> findBlackByAdmin(int adminid){
        SqlSession session = SqlSessionUtil.openSession();
        BlackUserMapper blackUserMapper =session.getMapper(BlackUserMapper.class);
        List<BlackUser> blackUsers=blackUserMapper.findBlackByAdmin(adminid);
        session.close();
        return blackUsers;
    }
    public List<BlackUser> findBlackByDate(Date date){
        SqlSession session = SqlSessionUtil.openSession();
        BlackUserMapper blackUserMapper =session.getMapper(BlackUserMapper.class);
        List<BlackUser> blackUsers=blackUserMapper.findBlackByDate(date);
        session.close();
        return blackUsers;
    }
    public int addBlack(BlackUser blackUser){
        SqlSession session = SqlSessionUtil.openSession();
        BlackUserMapper blackUserMapper =session.getMapper(BlackUserMapper.class);
        int i= blackUserMapper.addBlack(blackUser);
        session.commit();
        session.close();
        return i;
    }
    public int deleteBlackByUId(int usrid){
        SqlSession session = SqlSessionUtil.openSession();
        BlackUserMapper blackUserMapper =session.getMapper(BlackUserMapper.class);
        int i= blackUserMapper.deleteBlackByUId(usrid);
        session.commit();
        session.close();
        return i;
    }
    public void close(){

    }
    public List<BlackUser> searchBlack(String searchInput,String searchOption) throws ParseException {
        List<BlackUser> blackUsers=new ArrayList<>();
        SqlSession session = SqlSessionUtil.openSession();
        BlackUserMapper blackUserMapper =session.getMapper(BlackUserMapper.class);
        if(searchInput==null){
            blackUsers=blackUserMapper.findAllBlackUsers();
            session.close();
            return blackUsers;
        }
        if(searchOption.equals("userID")){
            blackUsers= blackUserMapper.findBlackByUId(Integer.valueOf(searchInput));
        }else if(searchOption.equals("adminid")){
            blackUsers= blackUserMapper.findBlackByAdmin(Integer.valueOf(searchInput));
        }else if (searchOption.equals("time")){
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date=simpleDateFormat.parse(searchInput);
            blackUsers= blackUserMapper.findBlackByDate(date);
        }
        session.close();
        return blackUsers;
    }
    public int getPages(List<BlackUser> blackUsers){
        int lengh=blackUsers.size();
        if(lengh%8==0){
            return lengh/8;
        }else {
            return lengh/8+1;
        }
    }
}


