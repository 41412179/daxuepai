package com.daxuepai.gaoxiao.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.daxuepai.gaoxiao.exception.ServiceException;
import com.daxuepai.gaoxiao.model.*;
import com.daxuepai.gaoxiao.service.CodeService;
import com.daxuepai.gaoxiao.service.UserService;
import com.daxuepai.gaoxiao.service.VerificationCodeService;
import com.daxuepai.gaoxiao.util.IPUtils;
import com.daxuepai.gaoxiao.util.StatusCode;
import com.zhenzi.sms.ZhenziSmsClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

@Controller
public class LoginController {
    Logger logger = LoggerFactory.getLogger(LoginController.class);

    private String apiUrl = "https://sms_developer.zhenzikj.com";
    private String appId = "102332";
    private String appSecret = "e29c0d2e-c055-4890-852e-80428a05eb5a";
    Random random = new Random();

    @Autowired
    CodeService codeService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    VerificationCodeService verificationCodeService;


    @RequestMapping(value = "/getcode",method = RequestMethod.GET)
    @ResponseBody
    public Result getcode(HttpServletRequest request,
                          @RequestParam("phone") String phone) throws Exception{

        Result result = new Result();
        String ip = IPUtils.getIpAddress(request);
        int userId = 0;
        User user = hostHolder.getUser();
        if(user != null){
            userId = user.getId();
        }
        VerificationCodeRecord record = new VerificationCodeRecord();
        record.setDate(new Date());
        record.setIp(ip);
        record.setPhone(phone);
        record.setUserId(userId);
        verificationCodeService.record(record);
        boolean isLimited = checkRequestCount(request, phone);
        if(isLimited){
            result = new Result(StatusCode.requet_too_buzy);
            return result;
        }

        //前段校验phone
        Result result = new Result();
        String code = String.valueOf(random.nextInt(99999));
        //把短信验证码插入
        int id = -1;
        Code generatedCode = new Code();
        generatedCode.setPhone(phone);
        generatedCode.setCode(code);
        Date now = new Date();
        generatedCode.setCreateTime(now);
        Date exiredTime = new Date(now.getTime()+300000);
        generatedCode.setExpiredTime(exiredTime);
        try {
            id = codeService.insertCode(generatedCode);
        }catch (Exception e){
            logger.error("", e);
            logger.error(StatusCode.INSERT_SMS_FAILED.toString());
            throw new ServiceException(StatusCode.SERVER_BUSY.getCode());
        }


        ZhenziSmsClient client = new ZhenziSmsClient(apiUrl,appId,appSecret);
        try {
            logger.info("phone = " + phone + "; code: " + code);
            String codeResult = client.send(phone,"大学派:你的验证码是:" + code + ",该验证码有效期5分钟，消息来自中国最大的高校论坛");
            JSONObject json = JSONObject.parseObject(codeResult);
            if (json.getIntValue("code")!=0){//发送短信失败
                result = new Result(StatusCode.SEND_SMS_FAILED);
                logger.error(StatusCode.SEND_SMS_FAILED.toString());
                return result;
            }
        } catch (Exception e) {
            logger.error(StatusCode.SEND_SMS_FAILED.toString());
            logger.error("", e);
            result = new Result(StatusCode.SEND_SMS_FAILED);
            return result;
        }

        result = new Result(StatusCode.SUCCESS);
        HashMap<String,Integer> map = new HashMap<>();
        map.put("id", id);
        result.setMsg(JSON.toJSONString(map));
        return result;
    }

    private boolean checkRequestCount(HttpServletRequest request, String phone) {
        String ip = IPUtils.getIpAddress(request);
        int ipCount = verificationCodeService.countIp(ip);
        int phoneCount = verificationCodeService.countPhone(phone);

    }

    @Autowired
    UserService userService;

    @RequestMapping(value = "/register",method = RequestMethod.GET)
    @ResponseBody
    public Result register(HttpServletResponse response,
                           @RequestParam("phone") String phone,
                           @RequestParam("code") int code,
                           @RequestParam("school") int schoolId,
                           @RequestParam("username") String username){
        Result result = new Result();

        User user1 = hostHolder.getUser();
        if(user1 != null){
            logger.error(StatusCode.REGISTER_HAS_LOGIN.toString());
            result = new Result(StatusCode.REGISTER_HAS_LOGIN);
            return result;
        }

        boolean success = checkCode(phone, code);
        if(success) {
            boolean hasRegister = checkPhone(phone);
            if(hasRegister){
                logger.error("注册失败：" + phone + "已经被注册了");
                result = new Result(StatusCode.PHONE_HAS_REGISTER);
                return result;
            }
            User user = new User();
            user.setPhone(phone);
            user.setSchool(schoolId);
            user.setUsername(username);
            user.setHeadurl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
            try {
                userService.insertUser(user);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("用户注册失败！");
                result = new Result(StatusCode.REGISTER_FAILED);
                return result;
            }
            //注册完成后进行登录
            String ticket = UUID.randomUUID().toString().replaceAll("-", "");
            Cookie cookie = new Cookie("ticket", ticket);
            cookie.setMaxAge(3600*24*5);
            cookie.setPath("/");
            response.addCookie(cookie);
            User u = new User();
            u.setTicket(ticket);
            Date now = new Date();
            Date timeOut = new Date(now.getTime() + 5*24*60*60*1000L);
            u.setTicketTimeout(timeOut);
            u.setTicketStatus(1);
            u.setPhone(phone);
            try {
                userService.updateUser(u);
            }catch (Exception e){
                e.printStackTrace();
                logger.error("注册后尝试登录失败");
                result = new Result(StatusCode.LOGIN_FAILED);
                return result;
            }
            result = new Result(StatusCode.SUCCESS, "注册并且登录成功");
        }else{
            result = new Result(StatusCode.verification_code_error);
            logger.error("验证码填写错误");
            return result;
        }
        result = new Result(StatusCode.SUCCESS);
        return result;
    }

    private boolean checkPhone(String phone) {
        int id = userService.selectByPhone(phone);
        if(id > 0){
            return true;
        }else{
            return false;
        }
    }

    private boolean checkCode(String phone, int code) {
        String dbCode = codeService.selectByPhone(phone);

        if(dbCode == null){
            logger.info("dbCode is null");
            return false;
        }else{
            logger.info("dbCode="+dbCode);
            if(String.valueOf(code).equals(dbCode)){
                return true;
            }else{
                return false;
            }
        }
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    @ResponseBody
    public Result login(@RequestParam("phone") String phone,
                        @RequestParam("code") String code,
                        @RequestParam(value = "isremember", defaultValue = "false") boolean isremember,
                        HttpServletResponse response){
        Result result = new Result();
        User u = hostHolder.getUser();

        if(u != null){
            logger.info("当前用户 ： " + u.toString());
            result = new Result(StatusCode.LOGIN_REPEAT);
            return result;
        }

        boolean isSuccess = checkCode(phone,Integer.valueOf(code));
        if(isSuccess){
            String ticket = UUID.randomUUID().toString().replaceAll("-", "");
            Cookie cookie = new Cookie("ticket", ticket);
            if(isremember){
                cookie.setMaxAge(3600*24*5);
                cookie.setPath("/");
            }else{
                cookie.setMaxAge(3600*24*5);
                cookie.setPath("/");
            }
            response.addCookie(cookie);
            User user = new User();
            user.setTicket(ticket);
            Date now = new Date();
            Date timeOut = new Date(now.getTime() + 5*24*60*60*1000L);
            user.setTicketTimeout(timeOut);
            user.setTicketStatus(1);
            user.setPhone(phone);
            try {
                userService.updateUser(user);
            }catch (Exception e){
                e.printStackTrace();
                logger.error("登录失败");
                result = new Result(StatusCode.LOGIN_FAILED);
                return result;
            }
            result = new Result(StatusCode.SUCCESS);
            return result;
        }else{
            result = new Result(StatusCode.verification_code_error);
            return result;
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    @ResponseBody
    public Result logout(){
        Result result = new Result();
        User u = hostHolder.getUser();
        if(u == null){
            result = new Result(StatusCode.dont_need_exit);
            return result;
        }
        User user = new User();
        user.setTicket(null);
        user.setTicketTimeout(null);
        user.setTicketStatus(1);
        //通过ticket注销
        try {
            userService.updateUser(user);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("退出失败！");
            result = new Result(StatusCode.exit_failed);
            return result;
        }
        result = new Result(StatusCode.SUCCESS);
        return result;
    }
}
