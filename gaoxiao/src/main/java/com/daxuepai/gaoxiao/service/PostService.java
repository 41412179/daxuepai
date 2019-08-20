package com.daxuepai.gaoxiao.service;

import com.daxuepai.gaoxiao.dao.PostDAO;
import com.daxuepai.gaoxiao.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PostService {


    @Autowired
    PostDAO postDAO;

    public List<Post> selectAllList(int row, int count) {
        return postDAO.selectAllList(row, count);
    }

    public List<Post> selectAllListBySchoolId(int row, int count, int schoolId) {
        return postDAO.selectAllListBySchoolId(schoolId,row,count);
    }

    public void insertPost(int uid, String title, String content, Date createTime, Date changeTime, int schoolId, String type) {
        postDAO.insertPost(uid, title, content, createTime, changeTime, schoolId, type);
    }
}
