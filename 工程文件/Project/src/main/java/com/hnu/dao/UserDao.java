package com.hnu.dao;


import com.hnu.entity.User;

import java.util.List;
/**
 * UserDao接口定义了与用户相关的数据访问对象（DAO）操作。
 * 这些操作允许从数据库中增删查改用户信息。
 */

public interface UserDao {
    /**
     * 根据用户ID查找并返回一个用户对象。
     * @param id 用户的唯一标识符
     * @return 如果找到对应的用户，则返回User对象，否则返回null。
     */
    User findUserById(int id);
    /**
     * 根据用户邮箱查找并返回一个用户对象。
     * @param email 用户的邮箱地址
     * @return 如果找到对应的用户，则返回User对象，否则返回null。
     */
    User findUserByEmail(String email);
    /**
     * 根据用户电话号码查找并返回一个用户对象。
     * @param phone 用户的电话号码
     * @return 如果找到对应的用户，则返回User对象，否则返回null。
     */
    User findUserByPhone(long phone);
    /**
     * 查找并返回所有用户信息的列表。
     * @return 包含所有用户信息的List<User>对象。
     */
    List<User>findAllUsers();
    /**
     * 向数据库添加一个新的用户对象。
     * @param user 要添加的用户对象
     */
    int addUser(User user);
    /**
     * 根据用户ID从数据库中删除一个用户对象。
     * @param id 要删除的用户的唯一标识符
     */
    int deleteUserById(int id);
    /**
     * 根据用户ID更新用户的密码。
     * @param id 用户的唯一标识符
     * @param newPassword 用户的新密码
     * @return 更新密码后的用户对象
     */
    int updatePasswordById(String newPassword,int id);
    List<User> findUserByPassword(String password);
    List<User> selectUsersNotInBlacklist();

}


