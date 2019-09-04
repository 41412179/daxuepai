package com.daxuepai.gaoxiao.dao;

import com.daxuepai.gaoxiao.model.Monitor;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MonitorDAO {

    @Insert("insert into monitor (user_id, url, take_time, create_time)values(#{userId}, #{url}, #{takeTime}, #{createTime})")
    int insert(Monitor monitor);
}
