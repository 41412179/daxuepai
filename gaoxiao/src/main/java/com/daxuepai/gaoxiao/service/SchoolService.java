package com.daxuepai.gaoxiao.service;

import com.daxuepai.gaoxiao.dao.SchoolDAO;
import com.daxuepai.gaoxiao.result.SchoolResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SchoolService {

    @Autowired
    SchoolDAO schoolDAO;


    public List<SchoolResult> getList() {
        return schoolDAO.selectList();
    }
}
