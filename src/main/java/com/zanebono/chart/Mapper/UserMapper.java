package com.zanebono.chart.Mapper;

import com.zanebono.chart.Model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface UserMapper {

    @Select("select * from user where username = #{username} and password = #{password}")
     User selectUser(@Param("username") String username,@Param("password") String password);

    @Select("select * from user")
     List<User> selectAllUser();

    @Select("select * from user where status='在线'")
     List<User> selectOnlineUser();

    @Update("update user set status = '在线' where username=#{username}")
     void changeOnline(@Param("username") String username);

    @Update("update user set status = '离线' where username=#{username}")
     void changeOffline(@Param("username") String username);

    @Select("select * from user where username = #{username}")
     User selectByName(String username);
}
