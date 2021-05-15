package com.example.demo.service;

import com.example.demo.common.ServerResponse;
import com.example.demo.pojo.User;

public interface IUserService {

    /**
     * 登录
     * @param username  用户名
     * @param password  密码
     * @return 登录成功返回 token，或者返回各种登录不成功的原因
     */
    public ServerResponse loginLogic(String username,String password);

    /**
     *
     * @param user 注册传入的用户
     * @return 返回是否注册成功
     */
    public ServerResponse registerLogic(User user);


    /**
     * 判断token是否有效
     * @param token 前端传来的 token 值
     * @return  返回是否有效
     */
    public ServerResponse tokenLogin(String token);

    /**
     * 修改密码
     * @param newPassword
     * @return
     */
    public ServerResponse alterPassword(String username,String newPassword,String primaryPassword);

}
