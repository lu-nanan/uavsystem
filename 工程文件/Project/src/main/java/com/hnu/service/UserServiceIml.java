package com.hnu.service;

import com.hnu.dao.UserDaoIml;
import com.hnu.entity.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.util.List;

public class UserServiceIml implements UserService{

    private UserDaoIml userDaoIml=new UserDaoIml();

    public UserServiceIml() throws IOException {
    }

    @Override
    public User findUserById(int id) {
        User user=userDaoIml.findUserById(id);
        return user;
    }

    @Override
    public User findUserByEmail(String email) {
        User user=userDaoIml.findUserByEmail(email);
        return user;
    }

    @Override
    public List<User> findAllUsers() {
        List<User> users=userDaoIml.findAllUsers();
        return users;
    }

    @Override
    public void addUser(User user) {
        userDaoIml.addUser(user);
    }

    @Override
    public void deleteUserById(int id) {
        userDaoIml.deleteUserById(id);

    }

    @Override
    public int updatePasswordById(int id,String newPassword) {
        int i=userDaoIml.updatePasswordById(newPassword,id);
        return i;
    }

    @Override
    public User findUserByPhone(long phone) {
        User user=userDaoIml.findUserByPhone(phone);
        return user;
    }
}
