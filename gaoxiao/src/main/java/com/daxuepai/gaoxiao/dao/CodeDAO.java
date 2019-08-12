package com.daxuepai.gaoxiao.dao;

import com.daxuepai.gaoxiao.model.Code;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CodeDAO {

    @Insert("insert into code (code, create_time, expired_time)values(#{code},#{createTime},#{expiredTime})")
    int insert(Code code);
}
