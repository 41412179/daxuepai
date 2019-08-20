package com.daxuepai.gaoxiao.controller;


import com.alibaba.fastjson.JSON;
import com.daxuepai.gaoxiao.model.*;
import com.daxuepai.gaoxiao.service.FilterService;
import com.daxuepai.gaoxiao.service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;
import java.util.logging.Filter;

@Controller
public class IndexController {
    Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    PostService postService;

    @Autowired
    FilterService filterService;

    @Autowired
    HostHolder hostHolder;

    //snnu :3761
    @RequestMapping(value = "/all/list", method= RequestMethod.GET)
    @ResponseBody
    public String getAllList(@RequestParam("schoolId") int schoolId,
                             @RequestParam("page") int pageNum){
        List<Post> posts = null;
        int row = (pageNum -1) * 10;
        int count = 10;
        if(schoolId == 0) {
            posts = postService.selectAllList(row, count);
        }else{
            posts = postService.selectAllListBySchoolId(row,count, schoolId);
        }

        if(posts != null && posts.size() > 0){
            posts = filterService.filter(posts);
        }
        return JSON.toJSONString(posts);
    }

    @RequestMapping(value = "/post/add", method = RequestMethod.GET)
    @ResponseBody
    public String addPost(@RequestParam("title") String title,
                          @RequestParam("type") String type,
                          @RequestParam("content") String content){
        Result result = new Result();
        User user = hostHolder.getUser();
        if(user == null){
            result.setStatus(ResultStatus.Failed);
            result.setMsg("用户未登录");
        }
        int uid = user.getId();
        Date createTime = new Date();
        Date changeTime = new Date();
        int schoolId = user.getSchool();

        try {
            postService.insertPost(uid, title, content, createTime, changeTime, schoolId, type);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("发帖失败");
            result.setStatus(ResultStatus.Failed);
            result.setMsg("发帖失败");
            return JSON.toJSONString(result);
        }
        result.setStatus(ResultStatus.Ok);
        result.setMsg("发帖成功");
        return JSON.toJSONString(result);
    }

}
