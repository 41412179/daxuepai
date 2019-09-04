package com.daxuepai.gaoxiao.dao;

import com.daxuepai.gaoxiao.model.VerificationCodeRecord;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface VerificationCodeDAO {
    @Insert("insert into phone_code_record (user_id, ip, date, phone) values(#{userId}, #{ip}, #{date}, #{phone})")
    int insert(VerificationCodeRecord record);
}
