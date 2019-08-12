package com.daxuepai.gaoxiao.service;

import com.daxuepai.gaoxiao.dao.ImportDAO;
import com.daxuepai.gaoxiao.model.School;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImportService {
    @Autowired
    ImportDAO importDAO;

    public void importSchool(
    School school){
        importDAO.insert(school);
    }
}
