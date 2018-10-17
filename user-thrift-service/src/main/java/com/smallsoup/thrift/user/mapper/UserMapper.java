package com.smallsoup.thrift.user.mapper;

import com.smallsoup.thrift.user.UserInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @program: micro-service
 * @description: UserMapper
 * @author: smallsoup
 * @create: 2018-09-14 23:48
 **/
@Mapper
public interface UserMapper {

    @Select("select id, username as name, password, real_name as realName, " +
            "mobile, email from pe_user where id=#{id}")
    UserInfo getUserById(@Param("id") int id);

    @Select("select id, username as name, password, real_name as realName, " +
            "mobile, email from pe_user where username=#{username}")
    UserInfo getUserByUserName(@Param("username") String username);

    @Insert("insert into pe_user (username, password, mobile, email, real_name) " +
            "values (#{u.name}, #{u.password}, #{u.mobile}, #{u.email}, #{u.realName})")
    void registerUser(@Param("u") UserInfo userInfo);

    @Select("select u.id, u.username as name, u.password, u.real_name as realName, u.email, u.mobile, t.intro, t.stars " +
            "from pe_user u, pe_teacher t where u.id=#{id} and u.id=t.id")
    UserInfo getTeacherById(@Param("id") int id);

}
