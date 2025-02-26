package com.hnu.service;

import com.hnu.Mapper.DataCollectionMapper;
import com.hnu.entity.DataCollection;
import com.hnu.util.SqlSessionUtil;
import org.apache.ibatis.session.SqlSession;

import java.util.Date;
import java.util.List;

public class DataCollectionService {
    //private SqlSession session = SqlSessionUtil.openSession();
    //private DataCollectionMapper dataCollectionMapper = session.getMapper(DataCollectionMapper.class);
    public List<DataCollection> findAllData() {
        SqlSession session = SqlSessionUtil.openSession();
        DataCollectionMapper dataCollectionMapper = session.getMapper(DataCollectionMapper.class);
        List<DataCollection> dataCollections= dataCollectionMapper.findAllData();
        return dataCollections;
    }
    public  List<DataCollection> findDataByUavId(int uavId) {
        SqlSession session = SqlSessionUtil.openSession();
        DataCollectionMapper dataCollectionMapper = session.getMapper(DataCollectionMapper.class);
        List<DataCollection> dataCollections=dataCollectionMapper.findDataByUavId(uavId);
        return dataCollections;
    }
    public List<DataCollection> findDataByTime(Date time) {
        SqlSession session = SqlSessionUtil.openSession();
        DataCollectionMapper dataCollectionMapper = session.getMapper(DataCollectionMapper.class);
        List<DataCollection> dataCollections=dataCollectionMapper.findDataByTime(time);
        return dataCollections ;
    }
    public int addData(DataCollection dataCollection) {
        SqlSession session = SqlSessionUtil.openSession();
        DataCollectionMapper dataCollectionMapper = session.getMapper(DataCollectionMapper.class);
        int i = dataCollectionMapper.addData(dataCollection);
        session.commit();
        session.close();
        return i;
    }
    public int deleteDataById(int id) {
        SqlSession session = SqlSessionUtil.openSession();
        DataCollectionMapper dataCollectionMapper = session.getMapper(DataCollectionMapper.class);
        int i = dataCollectionMapper.deleteDataById(id);
        session.commit();
        session.close();
        return i;
    }
    public int getPages(List<DataCollection> dataCollections){
        int lengh=dataCollections.size();
        if(lengh%8==0){
            return lengh/8;
        }else {
            return lengh/8+1;
        }
    }
    public void close(){

    }
}
