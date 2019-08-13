package com.daxuepai.gaoxiao.service;

import com.daxuepai.gaoxiao.dao.UserDAO;
import com.daxuepai.gaoxiao.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserDAO userDAO;

    public void insertUser(User user) {
        userDAO.insert(user);
    }

    public void updateUser(User user) {
        userDAO.update(user);
    }
}
