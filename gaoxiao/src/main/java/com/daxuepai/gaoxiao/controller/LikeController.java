package com.daxuepai.gaoxiao.controller;

import com.alibaba.fastjson.JSON;
import com.daxuepai.gaoxiao.model.HostHolder;
import com.daxuepai.gaoxiao.model.Result;
import com.daxuepai.gaoxiao.model.ResultStatus;
import com.daxuepai.gaoxiao.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
public class LikeController {

    @Autowired
    LikeService likeService;

    @Autowired
    HostHolder hostHolder;

    static final Object lock = new Object();


    @RequestMapping("/post/like")
    @ResponseBody
    public String likePost(@RequestParam("postId") int postId){
        Result result = new Result();
        if(hostHolder.getUser() != null) {
            int userId = hostHolder.getUser().getId();
            synchronized (lock) {
                int likeId = likeService.like(userId, postId);
                if (likeId > 0) {
                    likeService.delete(likeId);
                } else {
                    likeService.insert(userId, postId, new Date());
                }
            }
            result.setStatus(ResultStatus.Ok);
            result.setMsg("成功");
        }else {
            result.setStatus(ResultStatus.Failed);
            result.setMsg("用户未登录，请先登录");
        }
        return JSON.toJSONString(result);
    }

}
