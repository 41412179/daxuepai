package com.daxuepai.gaoxiao.dao;

import com.daxuepai.gaoxiao.model.IPRecord;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IPRecordDAO {


    @Insert("insert into user_daily_ip_records (user_id, ip, date)values(#{userId}, #{ip}, #{date})")
    int insertRecord(IPRecord ipRecord);
}
