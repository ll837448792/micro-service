package com.smallsoup.course.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.smallsoup.course.dto.CourseDto;
import com.smallsoup.course.mapper.CourseMapper;
import com.smallsoup.course.thrift.ServiceProvider;
import com.smallsoup.thrift.user.UserInfo;
import com.smallsoup.thrift.user.dto.TeacherDto;
import org.apache.thrift.TException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @program: micro-service
 * @description: CourseServiceImpl
 * @author: smallsoup
 * @create: 2018-09-26 23:14
 **/
@Service
public class CourseServiceImpl implements ICourseService {

    @Autowired
    private CourseMapper courseMapper;
    
    @Autowired
    private ServiceProvider serviceProvider;

    @Override
    public List<CourseDto> coursesInfos() {

        List<CourseDto> courseList = courseMapper.listCourse();

        if (courseList != null) {

            for (CourseDto course: courseList) {
                //根据课程id查教师userId
                Integer teacherId = courseMapper.getCourseTeacher(course.getId());
                if (teacherId != null) {
                    //查教师信息
                    try {
                        UserInfo userInfo = serviceProvider.getUserService().getTeacherById(teacherId);
                        //将教师信息塞到course里,需将user信息转为teacher
                        course.setTeacher(trans2Teacher(userInfo));
                    } catch (TException e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            }
        }

        return courseList;
    }

    private TeacherDto trans2Teacher(UserInfo userInfo) {
        TeacherDto teacherInfo = new TeacherDto();
        BeanUtils.copyProperties(userInfo, teacherInfo);
        return teacherInfo;
    }
}
