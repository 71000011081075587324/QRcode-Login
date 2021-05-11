package com.example.demo.service.impl;

import com.example.demo.common.ResponseCode;
import com.example.demo.common.ServerResponse;
import com.example.demo.dao.QRcodeLoginMapper;
import com.example.demo.pojo.QrLogin;
import com.example.demo.service.IQRcodeService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class QRcodeService implements IQRcodeService {

    @Autowired
    QRcodeLoginMapper qRcodeLoginMapper;

    @Override
    public ServerResponse hasRandchar(String randChar) {
        String result = qRcodeLoginMapper.hasRandchar(randChar);
        if(result == null){
            return ServerResponse.createServerResponseByFail(ResponseCode.QRCODELOGIN_FAIL.getCode(),ResponseCode.QRCODELOGIN_FAIL.getMsg());
        }
        return ServerResponse.createServerResponseBySuccess(result);
    }

    @Override
    public ServerResponse insertQrLogin(QrLogin qrLogin) {
        int result = qRcodeLoginMapper.insert(qrLogin);
        if(result == 0){
            return ServerResponse.createServerResponseByFail(ResponseCode.QRCODELOGIN_FAIL.getCode(),ResponseCode.QRCODELOGIN_FAIL.getMsg());
        }
        return ServerResponse.createServerResponseBySuccess();
    }

    @Override
    public String getQRcodeImg() {
        //生成的二维码保存位置
        String path = "F:\\课设-手机二维码的简单扫描实现\\Qrcode-Springboot\\src\\main\\resources\\static\\img";
        //生成唯一不重复的字符串
        String content = UUID.randomUUID().toString();

        try {
            String codeName = content + "QRcodeImg";// 二维码的图片名
            String imageType = "png";// 图片类型

            /**
             * MultiFormatWriter:多格式写入，这是一个工厂类，里面重载了两个 encode 方法，用于写入条形码或二维码
             *      encode(String contents,BarcodeFormat format,int width, int height,Map<EncodeHintType,?> hints)
             *      contents:条形码/二维码内容
             *      format：编码类型，如 条形码，二维码 等
             *      width：码的宽度
             *      height：码的高度
             *      hints：码内容的编码类型
             * BarcodeFormat：枚举该程序包已知的条形码格式，即创建何种码，如 1 维的条形码，2 维的二维码 等
             * BitMatrix：位(比特)矩阵或叫2D矩阵，也就是需要的二维码
             *
             *         **com.google.zxing.EncodeHintType：编码提示类型,枚举类型
             *         * EncodeHintType.CHARACTER_SET：设置字符编码类型
             *         * EncodeHintType.ERROR_CORRECTION：设置误差校正
             *         *      ErrorCorrectionLevel：误差校正等级，L = ~7% correction、M = ~15% correction、Q = ~25% correction、H = ~30% correction
             *         *      不设置时，默认为 L 等级，等级不一样，生成的图案不同，但扫描的结果是一样的
             *         * EncodeHintType.MARGIN：设置二维码边距，单位像素，值越小，二维码距离四周越近
             *         * *
             *
             *         **
             *         * 二维码的生成需要借助MatrixToImageWriter类，该类是由Google提供的
             *         *
             */
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            Map<EncodeHintType, String> hints = new HashMap<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            BitMatrix bitMatrix = multiFormatWriter.encode(codeName, BarcodeFormat.QR_CODE, 200, 200, hints);
            Path path1 = Paths.get(path, codeName + "." + imageType);
            MatrixToImageWriter.writeToPath(bitMatrix, imageType, path1);
            return codeName;
        } catch (WriterException | IOException e) {
            e.printStackTrace();
            return "";
        }




    }
}
