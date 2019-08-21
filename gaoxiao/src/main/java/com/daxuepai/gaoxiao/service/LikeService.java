package com.daxuepai.gaoxiao.service;

import com.daxuepai.gaoxiao.dao.LikeDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class LikeService {

    @Autowired
    LikeDAO likeDAO;

    public Integer like(int userId, int postId) {
        Integer ret = likeDAO.select(userId, postId);
        return ret != null  ? ret: -1;
    }

    public void delete(int id) {
        likeDAO.delete(id);
    }

    public void insert(int userId, int postId, Date date) {
        likeDAO.insert(userId,postId,date);
    }
}
