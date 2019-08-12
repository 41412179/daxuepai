package com.daxuepai.gaoxiao.service;

import com.daxuepai.gaoxiao.dao.CodeDAO;
import com.daxuepai.gaoxiao.model.Code;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CodeService {

    @Autowired
    CodeDAO codeDAO;

    public int insertCode(Code code) {
        return codeDAO.insert(code);
    }
}
