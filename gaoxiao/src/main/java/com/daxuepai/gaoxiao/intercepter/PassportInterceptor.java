package com.daxuepai.gaoxiao.intercepter;

import com.daxuepai.gaoxiao.model.HostHolder;
import com.daxuepai.gaoxiao.model.User;
import com.daxuepai.gaoxiao.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;


@Component
public class PassportInterceptor implements HandlerInterceptor {
    Logger logger = LoggerFactory.getLogger(PassportInterceptor.class);

    @Autowired
    UserService userService;

    @Autowired
    HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ticket = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("ticket")) {
                    ticket = cookie.getValue();
                    break;
                }
            }
        }
        if(ticket!= null){
            User user = userService.selectByTicket(ticket);
            if(user == null || user.getTicketTimeout().before(new Date()) || user.getTicketStatus() == 0){
                if(user == null){
                    logger.info("利用ticket未找到user");
                }else if(user.getTicketTimeout().before(new Date())){
                    logger.info("ticket已经过期");
                }else {
                    logger.info("当前用户已经登出");
                }
            }else{
                logger.info("当前用户 = "+user.toString());
                hostHolder.setUser(user);
            }
        }else{
            logger.info("当前客户端未保存ticket");
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (modelAndView != null && hostHolder.getUser() != null) {
            modelAndView.addObject("user", hostHolder.getUser());
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolder.clear();
    }
}
