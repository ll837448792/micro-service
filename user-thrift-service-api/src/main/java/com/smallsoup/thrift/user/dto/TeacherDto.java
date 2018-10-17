package com.smallsoup.thrift.user.dto;

import java.io.Serializable;

/**
 * @program: micro-service
 * @description: TeacherDto
 * @author: smallsoup
 * @create: 2018-09-26 22:38
 **/

public class TeacherDto extends UserInfoDto implements Serializable {

    private String intro;
    private int stars;

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }
}
