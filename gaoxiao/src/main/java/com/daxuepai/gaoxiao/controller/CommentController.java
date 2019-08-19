package com.daxuepai.gaoxiao.controller;

import com.alibaba.fastjson.JSON;
import com.daxuepai.gaoxiao.model.HostHolder;
import com.daxuepai.gaoxiao.model.Result;
import com.daxuepai.gaoxiao.model.ResultStatus;
import com.daxuepai.gaoxiao.model.User;
import com.daxuepai.gaoxiao.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
public class CommentController {
    @Autowired
    HostHolder hostHolder;

    @Autowired
    CommentService commentService;


    @RequestMapping("/post/comment/add")
    @ResponseBody
    public String comment(@RequestParam("postId") int postId,
                          @RequestParam("content") String content){
        Result result = new Result();

        User user = hostHolder.getUser();
        if(user == null){
            result.setStatus(ResultStatus.Failed);
            result.setMsg("评论失败，您还未登录");
        }else {
            int userId = user.getId();
            Date date = new Date();
            commentService.insert(userId, postId, content, date,date);
            result.setStatus(ResultStatus.Ok);
            result.setMsg("添加评论成功！");
        }
        return JSON.toJSONString(result);
    }

    @RequestMapping("/comment/delete")
    @ResponseBody
    public String deleteComment(@RequestParam("commentId") int commentId){
        User user = hostHolder.getUser();
        Result result = new Result();
        if(user == null){
            result.setStatus(ResultStatus.Failed);
            result.setMsg("未登录");
        }else{
            int userId = user.getId();
            int count = commentService.select(userId, commentId);
            if(count == 1){
                commentService.delete(userId, commentId);
            }
            result.setStatus(ResultStatus.Ok);
            result.setMsg("删除成功");
        }
        return JSON.toJSONString(result);
    }

    



}
