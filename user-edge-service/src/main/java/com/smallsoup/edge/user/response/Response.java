package com.smallsoup.edge.user.response;

import java.io.Serializable;

/**
 * @program: micro-service
 * @description: Response
 * @author: smallsoup
 * @create: 2018-09-15 15:21
 **/

public class Response implements Serializable {

    private int code;

    private String message;

    public Response() {
        this.code = 0;
        this.message = "success";
    }

    public static final Response USERNAME_PASSWORD_INVALID = new Response(1001, "username or password is invlid");
    public static final Response EMAIL_MOBILE_REQUIRED = new Response(1002, "email or mobile is required");
    public static final Response SEND_VERIFY_CODE_FAILED = new Response(1003, "send verify code is failed");
    public static final Response VERIFY_CODE_FAILED = new Response(1004, "verify code is failed");
    public static final Response SUCCESS = new Response();

    public Response(int code, String message) {
        this.code = code;
        this.message = message;
    }

    //错误处理比较好的方法使用拦截器去处理
    public static Response exception(Exception ex) {
        return new Response(9999, ex.getMessage());
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
