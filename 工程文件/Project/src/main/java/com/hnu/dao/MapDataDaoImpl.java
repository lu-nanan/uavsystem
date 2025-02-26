package com.hnu.dao;

import com.hnu.Mapper.DeviceInfoMapper;
import com.hnu.entity.DeviceInfo;
import com.hnu.entity.MapData;
import com.hnu.Mapper.MapDataMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.apache.ibatis.io.Resources.getResourceAsStream;

public class MapDataDaoImpl implements MapDataDao {

    private String resource = "mybatis-config.xml";
    private InputStream inputStream = getResourceAsStream(resource);
    private SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
    private SqlSession session = sqlSessionFactory.openSession();
    private MapDataMapper mapDataMapper = session.getMapper(MapDataMapper.class);

    public MapDataDaoImpl() throws IOException {
    }

    //    public MapDataDaoImpl() throws IOException {
//        String resource = "mybatis-config.xml";
//        sqlSessionFactory = new SqlSessionFactoryBuilder().build(getClass().getClassLoader().getResourceAsStream(resource));
//        session = sqlSessionFactory.openSession();
//        mapDataMapper = session.getMapper(MapDataMapper.class);
//    }

    @Override
    public List<MapData> findAllMapData() {
        List<MapData> mapDataList = mapDataMapper.findAllMapData();
        return mapDataList;
    }
}
