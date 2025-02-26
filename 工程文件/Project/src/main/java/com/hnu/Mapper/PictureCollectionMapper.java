package com.hnu.Mapper;

import com.hnu.entity.PictureCollection;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PictureCollectionMapper {
    List<PictureCollection> findAllPicture();
    List<PictureCollection> findPictureByUavId(int uavId);
    List<PictureCollection> findPictureById();

}
