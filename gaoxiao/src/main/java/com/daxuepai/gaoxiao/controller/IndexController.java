package com.daxuepai.gaoxiao.controller;


import com.daxuepai.gaoxiao.model.*;
import com.daxuepai.gaoxiao.service.FilterSensitiveWords;
import com.daxuepai.gaoxiao.service.TiebaFilterService;
import com.daxuepai.gaoxiao.service.PostService;
import com.daxuepai.gaoxiao.util.StatusCode;
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

@Controller
public class IndexController {
    Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    PostService postService;

    @Autowired
    TiebaFilterService tiebaFilterService;

    @Autowired
    FilterSensitiveWords filterSensitiveWords;

    @Autowired
    HostHolder hostHolder;

    //snnu :3761
    @RequestMapping(value = "/all/list", method= RequestMethod.GET)
    @ResponseBody
    public Result getAllList(@RequestParam("schoolId") int schoolId,
                             @RequestParam("page") int pageNum){
        Result result = null;
        List<Post> posts = null;
        try {

            int row = (pageNum - 1) * 10;
            int count = 10;
            if (schoolId == 0) {
                posts = postService.selectAllList(row, count);
            } else {
                posts = postService.selectAllListBySchoolId(row, count, schoolId);
            }

            if (posts != null && posts.size() > 0) {
                posts = tiebaFilterService.filter(posts);
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error("获取帖子列表出错");
            result = new Result(StatusCode.SERVER_BUSY, "获取帖子失败");
            return result;
        }
        result = new Result(StatusCode.SUCCESS);
        result.setData("posts", posts);
        return result;
    }

    @RequestMapping(value = "/post/add", method = RequestMethod.GET)
    @ResponseBody
    public Result addPost(@RequestParam("title") String title,
                          @RequestParam("type") String type,
                          @RequestParam("content") String content){
        content = filterSensitiveWords.filter(content);

        Result result = new Result();
        User user = hostHolder.getUser();
        if(user == null){
            result = new Result(StatusCode.USER_NOT_LOGIN);
            return result;
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
            result = new Result(StatusCode.SEND_POST_FAILED);
            return result;
        }
        result = new Result(StatusCode.SUCCESS);
        return result;
    }
}
