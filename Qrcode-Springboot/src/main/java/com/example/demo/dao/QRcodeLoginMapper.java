package com.example.demo.dao;

import com.example.demo.pojo.QrLogin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface QRcodeLoginMapper {

    /**
     *
     * @param qrLogin
     * @return 返回0代表插入失败
     */
    int insert(@Param("qrLogin")QrLogin qrLogin);

    /**
     *
     * @param randchar  网页端生成二维码的字符串
     * @return  返回null代表randchar不存在，未登陆成功
     */
    String hasRandchar(@Param("randchar")String randchar);

}
