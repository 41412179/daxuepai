package com.daxuepai.gaoxiao.dao;

import com.daxuepai.gaoxiao.model.VerificationCodeRecord;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Date;

@Mapper
public interface VerificationCodeDAO {
    @Insert("insert into phone_code_record (user_id, ip, date, phone) values(#{userId}, #{ip}, #{date}, #{phone})")
    int insert(VerificationCodeRecord record);

    @Select("select count(*) as count from phone_code_record where ip = #{ip}")
    int countIP(String ip);

    @Select("select count(*) as count from phone_code_record where phone = #{phone} date > #{date}")
    int countPhone(String phone, Date date);
}
