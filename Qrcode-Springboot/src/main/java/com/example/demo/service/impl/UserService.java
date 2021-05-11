package com.example.demo.service.impl;

import com.example.demo.common.ResponseCode;
import com.example.demo.common.ServerResponse;
import com.example.demo.dao.UserMapper;
import com.example.demo.pojo.User;
import com.example.demo.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public ServerResponse loginLogic(String username, String password) {
        //登录的业务逻辑
        //1.用户名和密码的非空判断
        if(username == null || username.equals("")){
            return ServerResponse.createServerResponseByFail(ResponseCode.USERNAME_NOT_EMPTY.getCode(),ResponseCode.USERNAME_NOT_EMPTY.getMsg());
        }
        if(password == null || password.equals("")){
            return ServerResponse.createServerResponseByFail(ResponseCode.PASSWORD_NOT_EMPTY.getCode(),ResponseCode.PASSWORD_NOT_EMPTY.getMsg());
        }

        //2.查看用户名是否存在
        int count = userMapper.findByUsername(username);
        if(count == 0){
            return ServerResponse.createServerResponseByFail(ResponseCode.USERNAME_NOT_EXIST.getCode(),ResponseCode.USERNAME_NOT_EXIST.getMsg());
        }

        //3.根据用户名和密码去查询
        User user = userMapper.findByUsernameAndPassword(username,password);
        if(user == null){
            return ServerResponse.createServerResponseByFail(ResponseCode.PASSWORD_ERROR.getCode(),ResponseCode.PASSWORD_ERROR.getMsg());
        }


        //4.登录成功，返回结果
        user.setPassword("");
        return ServerResponse.createServerResponseBySuccess(user);
    }

    @Override
    public ServerResponse registerLogic(User user) {
        //注册的业务逻辑

        if(user == null){
            return ServerResponse.createServerResponseByFail(ResponseCode.PARAMTER_NOT_EMPTY.getCode(),ResponseCode.PARAMTER_NOT_EMPTY.getMsg());
        }

        String username = user.getUsername();
        String password = user.getPassword();

        //1.用户名和密码的非空判断
        if(username == null || username.equals("")){
            return ServerResponse.createServerResponseByFail(ResponseCode.USERNAME_NOT_EMPTY.getCode(),ResponseCode.USERNAME_NOT_EMPTY.getMsg());
        }
        if(password == null || password.equals("")){
            return ServerResponse.createServerResponseByFail(ResponseCode.PASSWORD_NOT_EMPTY.getCode(),ResponseCode.PASSWORD_NOT_EMPTY.getMsg());
        }

        //2.查看用户名是否存在
        int count = userMapper.findByUsername(username);
        if(count > 0){
            return ServerResponse.createServerResponseByFail(ResponseCode.USERNAME_EXIST.getCode(),ResponseCode.USERNAME_EXIST.getMsg());
        }

        //3.注册(注意前端要加密)
        int result = userMapper.register(user);
        if(result == 0){
            return ServerResponse.createServerResponseByFail(ResponseCode.REGISTER_FAIL.getCode(),ResponseCode.REGISTER_FAIL.getMsg());
        }

        return  ServerResponse.createServerResponseBySuccess();
    }
}
