package com.hnu.service;

import com.hnu.entity.User;

/**
 * 获取登录用户用户信息
 */
public class Loginservice {
    private UserServiceIml userServiceIml;//用户服务类
    public User getUser(String type, String value){
        User user=new User();//创建了一个新的User对象实例。
        if(type.equals("ID")){
            int id= Integer.parseInt(value);
            user=userServiceIml.findUserById(id);//如果type参数等于"ID"，那么尝试将value参数转换为整数，并使用这个ID调用userServiceIml的findUserById方法来查找对应的用户。
        }else if(type.equals("PHONE")){
            long phone= Long.parseLong(value);
            user=userServiceIml.findUserByPhone(phone);//如果type参数等于"PHONE"，那么尝试将value参数转换为长整数，并使用这个电话号码调用userServiceIml的findUserByPhone方法来查找对应的用户。

        }
        return user;
    }
}
