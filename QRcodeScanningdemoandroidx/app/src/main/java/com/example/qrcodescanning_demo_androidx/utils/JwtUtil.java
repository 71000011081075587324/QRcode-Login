//package com.example.qrcodescanning_demo_androidx.utils;
//
//import com.auth0.jwt.JWT;
//import com.auth0.jwt.JWTVerifier;
//import com.auth0.jwt.algorithms.Algorithm;
//import com.auth0.jwt.interfaces.DecodedJWT;
//
//import java.util.Date;
//
//public class JwtUtil {
//
//    private static JwtUtil jwtUtil;
//
//
//    //创建JwtUtil的单列类
//    public static JwtUtil getInstance(){
//        if(jwtUtil == null){
//            synchronized (JwtUtil.class){
//                if(jwtUtil == null){
//                    jwtUtil = new JwtUtil();
//                }
//            }
//        }
//        return jwtUtil;
//    }
//
//
//    /* *
//     * @Author lsc
//     * <p> 获得用户名 </p>
//     * @Param [request]
//     * @Return java.lang.String
//     */
//    public String getUserNameByToken(String token)  {
//        DecodedJWT jwt = JWT.decode(token);
//        return jwt.getClaim("username")
//                .asString();
//    }
//
//
//}
