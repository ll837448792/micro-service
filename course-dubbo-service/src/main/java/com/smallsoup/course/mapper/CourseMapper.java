package com.smallsoup.course.mapper;

import com.smallsoup.course.dto.CourseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @program: micro-service
 * @description: com.smallsoup.course.mapper.CourseMapper
 * @author: smallsoup
 * @create: 2018-09-26 23:20
 **/
@Mapper
public interface CourseMapper {

    @Select("select * from pe_course")
    List<CourseDto> listCourse();

    @Select("select user_id from pr_user_course where course_id = #{courseId}")
    Integer getCourseTeacher(@Param("courseId") int courseId);
}
