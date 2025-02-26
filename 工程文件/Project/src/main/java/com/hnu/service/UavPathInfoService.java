package com.hnu.service;

import com.hnu.Mapper.UavPathInfoMapper;
import com.hnu.entity.DataCollection;
import com.hnu.entity.UavPathInfo;
import com.hnu.util.SqlSessionUtil;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class UavPathInfoService {
    public List<UavPathInfo> getAllUavPathInfo() {
        SqlSession session = SqlSessionUtil.openSession();
        UavPathInfoMapper uavPathInfoMapper = session.getMapper(UavPathInfoMapper.class);
        List<UavPathInfo> uavPathInfos = uavPathInfoMapper.selectAllPathInfo();
        session.close();
        return uavPathInfos;
    }

    public List<UavPathInfo> getUavPathInfoByUavId(int uavId) {
        SqlSession session = SqlSessionUtil.openSession();
        UavPathInfoMapper uavPathInfoMapper = session.getMapper(UavPathInfoMapper.class);
        List<UavPathInfo> uavPathInfos = uavPathInfoMapper.selectByUavId(uavId);
        session.close();
        return uavPathInfos;
    }

    public UavPathInfo getUavPathInfoByPathId(int pathId) {
        SqlSession session = SqlSessionUtil.openSession();
        UavPathInfoMapper uavPathInfoMapper = session.getMapper(UavPathInfoMapper.class);
        UavPathInfo uavPathInfo = uavPathInfoMapper.selectByPathId(pathId);
        session.close();
        return uavPathInfo;
    }

    public int getPages(List<UavPathInfo> dataCollections){
        int lengh=dataCollections.size();
        if(lengh%8==0){
            return lengh/8;
        }else {
            return lengh/8+1;
        }
    }

    public void close() {
        // No operation needed as each method closes the session individually
    }
}