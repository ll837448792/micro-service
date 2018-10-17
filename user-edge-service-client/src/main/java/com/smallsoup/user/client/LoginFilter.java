package com.smallsoup.user.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.smallsoup.thrift.user.dto.UserInfoDto;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

/**
 * @program: micro-service
 * @description: LoginFilter
 * @author: smallsoup
 * @create: 2018-09-16 10:02
 **/

public abstract class LoginFilter implements Filter {

    private static Cache<String, UserInfoDto> cache = CacheBuilder.newBuilder().
            maximumSize(10000).
            expireAfterWrite(3, TimeUnit.MINUTES).
            build();


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //先获取token
        String token = request.getParameter("token");

        if (StringUtils.isBlank(token)) {

            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("token".equals(cookie.getName())) {
                        token = cookie.getValue();
                        break;
                    }
                }
            }
        }

        //2.根据token去调用单点登录系统获取用户信息
        UserInfoDto userInfo = null;
        if (StringUtils.isNotBlank(token)) {

            userInfo = cache.getIfPresent(token);

            if (userInfo == null) {
                userInfo = getRequestUserInfo(token);
                //只有远端调用完才需放入cache中,3分钟之内token不过期,也不会进入这里的逻辑进行远程调用获取token
                if (userInfo != null) {
                    cache.put(token, userInfo);
                }
            }
        }

        System.out.println("LoginFilter userInfo is" + userInfo);

        //3.如果获取到的user信息是空,则跳转到登录页
        if (userInfo == null) {
            response.sendRedirect("http://www.smallsoup.com/user/login");
            return;
        }


        login(request, response, userInfo);
        filterChain.doFilter(request, response);
    }

    protected abstract void login(HttpServletRequest request, HttpServletResponse response, UserInfoDto userInfo);
    protected abstract String userEdgeServiceAddr();

    //发送request请求,调用单点登录系统根据token获取用户信息接口
    private UserInfoDto getRequestUserInfo(String token) {
        String url = "http://" + userEdgeServiceAddr()  + "/user/authentication";
//        String url = "http://www.smallsoup.com/user/authentication";
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);

        post.addHeader("token", token);
        InputStream in = null;
        try {
            HttpResponse response = client.execute(post);
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                throw new RuntimeException("request user info is failed, statusLine:" + response.getStatusLine());
            }

            /*String data = EntityUtils.toString(response.getEntity());
            return new ObjectMapper().readValue(data, UserInfoDto.class);*/
            in = response.getEntity().getContent();

            StringBuffer result = new StringBuffer();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) > 0) {
                result.append(new String(buffer, 0, len));
            }

            return new ObjectMapper().readValue(result.toString(), UserInfoDto.class);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    public void destroy() {

    }
}
