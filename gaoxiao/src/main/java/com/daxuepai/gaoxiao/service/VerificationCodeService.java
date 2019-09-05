package com.daxuepai.gaoxiao.service;

import com.daxuepai.gaoxiao.dao.VerificationCodeDAO;
import com.daxuepai.gaoxiao.exception.ServiceException;
import com.daxuepai.gaoxiao.model.VerificationCodeRecord;
import com.daxuepai.gaoxiao.util.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VerificationCodeService {

    @Autowired
    VerificationCodeDAO verificationCodeDAO;


    public void record(VerificationCodeRecord record) {
        verificationCodeDAO.insert(record);
    }

    public int countIp(String ip) throws ServiceException {
        try {
            return verificationCodeDAO.countIP(ip);
        }catch (Exception e){
            throw new ServiceException(StatusCode.db_failed);
        }
    }

    public int countPhone(String phone) throws ServiceException {
        try {
            return verificationCodeDAO.countPhone(phone);
        }catch (Exception e){
            throw new ServiceException(StatusCode.db_failed);
        }
    }
}
