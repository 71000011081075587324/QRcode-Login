package com.example.demo.service;

import com.example.demo.common.ServerResponse;
import com.example.demo.pojo.QrLogin;

public interface IQRcodeService {
    /**
     * 获得QRcode Img
     * @return  返回生成的二维码的名称
     */
    public String getQRcodeImg();

    /**
     * 判断是否登录成功(randchar是否存在)
     */
    public ServerResponse hasRandchar(String randChar);

    /**
     * Android端扫码存入数据
     * @return
     */
    public ServerResponse insertQrLogin(QrLogin qrLogin);

}
