package com.daxuepai.gaoxiao.dao;

import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Date;

@Mapper
public interface LikeDAO {
    @Select("select id from `like` where user_id = #{userId} and post_id = #{postId}")
    Integer select(int userId, int postId);

    @Delete("delete from `like` where id = #{id}")
    Integer delete(int id);

    @Insert("insert into `like`(user_id, post_id, create_time)values(#{userId}, #{postId}, #{date})")
    Integer insert(int userId, int postId, Date date);
}
