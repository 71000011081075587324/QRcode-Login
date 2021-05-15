package com.example.demo.Utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

public class JwtUtil {

    private static JwtUtil jwtUtil;

    private final static String secret = "Qrcode"; // 目的是加盐，内容随意(后期应改为每个用户有一个私钥，当用户注册时，服务器生成对应的公钥，并返回对应的私钥)

    // Token过期时间20分钟
    public static final long EXPIRE_TIME = 20 * 60 * 1000;

    //创建JwtUtil的单列类
    public static JwtUtil getInstance(){
        if(jwtUtil == null){
            synchronized (JwtUtil.class){
                if(jwtUtil == null){
                    jwtUtil = new JwtUtil();
                }
            }
        }
        return jwtUtil;
    }

    /* *
     * @Author lsc
     * <p> 校验token是否正确 </p>
     * @Param token
     * @Param username
     * @Param secret
     * @Return boolean
     */
    public boolean verify(String token, String username) {
        try {
            // 设置加密算法
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("username", username)
                    .build();
            // 效验TOKEN
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }



    /* *
     * @Author lsc
     * <p>创建Token并给它签名,20min后过期 </p>
     * @Param [username, secret]
     * @Return java.lang.String
     */
    public String sign(String username) {
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        Algorithm algorithm = Algorithm.HMAC256(secret);    //设置加密算法，使用HMAC256进行加密
        // 附带username信息的token（token为一个字符串）
        return JWT.create()     // 签发对象
                .withClaim("username", username)    //载荷（设置附带信息）
                .withExpiresAt(date)    //过期时间
                .sign(algorithm);   //签名 (由token的前几位+盐以哈希算法压缩成一定长的十六进制字符串)

    }

    /* *
     * @Author lsc
     * <p> 获得用户名 </p>
     * @Param [request]
     * @Return java.lang.String
     */
    public String getUserNameByToken(HttpServletRequest request)  {
        String token = request.getHeader("token");
        System.out.println("getUserNameByToken中token:" + token);
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getClaim("username")
                .asString();
    }


}
