package com.smallsoup.edge.user.controller;

import com.smallsoup.edge.user.redis.RedisClient;
import com.smallsoup.edge.user.response.LoginResponse;
import com.smallsoup.edge.user.response.Response;
import com.smallsoup.edge.user.thrift.ServiceProvider;
import com.smallsoup.thrift.user.UserInfo;
import com.smallsoup.thrift.user.dto.UserInfoDto;
import org.apache.commons.lang.StringUtils;
import org.apache.thrift.TException;
import org.apache.tomcat.util.buf.HexUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;


/**
 * @program: micro-service
 * @description: UserController
 * @author: smallsoup
 * @create: 2018-09-15 14:36
 **/
@Controller
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private ServiceProvider serviceProvider;

    @Autowired
    private RedisClient redisClient;


    @RequestMapping(value = "/sendverifycode", method = RequestMethod.POST)
    @ResponseBody
    public Response sendVerifyCode(@RequestParam(value = "email", required = false) String email,
                                   @RequestParam(value = "mobile", required = false) String mobile) throws TException {

        String message = "your verify code is:";
        String code = randomCode("0123456789", 6);
        boolean isSuccess;
        try {
            if (StringUtils.isNotBlank(email)) {
                isSuccess = serviceProvider.getMessageService().sendEmailMessage(email, message + code);
                redisClient.set(email, code);
            } else if (StringUtils.isNotBlank(mobile)) {
                isSuccess = serviceProvider.getMessageService().sendMobileMessage(mobile, message + code);
                redisClient.set(mobile, code);
            } else {
                return Response.EMAIL_MOBILE_REQUIRED;
            }

            if (!isSuccess) {
                return Response.SEND_VERIFY_CODE_FAILED;
            }
        } catch (TException e) {
            e.printStackTrace();
            return Response.exception(e);
        }

        return Response.SUCCESS;
    }

    @RequestMapping(value = "/regist", method = RequestMethod.POST)
    @ResponseBody
    public Response register(@RequestParam("username") String username,
                             @RequestParam("password") String password,
                             @RequestParam(value = "realName", required = false) String realName,
                             @RequestParam(value = "email", required = false) String email,
                             @RequestParam(value = "mobile", required = false) String mobile,
                             @RequestParam("verifyCode") String verifyCode) {

        if (StringUtils.isBlank(email) && StringUtils.isBlank(mobile)) {
            return Response.EMAIL_MOBILE_REQUIRED;
        }

        if (StringUtils.isNotBlank(email)) {
            String code = (String)redisClient.get(email);
            if (!verifyCode.equals(code)) {
                return Response.VERIFY_CODE_FAILED;
            }

        } else {
            String code = (String)redisClient.get(mobile);
            if (!verifyCode.equals(code)) {
                return Response.VERIFY_CODE_FAILED;
            }
        }

        UserInfo userInfo = new UserInfo();
        userInfo.setEmail(email);
        userInfo.setMobile(mobile);
        userInfo.setName(username);
        userInfo.setPassword(md5(password));
        userInfo.setRealName(realName);

        try {
            serviceProvider.getUserService().send_registerUser(userInfo);
        } catch (TException e) {
            e.printStackTrace();
            return Response.exception(e);
        }

        return Response.SUCCESS;

    }

    @RequestMapping(value = "/authentication", method = RequestMethod.POST)
    @ResponseBody
    public UserInfoDto authentication(@RequestHeader("token") String token) {
        return redisClient.get(token);
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public Response login(@RequestParam("username") String username, @RequestParam("password") String password) {

        //1.验证用户名密码,调用userService服务

        UserInfo userInfo;
        try {
            userInfo = serviceProvider.getUserService().getUserByName(username);
        } catch (TException e) {
            e.printStackTrace();
            return Response.USERNAME_PASSWORD_INVALID;
        }

        if (null == userInfo) {
            return Response.USERNAME_PASSWORD_INVALID;
        }

        if (!userInfo.getPassword().equals(md5(password))) {
            return Response.USERNAME_PASSWORD_INVALID;
        }

        //2.生成token
        String token = getToken();


        //3.缓存用户,缓存到redis里
        redisClient.set(token, toDto(userInfo), 3600);

        return new LoginResponse(token);

    }

    private UserInfoDto toDto(UserInfo userInfo) {

        UserInfoDto userDto = new UserInfoDto();
        BeanUtils.copyProperties(userInfo, userDto);

        return userDto;
    }

    private String getToken() {

        return randomCode("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIGKLMNOPQRSTUVWXYZ", 32);

    }

    private String randomCode(String source, int size) {

        StringBuffer token = new StringBuffer(size);

        Random random = new Random();
        for (int i = 0; i < size; i++) {
            int locIndex = random.nextInt(source.length());
            token.append(source.charAt(locIndex));
        }

        return token.toString();
    }

    private String md5(String pwd) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] md5Bytes = md5.digest(pwd.getBytes("utf-8"));

            return HexUtils.toHexString(md5Bytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

}
