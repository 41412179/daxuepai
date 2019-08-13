package com.daxuepai.gaoxiao.dao;

import com.daxuepai.gaoxiao.model.Code;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CodeDAO {

    @Insert("insert into code (code, phone , create_time, expired_time)values(#{code},#{phone},#{createTime},#{expiredTime})")
    int insert(Code code);

    @Select("select code from code where phone = #{phone} order by create_time limit 1")
    String select(String phone);
}
