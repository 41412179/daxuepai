package com.daxuepai.gaoxiao.service;

import com.daxuepai.gaoxiao.dao.IPRecordDAO;
import com.daxuepai.gaoxiao.model.IPRecord;
import com.daxuepai.gaoxiao.util.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.ConnectException;

@Service
public class IPRecordService {
    Logger logger = LoggerFactory.getLogger(IPRecordService.class);

    @Autowired
    IPRecordDAO ipRecordDAO;


    public void recordIp(IPRecord ipRecord) {
        try {
            ipRecordDAO.insertRecord(ipRecord);
        }catch (Exception e){
            logger.error(StatusCode.db_failed.toString());
            logger.error("" , e);
        }
    }
}
