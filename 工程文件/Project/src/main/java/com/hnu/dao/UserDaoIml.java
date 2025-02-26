package com.hnu.dao;

import com.hnu.Mapper.UserMapper;
import com.hnu.entity.User;
import com.hnu.service.UserService;
import com.hnu.service.sendCode;
import com.hnu.util.SqlSessionUtil;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class UserDaoIml implements UserDao{

    public UserDaoIml() throws IOException {
    }
    /**
     * 根据用户ID查找用户。
     * @param id 用户的ID
     * @return 返回找到的用户对象。
     */
    @Override
    public User findUserById(int id) {
        SqlSession session = SqlSessionUtil.openSession();
        UserMapper userMapper = session.getMapper(UserMapper.class);
        User user = userMapper.findUserById(id);
        session.close();
        return user;
    }

    /**
     * 根据用户邮箱查找用户。
     * @param email 用户的邮箱
     * @return 返回找到的用户对象。
     */
    @Override
    public User findUserByEmail(String email) {
        SqlSession session = SqlSessionUtil.openSession();
        UserMapper userMapper = session.getMapper(UserMapper.class);
        User user = userMapper.findUserByEmail(email);
        session.close();
        return user;
    }

    /**
     * 查找所有用户。
     * @return 返回所有用户对象的列表。
     */
    @Override
    public List<User> findAllUsers() {
        SqlSession session = SqlSessionUtil.openSession();
        UserMapper userMapper = session.getMapper(UserMapper.class);
        List<User> users = userMapper.findAllUsers();
        session.close();
        return users;
    }
    /**
     * 添加一个新的用户。
     * @param user 要添加的用户对象
     */
    @Override
    public int addUser(User user) {
        SqlSession session = SqlSessionUtil.openSession();
        UserMapper userMapper = session.getMapper(UserMapper.class);
        int result = userMapper.addUser(user);
        session.commit();
        session.close();
        return result;
    }
    /**
     * 根据用户ID删除用户。
     * @param id 要删除的用户ID
     */
    @Override
    public int deleteUserById(int id) {
        SqlSession session = SqlSessionUtil.openSession();
        UserMapper userMapper = session.getMapper(UserMapper.class);
        int result = userMapper.deleteUserById(id);
        session.commit();
        session.close();
        return result;
    }
    /**
     * 根据用户ID更新用户密码。
     * @param id 用户的ID
     * @param newPassword 新密码
     * @return 返回更新密码后的用户对象。
     */
    @Override
    public int updatePasswordById(String newPassword, int id) {
        SqlSession session = SqlSessionUtil.openSession();
        UserMapper userMapper = session.getMapper(UserMapper.class);
        int result = userMapper.updatePasswordById(newPassword, id);
        session.commit();
        session.close();
        return result;
    }
    /**
     * 根据用户电话号码查找用户。
     * @param phone 用户的电话号码
     * @return 返回找到的用户对象。
     */
    @Override
    public User findUserByPhone(long phone) {
        SqlSession session = SqlSessionUtil.openSession();
        UserMapper userMapper = session.getMapper(UserMapper.class);
        User user = userMapper.findUserByPhone(phone);
        session.close();
        return user;
    }
    public void close(){

    }
    public List<User> searchUser(String searchInput, String searchOption) {
        sendCode sendCode=new sendCode();
        SqlSession session = SqlSessionUtil.openSession();
        UserMapper userMapper = session.getMapper(UserMapper.class);
        User user=new User().getInstance();
        List<User> users = new ArrayList<>();
        if (searchInput == null || searchOption == null) {
            users.addAll(userMapper.findAllUsers());
        } else if (searchOption.equals("email")) {
            if(!sendCode.checkEmail(searchInput)){
                return users;
            }else {
                users.add(userMapper.findUserByEmail(searchInput));
            }
        } else if (searchOption.equals("telephone")) {
            if(!sendCode.checkPhone(searchInput)){
                return users;
            }else {
                users.add(userMapper.findUserByPhone(Long.valueOf(searchInput)));
            }

        } else if (searchOption.equals("password")) {
            users=userMapper.findUserByPassword(searchInput);
        }
        session.close();
        return users;
    }

    @Override
    public List<User> findUserByPassword(String password) {
        SqlSession session = SqlSessionUtil.openSession();
        UserMapper userMapper = session.getMapper(UserMapper.class);
        List<User> users = userMapper.findUserByPassword(password);
        session.close();
        return users;
    }

    @Override
    public List<User> selectUsersNotInBlacklist() {
        SqlSession session = SqlSessionUtil.openSession();
        UserMapper userMapper = session.getMapper(UserMapper.class);
        List<User> users = userMapper.selectUsersNotInBlacklist();
        session.close();
        return users;
    }

    public int getPages(List<User> users){
        int lengh=users.size();
        if(lengh%8==0){
            return lengh/8;
        }else {
            return lengh/8+1;
        }
    }

}
