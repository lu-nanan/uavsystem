package com.hnu.Mapper;

import com.hnu.entity.BlackUser;

import java.util.Date;
import java.util.List;

public interface BlackUserMapper {
    List<BlackUser> findAllBlackUsers();
    List<BlackUser> findBlackByUId(int userid);
    List<BlackUser> findBlackByAdmin(int adminid);
    List<BlackUser> findBlackByDate(Date date);
    int addBlack(BlackUser blackUser);
    int deleteBlackByUId(int usrid);

}
