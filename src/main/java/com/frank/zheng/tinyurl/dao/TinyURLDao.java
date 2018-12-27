package com.frank.zheng.tinyurl.dao;

import com.frank.zheng.tinyurl.entity.TinyUrl;
import org.apache.ibatis.annotations.*;
import org.springframework.lang.NonNull;

@Mapper
public interface TinyURLDao {

    @Select("SELECT * FROM tiny_url WHERE ID = #{id}")
    TinyUrl findById(@Param("id") int id);


    @Insert("INSERT INTO tiny_url(original_url) VALUES(#{original_url})")
    @Options(useGeneratedKeys = true)
    int insert(@NonNull TinyUrl tinyUrl);

    @Update("UPDATE tiny_url SET tiny_url = #{tiny_url} WHERE id = #{id}")
    int updateTinyUrl(@NonNull TinyUrl tinyUrl);

}


