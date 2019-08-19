package com.daxuepai.gaoxiao.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Date;

@Mapper
public interface LikeDAO {
    @Select("select id from like where user_id = #{userId} and post_id = #{postId}")
    int select(int userId, int postId);

    @Delete("delete from like where id = #{id}")
    int delete(int id);

    @Insert("insert into like(user_id, post_id, create_time)values(#{userId}, #{postId}, #{date})")
    int insert(int userId, int postId, Date date);
}
