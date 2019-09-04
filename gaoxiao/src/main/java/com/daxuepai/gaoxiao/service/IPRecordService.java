package com.daxuepai.gaoxiao.service;

import com.daxuepai.gaoxiao.dao.IPRecordDAO;
import com.daxuepai.gaoxiao.model.IPRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IPRecordService {
    @Autowired
    IPRecordDAO ipRecordDAO;


    public void recordIp(IPRecord ipRecord) {
        ipRecordDAO.insertRecord(ipRecord);
    }
}
