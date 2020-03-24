package com.wonders.shiro.controller;

import com.alibaba.fastjson.JSONObject;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ：zj
 * @date ：Created in 2020/3/24 10:27
 * @description：异常有两种，登录认证异常和权限认证异常，分别对应的异常类是
 * 登录认证异常：UnauthenticatedException，AuthenticationException
 * 权限认证异常：UnauthorizedException，AuthorizationException
 *
 *
 * https://blog.csdn.net/kity9420/article/details/89199346
 * @modified By：
 * @version: $
 */
public abstract  class BaseController {

    /**
     * 登录认证异常
     *
     * @param request
     * @param response
     * @return
     */
    @ExceptionHandler({ UnauthenticatedException.class, AuthenticationException.class })
    public String authenticationException(HttpServletRequest request, HttpServletResponse response){
        Map<String, Object> map = new HashMap<>();
        map.put("status", "-1000");
        map.put("message", "未登录");
        writeJson(map, response);
        return null;
    }

    /**
     * 权限异常
     *
     * @param request
     * @param response
     * @return
     */
    @ExceptionHandler({ UnauthorizedException.class, AuthorizationException.class })
    public String authorizationException(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", "-1001");
        map.put("message", "无权限");
        writeJson(map, response);
        return null;
    }

    private void writeJson(Map<String, Object> map, HttpServletResponse response) {
        PrintWriter out = null;
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            out = response.getWriter();

            out.write( new JSONObject(map).toString());
        } catch (IOException e) {
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}
