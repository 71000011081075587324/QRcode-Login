package com.example.demo.service;

import com.example.demo.common.ServerResponse;
import com.example.demo.pojo.User;

public interface IUserService {

    /**
     * 登录
     * @param username  用户名
     * @param password  密码
     * @return
     */
    public ServerResponse loginLogic(String username,String password);

    /**
     *
     * @param user 注册传入的用户
     * @return
     */
    public ServerResponse registerLogic(User user);
}
