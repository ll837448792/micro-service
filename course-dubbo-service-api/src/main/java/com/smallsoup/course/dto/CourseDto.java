package com.smallsoup.course.dto;

import com.smallsoup.thrift.user.dto.TeacherDto;

import java.io.Serializable;

/**
 * @program: micro-service
 * @description:
 * @author: smallsoup
 * @create: 2018-09-26 22:02
 **/

public class CourseDto implements Serializable {

    private int id;
    private String title;
    private String description;
    private TeacherDto teacher;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TeacherDto getTeacher() {
        return teacher;
    }

    public void setTeacher(TeacherDto teacher) {
        this.teacher = teacher;
    }
}
