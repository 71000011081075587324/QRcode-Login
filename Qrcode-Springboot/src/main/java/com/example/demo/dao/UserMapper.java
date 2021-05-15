package com.example.demo.dao;

import com.example.demo.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {

    /**
     * 判断用户名是否存在
     * @param username  用户名
     * @return  返回用户名是否存在
     */
    int findByUsername(@Param("username") String username);

    /**
     * 根据用户名和密码去查询,判断登录是否成功
     * @param username  用户姓名
     * @param password  用户密码
     * @return  返回用户信息
     */
    User findByUsernameAndPassword(@Param("username") String username,@Param("password") String password);

    /**
     *
     * @param record 注册信息
     * @return  返回是否注册成功
     */
    int  register(@Param("record") User record);

    /**
     * 修改用户密码
     * @param newpassword
     * @return sql执行影响的记录条数（此处只需要判断是否为 0，不为0即代表修改成功）
     */
    int alterPassword(@Param("username") String username,@Param("primarypassword") String primarypassword,@Param("newpassword") String  newpassword);

}
