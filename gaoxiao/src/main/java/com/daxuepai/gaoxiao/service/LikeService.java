package com.daxuepai.gaoxiao.service;

import com.daxuepai.gaoxiao.dao.LikeDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class LikeService {

    @Autowired
    LikeDAO likeDAO;

    public int like(int userId, int postId) {
        return likeDAO.select(userId, postId);
    }

    public void delete(int id) {
        likeDAO.delete(id);
    }

    public void insert(int userId, int postId, Date date) {
        likeDAO.insert(userId,postId,date);
    }
}
