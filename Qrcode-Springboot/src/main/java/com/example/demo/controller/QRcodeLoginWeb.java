package com.example.demo.controller;

import com.example.demo.common.ServerResponse;
import com.example.demo.service.IQRcodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class QRcodeLoginWeb {

    @Autowired
    private IQRcodeService qrcodeService;

    @GetMapping(value = "/QRcodeLogin")
    public String getImage(Model model){
        model.addAttribute("QRcodeImg",qrcodeService.getQRcodeImg());   //传给页面图片名称
        return "QRcodeLogin";
    }

    @ResponseBody
    @GetMapping("/QRcodeLogin/check")
    public ServerResponse qrlogin(String randchar){
        return qrcodeService.hasRandchar(randchar);
    }

    @GetMapping("/QRcodeLogin/success")
    public String loginSuccess(String username,Model model){
            model.addAttribute("username",username);
            return "LoginSuccess";
    }

}
