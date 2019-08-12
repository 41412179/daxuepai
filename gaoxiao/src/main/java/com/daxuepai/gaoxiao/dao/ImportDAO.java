package com.daxuepai.gaoxiao.dao;

import com.daxuepai.gaoxiao.model.School;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ImportDAO {

    @Insert("insert into school (id, name, province_id, level, website, abbreviation, city )values(#{id},#{name},#{provinceId}, #{level}, #{website}, #{abbreviation}, #{city})")
    int insert(School school);
}
