/*
package com.wonders.shiro.sso;

*/
/**
 * @author ：zj
 * @date ：Created in 2020/3/24 9:24
 * @description：自定义单点登录过滤器
 * @modified By：
 * @version: $
 *//*


import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.servlet.Cookie;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

*/
/**
 * sso自定义过滤器
 *//*

public class SSOFilter implements Filter {

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            String token = request.getParameter("token");
            PropertiesTool propertiesTool = PropertiesTool.getInstance();
            String ssoServiceUrl = propertiesTool.getValue("ssoServiceUrl");
            String tokenValidateUrl = propertiesTool.getValue("tokenValidateUrl");
            if (null == token) {
                Cookie cookie = CookieUtil.getCookieByName(request, "token");
                if (null != cookie) {
                    token = cookie.getValue();
                }
            }
            if (null == token) {
                //没有token，重定向至sso server登录页
                response.sendRedirect(ssoServiceUrl);
            } else {
                String urlString = request.getRequestURI();
                if (urlString.endsWith("/logout")) {
                    String JedisUrl = propertiesTool.getValue("JedisUrl");
                    String JedisPort = propertiesTool.getValue("JedisPort");
                    Jedis jedis = new Jedis(JedisUrl, Integer.parseInt(JedisPort));
                    jedis.del(token.getBytes());
                    jedis.close();
                    SecurityUtils.getSubject().logout();
                    response.sendRedirect(ssoServiceUrl);//重定向至sso server登录页
                    return;
                }

                //有token,去sso server验证token的有效性
                Map<String, String> map = new HashMap<>();
                map.put("token", token);
                String result = HttpClientUtil.doGet(tokenValidateUrl, map);
                if (StringUtils.isNotBlank(result)) {//验证成功，跳转至首页，并从redis中获取权限
                    SystemSession.setUser(SSOTokenUtil.getToken(request));//设置系统session(把用户信息保存在ThreadLocal中)
                    Cookie cookie = new Cookie("token", token);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                    filterChain.doFilter(request, response);
                } else {
                    response.sendRedirect(ssoServiceUrl);//验证失败，重定向至sso server登录页
                }
            }
        }
    }


    public void destroy() {

    }
}
*/
