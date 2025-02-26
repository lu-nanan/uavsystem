package com.hnu.Mapper;


import com.hnu.entity.UavPathInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UavPathInfoMapper {
    // 根据路径id查询路径信息
    UavPathInfo selectByPathId(Integer pathId);

    // 查询所有路径信息
    List<UavPathInfo> selectAllPathInfo();

    // 根据无人机id查询路径信息
    List<UavPathInfo> selectByUavId(Integer uavId);

    // 插入一条路径信息
    int insertUavPathInfo(UavPathInfo uavPathInfo);

    // 根据路径id更新路径信息
    int updateUavPathInfoById(UavPathInfo uavPathInfo);

    // 根据路径id删除路径信息
    int deleteUavPathInfoById(Integer pathId);
}
