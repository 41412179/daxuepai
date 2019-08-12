package com.daxuepai.gaoxiao.controller;

import com.alibaba.fastjson.JSON;
import com.daxuepai.gaoxiao.model.Code;
import com.daxuepai.gaoxiao.model.Result;
import com.daxuepai.gaoxiao.model.ResultStatus;
import com.daxuepai.gaoxiao.service.CodeService;
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

        String code = String.valueOf(random.nextInt(99999));
        ZhenziSmsClient client = new ZhenziSmsClient(apiUrl,appId,appSecret);
        int id = -1;
        try {
            String result = client.send(phone,"【大学派】你的验证码是:" + code + ",该验证码有效期5分钟，消息来自中国最大的高校论坛[大学派]");
            Code generatedCode = new Code();
            generatedCode.setCode(code);
            Date now = new Date();
            generatedCode.setCreateTime(now);
            Date exiredTime = new Date(now.getTime()+300000);
            generatedCode.setExpiredTime(exiredTime);

            id = codeService.insertCode(generatedCode);
        } catch (Exception e) {
            logger.error("榛子云发送短信失败！");
            e.printStackTrace();
        }
        Result result = new Result();
        result.setStatus(ResultStatus.Ok);
        HashMap<String,Integer> map = new HashMap<>();
        map.put("id", id);
        result.setMsg(JSON.toJSONString(map));
        return JSON.toJSONString(result);
    }

    @RequestMapping("/register")
    @ResponseBody
    public String register(@RequestParam("phone") String phone,
                           @RequestParam("code") int code,
                           @RequestParam("school") int schoolId,
                           @RequestParam("username") String username){
        Result result = new Result();
        return JSON.toJSONString(result);
    }
}
