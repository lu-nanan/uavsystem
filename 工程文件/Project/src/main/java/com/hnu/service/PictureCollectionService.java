package com.hnu.service;

import com.hnu.Mapper.PictureCollectionMapper;
import com.hnu.entity.PictureCollection;
import com.hnu.entity.User;
import com.hnu.util.SqlSessionUtil;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class PictureCollectionService {
    public List<PictureCollection> findAllPicture() {
        SqlSession session = SqlSessionUtil.openSession();
        PictureCollectionMapper pictureCollectionMapper = session.getMapper(PictureCollectionMapper.class);
        List<PictureCollection> pictures = pictureCollectionMapper.findAllPicture();
        session.close();
        return pictures;
    }

    public List<PictureCollection> findPictureByUavId(int uavId) {
        SqlSession session = SqlSessionUtil.openSession();
        PictureCollectionMapper pictureCollectionMapper = session.getMapper(PictureCollectionMapper.class);
        List<PictureCollection> pictures = pictureCollectionMapper.findPictureByUavId(uavId);
        session.close();
        return pictures;
    }

    public List<PictureCollection> findPictureByTime() {
        SqlSession session = SqlSessionUtil.openSession();
        PictureCollectionMapper pictureCollectionMapper = session.getMapper(PictureCollectionMapper.class);
        List<PictureCollection> pictures = pictureCollectionMapper.findPictureById();
        session.close();
        return pictures;
    }
    public void close(){

    }
}