package com.daxuepai.gaoxiao.service;

import com.daxuepai.gaoxiao.dao.CommentDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CommentService {

    @Autowired
    CommentDAO commentDAO;

    public void insert(int userId, int postId, String content, Date createTime, Date changeTime) {
        commentDAO.insert(userId, postId, content, createTime, changeTime);
    }

    public int select(int userId, int commentId) {
        return commentDAO.select(userId, commentId);
    }

    public void delete(int userId, int commentId) {
        commentDAO.delete(userId, commentId);
    }
}
