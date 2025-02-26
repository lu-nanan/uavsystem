package com.hnu.service;

import com.hnu.Mapper.TransMassageMapper;
import com.hnu.entity.TransMessage;
import com.hnu.util.SqlSessionUtil;
import org.apache.ibatis.session.SqlSession;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TransService {
    public List<TransMessage> findTAllTrans() {
        SqlSession session = SqlSessionUtil.openSession();
        TransMassageMapper transMassageMapper = session.getMapper(TransMassageMapper.class);
        List<TransMessage> transMessages = transMassageMapper.findTAllTrans();
        session.close();
        return transMessages;
    }

    public List<TransMessage> findTransByTId(int transId) {
        SqlSession session = SqlSessionUtil.openSession();
        TransMassageMapper transMassageMapper = session.getMapper(TransMassageMapper.class);
        List<TransMessage> transMessages = transMassageMapper.findTransByTId(transId);
        session.close();
        return transMessages;
    }

    public List<TransMessage> findTransBySend(int send) {
        SqlSession session = SqlSessionUtil.openSession();
        TransMassageMapper transMassageMapper = session.getMapper(TransMassageMapper.class);
        List<TransMessage> transMessages = transMassageMapper.findTransBySend(send);
        session.close();
        return transMessages;
    }

    public List<TransMessage> findTransByTrans(int trans) {
        SqlSession session = SqlSessionUtil.openSession();
        TransMassageMapper transMassageMapper = session.getMapper(TransMassageMapper.class);
        List<TransMessage> transMessages = transMassageMapper.findTransByTrans(trans);
        session.close();
        return transMessages;
    }

    public List<TransMessage> findTransByDate(Date date) {
        SqlSession session = SqlSessionUtil.openSession();
        TransMassageMapper transMassageMapper = session.getMapper(TransMassageMapper.class);
        List<TransMessage> transMessages = transMassageMapper.findTransByDate(date);
        session.close();
        return transMessages;
    }

    public List<TransMessage> findTransBySuccess(int isSuccess) {
        SqlSession session = SqlSessionUtil.openSession();
        TransMassageMapper transMassageMapper = session.getMapper(TransMassageMapper.class);
        List<TransMessage> transMessages = transMassageMapper.findTransBySuccess(isSuccess);
        session.close();
        return transMessages;
    }

    public int addTrans(TransMessage transMessage) {
        SqlSession session = SqlSessionUtil.openSession();
        TransMassageMapper transMassageMapper = session.getMapper(TransMassageMapper.class);
        int result = transMassageMapper.addTrans(transMessage);
        session.commit();
        session.close();
        return result;
    }

    public int deleteTransByTid(int transid) {
        SqlSession session = SqlSessionUtil.openSession();
        TransMassageMapper transMassageMapper = session.getMapper(TransMassageMapper.class);
        int result = transMassageMapper.deleteTransByTid(transid);
        session.commit();
        session.close();
        return result;
    }

    public int updateTransByTId(int isSuccess, int transId) {
        SqlSession session = SqlSessionUtil.openSession();
        TransMassageMapper transMassageMapper = session.getMapper(TransMassageMapper.class);
        int result = transMassageMapper.updateTransByTId(isSuccess, transId);
        session.commit();
        session.close();
        return result;
    }

    public void close() {
        // No operation needed as each method closes the session individually
    }

    public int getPage(List<TransMessage> transMessages) {
        int i = transMessages.size();
        if (i % 8 == 0) {
            return i / 8;
        } else {
            return i / 8 + 1;
        }
    }

    public List<TransMessage> searchTrans(String searchOption, String searchInput) {
        SqlSession session = SqlSessionUtil.openSession();
        TransMassageMapper transMassageMapper = session.getMapper(TransMassageMapper.class);
        List<TransMessage> transMessages = new ArrayList<>();
        if (searchInput == null) {
            transMessages = transMassageMapper.findTAllTrans();
        } else {
            if (searchOption.equals("send")) {
                transMessages = transMassageMapper.findTransBySend(Integer.valueOf(searchInput));
            } else if (searchOption.equals("trans")) {
                transMessages = transMassageMapper.findTransByTrans(Integer.valueOf(searchInput));
            } else if (searchOption.equals("isSuccess")) {
                transMessages = transMassageMapper.findTransBySuccess(Integer.valueOf(searchInput));
            } else {
                transMessages = transMassageMapper.findTAllTrans();
            }
        }
        session.close();
        return transMessages;
    }
}