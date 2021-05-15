package com.example.demo.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo.Utils.JwtUtil;
import com.example.demo.common.ResponseCode;
import com.example.demo.common.ServerResponse;
import com.example.demo.dao.UserMapper;
import com.example.demo.pojo.User;
import com.example.demo.service.IUserService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;

@Service
public class UserService implements IUserService {

    @Autowired
    UserMapper userMapper;


    @Override
    public ServerResponse alterPassword(String username,String newPassword,String primaryPassword) {

        System.out.println("username = " + username);
        System.out.println("newPassword = " + newPassword);
        System.out.println("primaryPassword = " + primaryPassword);

        if(username == null || username.equals("")){
            return ServerResponse.createServerResponseByFail(ResponseCode.ERROR.getCode(),ResponseCode.ERROR.getMsg());
        }

        if(newPassword.equals("") || newPassword == null || primaryPassword.equals("") || primaryPassword == null){
            return ServerResponse.createServerResponseByFail(ResponseCode.PASSWORD_NOT_EMPTY.getCode(),ResponseCode.PASSWORD_NOT_EMPTY.getMsg());
        }

        int result = userMapper.alterPassword(username,newPassword,primaryPassword);
        if(result > 0){
            return ServerResponse.createServerResponseBySuccess();
        }else{
            return ServerResponse.createServerResponseByFail(ResponseCode.PASSWORD_ERROR.getCode(),ResponseCode.PASSWORD_ERROR.getMsg());
        }

    }


    @Override
    public ServerResponse tokenLogin(String token) {

        System.out.println("token = " + token);

        //获得JwtUtil单例类
        JwtUtil jwtUtil = JwtUtil.getInstance();

        if (token != ""){
            DecodedJWT jwt = JWT.decode(token);
            String username =  jwt.getClaim("username").asString();
            boolean result = jwtUtil.verify(token,username);
            if(result){
//                System.out.println("通过拦截器");
                return ServerResponse.createServerResponseBySuccess();
            }
        }
        return ServerResponse.createServerResponseByFail(ResponseCode.TOKEN_VERIFY_FAIL.getCode(),ResponseCode.TOKEN_VERIFY_FAIL.getMsg());
    }

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

        JwtUtil jwtUtil = JwtUtil.getInstance();

        return ServerResponse.createServerResponseBySuccess(jwtUtil.sign(username));
    }

    @Override
    public ServerResponse registerLogic(User user) {
        //注册的业务逻辑

        System.out.println("user = " + user);
        System.out.println("user.id = " + user.getUesrid());
        System.out.println("user.username = " + user.getUsername());
        System.out.println("user.password = " + user.getPassword());


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
