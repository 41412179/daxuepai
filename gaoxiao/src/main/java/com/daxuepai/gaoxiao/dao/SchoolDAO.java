package com.daxuepai.gaoxiao.dao;

import com.daxuepai.gaoxiao.result.SchoolResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SchoolDAO {


    @Select("select id, name from school")
    List<SchoolResult> selectList();
}
