package com.hnu.Mapper;
import com.hnu.entity.User;
import org.apache.ibatis.annotations.*;
import java.util.List;

/**
 * User表的Mapper接口
 * 进行数据库操作
 */
@Mapper

public interface UserMapper{
    /**
     * 根据用户ID查找用户。
     * @param id 用户的ID
     * @return 返回匹配的用户对象，如果没有找到返回null。
     */
    User findUserById(int id);
    /**
     * 根据用户电话号码查找用户。
     * @param phone 用户的电话号码
     * @return 返回匹配的用户对象，如果没有找到返回null。
     */
    User findUserByPhone(long phone);
    /**
     * 查找所有用户。
     * @return 返回所有用户对象的列表。
     */
    List<User>findAllUsers();
    /**
     * 添加一个新的用户。
     *
     * @param user 要添加的用户对象
     * @return
     */
    int addUser(User user);
    /**
     * 根据用户ID删除用户。
     * @param id 要删除的用户ID
     */
    int deleteUserById(int id);

    /**
     * 根据用户邮箱查找用户。
     * @param email 用户的邮箱
     * @return 返回匹配的用户对象，如果没有找到返回null。
     */
    User findUserByEmail(String email);
    /**
     * 根据用户ID更新用户密码。
     * @param id 用户的ID
     * @param newPassword 新密码
     * @return 返回更新密码后的用户对象。
     */
    int updatePasswordById(@Param("newPassword") String newPassword,@Param("id") int id);
    List<User> findUserByPassword(String password);
    List<User> selectUsersNotInBlacklist();

}
