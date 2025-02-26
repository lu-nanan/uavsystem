package com.hnu.dao;

import com.hnu.Mapper.FeedBackMapper;
import com.hnu.entity.FeedBack;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

public class FeedBackDaoIml implements FeedBackDao{
    private String resource = "mybatis-config.xml";// MyBatis配置文件的路径
    private InputStream inputStream = Resources.getResourceAsStream(resource);// 读取配置文件的输入流
    private SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);// 创建SqlSessionFactory对象
    private SqlSession session = sqlSessionFactory.openSession();// 创建SqlSession对象
    private FeedBackMapper feedBackMapper=session.getMapper(FeedBackMapper.class);

    public FeedBackDaoIml() throws IOException {
    }

    @Override
    public List<FeedBack> findAllFeed() {
        List<FeedBack>feedBacks=feedBackMapper.findAllFeed();
        return feedBacks;
    }

    @Override
    public List<FeedBack> findFeedById(int userid) {
        List<FeedBack>feedBacks=feedBackMapper.findFeedById(userid);
        return feedBacks;
    }

    @Override
    public FeedBack findUserByFId(int feedid) {
        return feedBackMapper.findUserByFId(feedid);
    }

    @Override
    public List<FeedBack> findUserByDate(Date date) {
        List<FeedBack>feedBacks=feedBackMapper.findUserByDate(date);
        return feedBacks;
    }

    @Override
    public int addFeed(FeedBack feedBack) {
        return feedBackMapper.addFeed(feedBack);
    }

    @Override
    public void deleteUserById(int userid) {
        feedBackMapper.deleteUserById(userid);
    }

    @Override
    public void deleteUserByFId(int feedid) {
        feedBackMapper.deleteUserByFId(feedid);
    }

    @Override
    public void updateFeedByFId(int feedid,boolean ishandle) {
        return;
    }
}
