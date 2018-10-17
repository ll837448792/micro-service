package com.smallsoup.edge.user.response;

/**
 * @program: micro-service
 * @description: LoginResponse
 * @author: smallsoup
 * @create: 2018-09-15 16:41
 **/

public class LoginResponse extends Response {

    private String token;

    public LoginResponse(int code, String message, String token) {
        super(code, message);
        this.token = token;
    }

    public LoginResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

