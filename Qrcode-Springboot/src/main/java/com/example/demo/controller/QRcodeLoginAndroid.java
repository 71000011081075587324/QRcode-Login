package com.example.demo.controller;

import com.example.demo.common.ServerResponse;
import com.example.demo.pojo.QrLogin;
import com.example.demo.service.IQRcodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class QRcodeLoginAndroid {

    @Autowired
    private IQRcodeService qrcodeService;

    @RequestMapping("/qrLogin")
    public ServerResponse login(QrLogin qrLogin){
        ServerResponse serverResponse = qrcodeService.insertQrLogin(qrLogin);
        return serverResponse;
    }

}
