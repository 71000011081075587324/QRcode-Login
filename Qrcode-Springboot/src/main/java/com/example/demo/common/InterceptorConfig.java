package com.example.demo.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

/* *
 * @Author lsc
 * <p>拦截器配置 </p>
 * @Param
 * @Return
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    /* *
     * @Author lsc
     * <p> 设置拦截路径 </p>
     * @Param [registry]
     * @Return void
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        List<String> excludePathLists= new ArrayList<>();
//        excludePathLists.add("/user/*");
        excludePathLists.add("/user/login");
        excludePathLists.add("/user/register");
        excludePathLists.add("/user/tokenlogin");
        excludePathLists.add("/QRcodeLogin");
        excludePathLists.add("/QRcodeLogin/check");

//        excludePathLists.add("/QRcodeLogin/success");

        registry.addInterceptor(authenticationInterceptor())
                .excludePathPatterns(excludePathLists)
                .addPathPatterns("/**");
    }
    /* *
     * @Author lsc
     * <p> 将自定义拦截器注入context </p>
     * @Param []
     * @Return com.example.demo.common.JwtInterceptor
     */
    @Bean
    public JwtInterceptor authenticationInterceptor() {
        return new JwtInterceptor();
    }


    /* *
     * @Author lsc
     * <p>跨域支持 </p>
     * @Param [registry]
     * @Return void
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "DELETE", "PUT", "PATCH", "OPTIONS", "HEAD")
                .maxAge(3600 * 24);
    }
}