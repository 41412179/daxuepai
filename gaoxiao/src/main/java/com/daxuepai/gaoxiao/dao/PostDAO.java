package com.daxuepai.gaoxiao.dao;

import com.daxuepai.gaoxiao.model.Post;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PostDAO {

    @Select("select * from post limit #{row}, #{count}")
    List<Post> selectAllList(int row, int count);

    @Select("select * from post where school_id = #{schoolId} limit #{row}, #{count}")
    List<Post> selectAllListBySchoolId( int schoolId,int row, int count);
}
