package com.example.demo.common;

import com.example.demo.Utils.JwtUtil;
import com.example.demo.pojo.User;
import com.example.demo.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * token验证拦截器
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    UserService userService;

    /**
     *
     * @param request   是指经过spring封装的请求对象, 包含请求地址, 头, 参数, body(流)等信息.
     * @param response  是指经过spring封装的响应对象, 包含输入流, 响应body类型等信息.
     * @param handler   是指controller的@Controller注解下的"完整"方法名, 是包含exception等字段信息的.
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.setCharacterEncoding("utf-8");

        // 从 http 请求头中取出 token
        String token = request.getHeader("token");

        //获得JwtUtil单例类
        JwtUtil jwtUtil = JwtUtil.getInstance();

        // 如果不是映射到方法直接通过
        if(!(handler instanceof HandlerMethod)){
            return true;
        }
        if (token != null){
            String username = jwtUtil.getUserNameByToken(request);
            boolean result = jwtUtil.verify(token,username);
            if(result){
                System.out.println("通过拦截器");
                return true;
            }
        }
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
