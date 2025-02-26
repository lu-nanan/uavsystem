package com.hnu.service;

import com.hnu.Mapper.HistoryMapper;
import com.hnu.dao.UserDaoIml;
import com.hnu.entity.History;
import com.hnu.entity.User;
import com.hnu.util.SqlSessionUtil;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.SqlSession;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserHistoryService {
    private SqlSession session = SqlSessionUtil.openSession();
    private HistoryMapper historyMapper=session.getMapper(HistoryMapper.class);
    public List<History> findAllUserHistory(){
        SqlSession session = SqlSessionUtil.openSession();
        HistoryMapper historyMapper=session.getMapper(HistoryMapper.class);
        List<History> histories=historyMapper.findAllUserHistory();
        session.close();
        return histories;
    }
    public List<History> findHistoryByUId(int id){
        SqlSession session = SqlSessionUtil.openSession();
        HistoryMapper historyMapper=session.getMapper(HistoryMapper.class);
        List<History> histories=historyMapper.findHistoryByUId(id);
        session.close();
        return histories;
    }
    public History findHistoryByHId(int hisid){
        SqlSession session = SqlSessionUtil.openSession();
        HistoryMapper historyMapper=session.getMapper(HistoryMapper.class);
        History history=historyMapper.findHistoryByHId(hisid);
        session.close();
        return history;
    }
    public History findHistoryByHUId(@Param("hisid") int hisid, @Param("userid" ) int userid){
        SqlSession session = SqlSessionUtil.openSession();
        HistoryMapper historyMapper=session.getMapper(HistoryMapper.class);
        History history=historyMapper.findHistoryByHUId(hisid,userid);
        session.close();
        return history;

    }
    public List<History> findHistoryByDate(Date date){
        SqlSession session = SqlSessionUtil.openSession();
        HistoryMapper historyMapper=session.getMapper(HistoryMapper.class);
        List<History> histories=historyMapper.findHistoryByDate(date);
        session.close();
        return histories;
    }
    public List<History> findHistoryByDateId(@Param("time") Date date,@Param("userid") int userid){
        SqlSession session = SqlSessionUtil.openSession();
        HistoryMapper historyMapper=session.getMapper(HistoryMapper.class);
        List<History> histories=historyMapper.findHistoryByDateId(date,userid);
        session.close();
        return histories;
    }
    public List<History> findHistoryByContentID(@Param("content") String content,@Param("userid") int userid){
        SqlSession session = SqlSessionUtil.openSession();
        HistoryMapper historyMapper=session.getMapper(HistoryMapper.class);
        List<History> histories= historyMapper.findHistoryByContentID(content,userid);
        session.close();
        return histories;
    }
    public List<History> findHistoryByContent(String content){
        SqlSession session = SqlSessionUtil.openSession();
        HistoryMapper historyMapper=session.getMapper(HistoryMapper.class);
        List<History> histories= historyMapper.findHistoryByContent(content);
        session.close();
        return histories;
    }
    public int addHistory(History history){
        SqlSession session = SqlSessionUtil.openSession();
        HistoryMapper historyMapper=session.getMapper(HistoryMapper.class);
        int i=historyMapper.addHistory(history);
        session.commit();
        session.close();
        return i;
    }
    public int deleteUserByUId(int userid){
        SqlSession session = SqlSessionUtil.openSession();
        HistoryMapper historyMapper=session.getMapper(HistoryMapper.class);
        int i=historyMapper.deleteUserByUId(userid);
        session.commit();
        session.close();
        return i;
    }
    public int deleteUserByHId(int hisid){
        SqlSession session = SqlSessionUtil.openSession();
        HistoryMapper historyMapper=session.getMapper(HistoryMapper.class);
        int i=historyMapper.deleteUserByHId(hisid);
        session.commit();
        session.close();
        return i;
    }
    public List<History> getHistory(int id) throws IOException {
        SqlSession session = SqlSessionUtil.openSession();
        HistoryMapper historyMapper=session.getMapper(HistoryMapper.class);
        UserDaoIml userDaoIml=new UserDaoIml();
        User user =userDaoIml.findUserById(id);
        if(user.getType()==0){
            List<History> histories= historyMapper.findAllUserHistory();
            session.close();
            return histories;
        }else if (user.getType()==1){
            List<History> histories= historyMapper.findHistoryByUId(id);
            session.close();
            return histories;
        }else {
            return null;
        }
    }
    public void close(){

    }
    public List<History> searchHistory(int id,String searchInput,String searchOption ) throws IOException, ParseException {
        SqlSession session = SqlSessionUtil.openSession();
        HistoryMapper historyMapper=session.getMapper(HistoryMapper.class);
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List <History> histories=new ArrayList<>();
        UserDaoIml userDaoIml=new UserDaoIml();
        User user=userDaoIml.findUserById(id);
        if (searchInput==null){
            histories=getHistory(id);
        }
        if(user.getType()==1){
            if(searchOption.equals("ID")){
                histories.add(historyMapper.findHistoryByHUId(Integer.valueOf(searchInput),id));
            }else if(searchOption.equals("date")){
                histories=historyMapper.findHistoryByDateId(simpleDateFormat.parse(searchInput),id);
            } else if (searchOption.equals("content")) {
                histories=historyMapper.findHistoryByContentID(searchInput,id);
            }else {
                session.close();
                return null;
            }
        }else if(user.getType()==0){
            if(searchOption.equals("ID")){
                histories.add(findHistoryByHId(Integer.valueOf(searchInput)));
            }else if(searchOption.equals("date")){
                histories=historyMapper.findHistoryByDate(simpleDateFormat.parse(searchInput));
            } else if (searchOption.equals("content")) {
                histories=historyMapper.findHistoryByContent(searchInput);
            }else {
                session.close();
                return null;
            }
        }else {
            session.close();
            return null;
        }
        session.close();
        return histories;
    }
    public int getPages(List<History> histories){
        int lengh=histories.size();
        if(lengh%8==0){
            return lengh/8;
        }else {
            return lengh/8+1;
        }
    }
    public int add(String content,int userId){
        SqlSession session = SqlSessionUtil.openSession();
        HistoryMapper historyMapper=session.getMapper(HistoryMapper.class);
        History history=new History();
        history.setContent(content);
        history.setTime(new Date());
        history.setUserid(userId);
        int i=historyMapper.addHistory(history);
        session.commit();
        session.close();
        return i;
    }

}
