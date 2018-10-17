package com.smallsoup.course.service;

import com.smallsoup.course.dto.CourseDto;

import java.util.List;

/**
 * @program: micro-service
 * @description: ICourseService
 * @author: smallsoup
 * @create: 2018-09-26 22:01
 **/
public interface ICourseService {

    List<CourseDto> coursesInfos();
}
