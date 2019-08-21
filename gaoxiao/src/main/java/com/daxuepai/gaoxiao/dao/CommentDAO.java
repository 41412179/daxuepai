package com.daxuepai.gaoxiao.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Date;

@Mapper
public interface CommentDAO {
    @Insert("insert into comment(owner_id, post_id, content, create_time, change_time)values(#{userId}, #{postId}, #{content}, #{createTime}, #{changeTime})")
    public void insert(int userId, int postId, String content, Date createTime, Date changeTime);

    @Select("select count(*) from comment where id = #{commentId} and owner_id = #{userId}  ")
    int select(int userId, int commentId);

    @Select("delete from comment where id = #{commentId} and owner_id = #{userId} ")
    Integer delete(int userId, int commentId);
}
