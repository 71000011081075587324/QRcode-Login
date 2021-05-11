package com.example.demo.controller;

import com.example.demo.common.ServerResponse;
import com.example.demo.pojo.User;
import com.example.demo.service.IUserService;
import com.example.demo.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @RequestMapping("/login")
    public ServerResponse login(String username,String password){
        ServerResponse serverResponse = userService.loginLogic(username,password);
        return serverResponse;
    }

    @RequestMapping("/register")
    public ServerResponse register(User user){
        return userService.registerLogic(user);
    }

}
