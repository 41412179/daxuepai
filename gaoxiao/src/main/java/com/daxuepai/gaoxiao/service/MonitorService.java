package com.daxuepai.gaoxiao.service;

import com.daxuepai.gaoxiao.dao.MonitorDAO;
import com.daxuepai.gaoxiao.model.Monitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MonitorService {
    @Autowired
    MonitorDAO monitorDAO;

    public int insert(Monitor monitor) {
        return monitorDAO.insert(monitor);
    }
}
