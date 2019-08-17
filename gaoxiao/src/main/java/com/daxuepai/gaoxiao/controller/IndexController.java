package com.daxuepai.gaoxiao.controller;


import com.alibaba.fastjson.JSON;
import com.daxuepai.gaoxiao.model.Post;
import com.daxuepai.gaoxiao.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class IndexController {

    @Autowired
    PostService postService;



    @RequestMapping(value = "/all/list", method= RequestMethod.GET)
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
        return JSON.toJSONString(posts);
    }
}
