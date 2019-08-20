package com.daxuepai.gaoxiao.dao;

import com.daxuepai.gaoxiao.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserDAO {

    @Insert("insert into user (phone, username, school, headurl, ticket, ticket_timeout, ticket_status) values(#{phone}, #{username},#{school},#{headurl},#{ticket}, #{ticketTimeout}, #{ticketStatus})")
    int insert(User user);

    @Update("update user set ticket = #{ticket} , ticket_timeout = #{ticketTimeout},ticket_status = #{ticketStatus} where phone = #{phone}")
    int update(User user);

    @Select("select * from user where ticket = #{ticket}")
    User selectByTicket(String ticket);

    @Select("select id from user where phone = #{phone}")
    int selectByPhone(String phone);
}
