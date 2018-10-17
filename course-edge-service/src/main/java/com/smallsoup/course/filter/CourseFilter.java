package com.smallsoup.course.filter;

import com.smallsoup.thrift.user.dto.UserInfoDto;
import com.smallsoup.user.client.LoginFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *  * @program: micro-service
 *   * @description: filter.CourseFilter
 *    * @author: smallsoup
 *     * @create: 2018-09-27 11:24
 *      **/
@Component
public class CourseFilter extends LoginFilter {

    @Value("${user.edge.service.addr}")
    private String userEdgeServiceAddr;

    @Override
    protected void login(HttpServletRequest request, HttpServletResponse response, UserInfoDto userInfo) {
        System.out.println("CourseFilter userInfo is" + userInfo);
        request.setAttribute("user", userInfo);
    }

    @Override
    protected String userEdgeServiceAddr() {
        return userEdgeServiceAddr;
    }
}

