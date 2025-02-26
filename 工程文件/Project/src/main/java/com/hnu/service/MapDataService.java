package com.hnu.service;

import com.hnu.Mapper.MapDataMapper;
import com.hnu.entity.MapData;
import com.hnu.util.SqlSessionUtil;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class MapDataService {

    public List<MapData> getAllMapData() {
        SqlSession session = SqlSessionUtil.openSession();
        MapDataMapper mapDataMapper = session.getMapper(MapDataMapper.class);
        List<MapData> mapData=mapDataMapper.findAllMapData();
        session.close();
        return mapData;
    }
    public void close(){

    }
}
