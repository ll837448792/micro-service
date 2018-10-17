package com.smallsoup.course.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.smallsoup.course.dto.CourseDto;
import com.smallsoup.course.service.ICourseService;
import com.smallsoup.thrift.user.dto.UserInfoDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @program: micro-service
 * @description: com.smallsoup.course.controller.CourseController
 * @author: smallsoup
 * @create: 2018-09-27 10:38
 **/
@Controller
@RequestMapping("/course")
public class CourseController {

    @Reference
    private ICourseService courseService;

    @RequestMapping(value = "/courseList", method = RequestMethod.GET)
    @ResponseBody
    public List<CourseDto> getCourseList(HttpServletRequest request) {
        UserInfoDto user = (UserInfoDto)request.getAttribute("user");
        System.out.println("user is: " + user.toString());

        return courseService.coursesInfos();
    }
}
