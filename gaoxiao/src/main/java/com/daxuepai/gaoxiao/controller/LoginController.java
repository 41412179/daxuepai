package com.daxuepai.gaoxiao.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.daxuepai.gaoxiao.model.*;
import com.daxuepai.gaoxiao.service.CodeService;
import com.daxuepai.gaoxiao.service.UserService;
import com.zhenzi.sms.ZhenziSmsClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
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

    @RequestMapping(value = "/getcode",method = RequestMethod.GET)
    @ResponseBody
    public String getcode(@RequestParam("phone") String phone){
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
            e.printStackTrace();
            logger.error("插入短信验证码出错");
            result.setStatus(ResultStatus.Failed);
            result.setMsg("服务器生成验证码出错");
            return JSON.toJSONString(result);
        }


        ZhenziSmsClient client = new ZhenziSmsClient(apiUrl,appId,appSecret);
        try {
            logger.info("phone = " + phone + "; code: " + code);
            String codeResult = client.send(phone,"大学派:你的验证码是:" + code + ",该验证码有效期5分钟，消息来自中国最大的高校论坛");
            JSONObject json = JSONObject.parseObject(codeResult);
            if (json.getIntValue("code")!=0){//发送短信失败
                result.setStatus(ResultStatus.Failed);
                result.setMsg("发送短信失败");
                logger.error("发送短信失败");
                return JSONObject.toJSONString(result);
            }
        } catch (Exception e) {
            logger.error("榛子云发送短信失败！");
            result.setStatus(ResultStatus.Failed);
            result.setMsg("发送短信失败");
            e.printStackTrace();
            return JSONObject.toJSONString(result);
        }

        result.setStatus(ResultStatus.Ok);
        HashMap<String,Integer> map = new HashMap<>();
        map.put("id", id);
        result.setMsg(JSON.toJSONString(map));
        return JSON.toJSONString(result);
    }

    @Autowired
    UserService userService;

    @RequestMapping(value = "/register",method = RequestMethod.GET)
    @ResponseBody
    public String register(HttpServletResponse response,
                           @RequestParam("phone") String phone,
                           @RequestParam("code") int code,
                           @RequestParam("school") int schoolId,
                           @RequestParam("username") String username){
        Result result = new Result();

        User user1 = hostHolder.getUser();
        if(user1 != null){
            result.setStatus(ResultStatus.Failed);
            result.setMsg("当前登录账户："+user1.getUsername() + " 请退出后再注册");
            return JSON.toJSONString(result);
        }

        boolean success = checkCode(phone, code);
        if(success) {
            boolean hasRegister = checkPhone(phone);
            if(hasRegister){
                logger.error("注册失败：" + phone + "已经被注册了");
                result.setMsg("该手机号已经被注册");
                result.setStatus(ResultStatus.Failed);
                return JSON.toJSONString(result);
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
                result.setStatus(ResultStatus.Failed);
                result.setMsg("注册失败：创建用户失败");
                return JSON.toJSONString(result);
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
                result.setStatus(ResultStatus.Failed);
                result.setMsg("注册后尝试登录失败");
                return JSON.toJSONString(result);
            }
            result.setStatus(ResultStatus.Ok);
            result.setMsg("注册并且登录成功");
        }else{
            result.setStatus(ResultStatus.Failed);
            result.setMsg("验证码填写错误");
            logger.error("验证码填写错误");
        }
        return JSON.toJSONString(result);
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
    public String login(@RequestParam("phone") String phone,
                        @RequestParam("code") String code,
                        @RequestParam(value = "isremember", defaultValue = "false") boolean isremember,
                        HttpServletResponse response){
        Result result = new Result();
        User u = hostHolder.getUser();

        if(u != null){
            logger.info("当前用户 ： " + u.toString());
            result.setStatus(ResultStatus.Failed);
            result.setMsg("当前在线账户："+u.getUsername()+" 请退出后再登录");
            return JSON.toJSONString(result);
        }else {
            logger.info("当前没有用户登录");
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
                result.setStatus(ResultStatus.Failed);
                result.setMsg("登录失败");
                return JSON.toJSONString(result);
            }
            result.setStatus(ResultStatus.Ok);
            result.setMsg("登录成功");
            return JSON.toJSONString(result);
        }else{
            result.setStatus(ResultStatus.Failed);
            result.setMsg("验证码填写错误");
            return JSON.toJSONString(result);
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    @ResponseBody
    public String logout(){
        Result result = new Result();
        User u = hostHolder.getUser();
        if(u == null){
            result.setStatus(ResultStatus.Ok);
            result.setMsg("退出成功");
            return JSON.toJSONString(result);
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
            result.setStatus(ResultStatus.Failed);
            return JSON.toJSONString(result);
        }
        result.setStatus(ResultStatus.Ok);
        result.setMsg("退出成功");
        return JSON.toJSONString(result);
    }
}
