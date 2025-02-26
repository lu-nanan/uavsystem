package com.hnu.service;

import com.hnu.Mapper.UserMapper;
import com.hnu.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class serviceIml implements service {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User selectById(int id) {
        return userMapper.findUserById(id);
    }
}
