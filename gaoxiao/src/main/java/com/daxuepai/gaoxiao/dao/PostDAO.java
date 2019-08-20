package com.daxuepai.gaoxiao.dao;

import com.daxuepai.gaoxiao.model.Post;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

@Mapper
public interface PostDAO {

    @Select("select * from post limit #{row}, #{count}")
    List<Post> selectAllList(int row, int count);

    @Select("select * from post where school_id = #{schoolId} limit #{row}, #{count}")
    List<Post> selectAllListBySchoolId( int schoolId,int row, int count);

    @Insert("insert into post(owner_id, title, content, post_type, create_time, change_time, school_id)values(#{uid}, #{title}, #{content}, #{type}, #{createTime}, #{changeTime}, #{schoolId})")
    int insertPost(int uid, String title, String content, Date createTime, Date changeTime, int schoolId, String type);
}
