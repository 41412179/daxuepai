package com.daxuepai.gaoxiao.service;

import com.daxuepai.gaoxiao.dao.VerificationCodeDAO;
import com.daxuepai.gaoxiao.model.VerificationCodeRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VerificationCodeService {

    @Autowired
    VerificationCodeDAO verificationCodeDAO;


    public void record(VerificationCodeRecord record) {
        verificationCodeDAO.insert(record);
    }
}
