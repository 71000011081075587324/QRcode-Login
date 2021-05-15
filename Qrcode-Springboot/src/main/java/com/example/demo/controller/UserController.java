package com.example.demo.controller;

import com.example.demo.common.ServerResponse;
import com.example.demo.pojo.User;
import com.example.demo.service.IUserService;
import com.example.demo.service.impl.UserService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {


    @Autowired
    private IUserService userService;

    @PostMapping("/login")
    public ServerResponse login(String username, String password){
        ServerResponse serverResponse = userService.loginLogic(username,password);
        return serverResponse;
    }

    @PostMapping("/register")
    public ServerResponse register(String username,  String password){
//        System.out.println("username = " + username);
//        System.out.println("password = " + password);
        User user = new User(username,password);
        return userService.registerLogic(user);
    }

    @PostMapping("/tokenlogin")
    public ServerResponse tokenLogin(String token){
//        System.out.println("controller token = " + token);
        return  userService.tokenLogin(token);
    }

    @PostMapping("/alterlogin")
    public ServerResponse alterPassword(String username,String newPassword,String primaryPassword){
        System.out.println("username = " + username);
        System.out.println("newPassword = " + newPassword);
        System.out.println("primaryPassword = " + primaryPassword);
        return userService.alterPassword(username,newPassword,primaryPassword);
    }

}
