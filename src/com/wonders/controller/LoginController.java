package com.wonders.controller;

import com.wonders.dao.TUser;
import com.wonders.service.AccountService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ：zj
 * @date ：Created in 2020/3/17 14:39
 * @description：
 * @modified By：
 * @version: $
 */
@Controller
public class LoginController {
    @Autowired
    private AccountService accountService;

    // 添加slf4j日志实例对象
    final static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping("/toLogin")
    public String toLogin(){
        logger.info("==============>>用户登录操作info<<===============");
        logger.warn("==========>>用户登录操作warn<<=====================");
        logger.debug("==============>>用户登录操作debug<<===============");
        return "login";
    }

    /***
     * 实现用户登录
     *
     * @param username
     * @param password
     * @return
     */
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public ModelAndView Login(String username, String password) {
        ModelAndView mav = new ModelAndView();
        TUser user = accountService.getUserByUserName(username);
        if (user == null) {
            //mav.setView("toLogin");
            mav.addObject("msg", "用户不存在");
            return mav;
        }
        if (!user.getPassword().equals(password)) {
            //mav.setView("toLogin");
            mav.addObject("msg", "帐号密码错误");
            return mav;
        }
        SecurityUtils.getSecurityManager().logout(SecurityUtils.getSubject());
        // 登录后存放进shiro token
        UsernamePasswordToken token = new UsernamePasswordToken(
                user.getUserName(), user.getPassword());
        Subject subject = SecurityUtils.getSubject();

        boolean vip = subject.hasRole("vip");
        System.out.println("========>>"+vip+"<<===========>>"+subject.hasRole("p"));
        subject.login(token);
        // 登录成功后会跳转到successUrl配置的链接，不用管下面返回的链接。
        //mav.setView("redirect:/home");
        mav.setViewName("redirect:/home");
        return mav;
    }

    @RequestMapping("/home")
    public String index() {
        System.out.println("===================>>登录成功<<==============");
        return "demo3";
    }

    @RequestMapping("/403")
    @ResponseBody
    public Object unauthorizedUrl(){
        return "unauthorizedUrl";
    }


    @RequestMapping("")
    @ResponseBody
    public Object result(){
        List<Object> child = new ArrayList<Object>();

       Map<String,String> childMap= new HashMap<String, String>();
        childMap.put("text","党员管理");
        childMap.put("url","https://localhost:8087/easyui/partyMember/getPartyMemberList");


        Map<String,String> childMap2= new HashMap<String, String>();
        childMap2.put("text","组织生活管理");
        childMap2.put("url","https://localhost:8087/easyui/partyMember/getPartyMemberList");

        child.add(childMap);
        Map<String,Object> map = new HashMap<String, Object>();

        map.put("text","党务管理");
        map.put("children",child);

        return map;
    }


}
