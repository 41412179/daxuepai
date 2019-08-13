package com.daxuepai.gaoxiao.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.daxuepai.gaoxiao.model.Code;
import com.daxuepai.gaoxiao.model.Result;
import com.daxuepai.gaoxiao.model.ResultStatus;
import com.daxuepai.gaoxiao.model.User;
import com.daxuepai.gaoxiao.service.CodeService;
import com.daxuepai.gaoxiao.service.UserService;
import com.zhenzi.sms.ZhenziSmsClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

@Controller
public class LoginController {
    Logger logger = LoggerFactory.getLogger(LoginController.class);

    private String apiUrl = "https://sms_developer.zhenzikj.com";
    private String appId = "102332";
    private String appSecret = "e29c0d2e-c055-4890-852e-80428a05eb5a";
    Random random = new Random();

    @Autowired
    CodeService codeService;

    @RequestMapping("/getcode")
    @ResponseBody
    public String getcode(@RequestParam("phone") String phone){
        //前段校验phone
        Result result = new Result();
        String code = String.valueOf(random.nextInt(99999));
        ZhenziSmsClient client = new ZhenziSmsClient(apiUrl,appId,appSecret);
        int id = -1;
        try {
            logger.info("phone = " + phone + "; code: " + code);
            String codeResult = client.send(phone,"大学派:你的验证码是:" + code + ",该验证码有效期5分钟，消息来自中国最大的高校论坛");
            JSONObject json = JSONObject.parseObject(codeResult);
            if (json.getIntValue("code")!=0){//发送短信失败
                result.setStatus(ResultStatus.Failed);
                return JSONObject.toJSONString(result);
            }

        } catch (Exception e) {
            logger.error("榛子云发送短信失败！");
            e.printStackTrace();
        }

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
            return JSON.toJSONString(result);
        }
        result.setStatus(ResultStatus.Ok);
        HashMap<String,Integer> map = new HashMap<>();
        map.put("id", id);
        result.setMsg(JSON.toJSONString(map));
        return JSON.toJSONString(result);
    }

    @Autowired
    UserService userService;

    @RequestMapping("/register")
    @ResponseBody
    public String register(@RequestParam("phone") String phone,
                           @RequestParam("code") int code,
                           @RequestParam("school") int schoolId,
                           @RequestParam("username") String username){
        Result result = new Result();
        boolean success = checkCode(phone, code);
        if(success) {
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
            result.setStatus(ResultStatus.Ok);
            result.setMsg("注册成功");

        }else{
            result.setStatus(ResultStatus.Failed);
            result.setMsg("验证码校验未通过");
        }
        return JSON.toJSONString(result);
    }

    private boolean checkCode(String phone, int code) {
        String dbCode = codeService.selectByPhone(phone);
        if(dbCode == null){
            return false;
        }else{
            if(String.valueOf(code).equals(dbCode)){
                return true;
            }else{
                return false;
            }
        }
    }
}
