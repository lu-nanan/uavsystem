package com.hnu.service;


import com.hnu.entity.User;

import java.util.List;

public interface UserService{
    /**
     * 根据用户ID查找并返回一个用户对象。
     * @param id 用户的唯一标识符
     * @return 如果找到对应的用户，则返回User对象，否则返回null。
     */
    User findUserById(int id);
    /**
     * 根据用户邮箱查找并返回一个用户对象。
     * @param email
     * @return 如果找到对应的用户，则返回User对象，否则返回null。
     */
    User findUserByEmail(String email);
    /**
     * 查找所有用户
     */
    List<User>findAllUsers();
    /**
     * 添加用户
     */
    void addUser(User user);
    /**
     * 根据用户ID删除用户对象。
     * @param id 用户的唯一标识符
     */
    void deleteUserById(int id);
    /**
     * 根据用户ID查找更新用户密码。
     * @param id 用户的唯一标识符
     * @param newPassword
     */
    int updatePasswordById(int id, String newPassword);
    /**
     * 根据用户手机号查找并返回一个用户对象。
     * @param phone
     */
    User findUserByPhone(long phone);


}


