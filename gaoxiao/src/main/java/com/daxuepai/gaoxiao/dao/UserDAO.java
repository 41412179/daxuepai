package com.daxuepai.gaoxiao.dao;

import com.daxuepai.gaoxiao.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDAO {

    @Insert("insert into user (phone, username, school, headurl, ticket, ticket_timeout) values(#{phone}, #{username},#{school},#{headurl},#{ticket}, #{ticketTimeout})")
    int insert(User user);
}
