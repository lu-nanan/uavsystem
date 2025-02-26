package com.hnu.Mapper;

import com.hnu.entity.DataCollection;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;
@Mapper
public interface DataCollectionMapper {
    List<DataCollection> findAllData();
    DataCollection findDataById(int dataId);
    List<DataCollection> findDataByUavId(int uavId);
    List<DataCollection> findDataByTime(Date returnTime);
    int addData(DataCollection dataCollection);
    int deleteDataById(int id);
}
