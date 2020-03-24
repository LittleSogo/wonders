package com.imooc.controller;

import com.imooc.service.EasyUiTreeService;
import common.utils.MWYHandleDaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import wfc.service.database.RecordSet;

/**
 * @author ：zj
 * @date ：Created in 2020/3/17 16:10
 * @description：
 * @modified By：
 * @version: $
 */
@Controller
public class EasyUiTreeController {

    @Autowired
    private EasyUiTreeService easyUiTreeService;

    @RequestMapping("/getAllData")
    @ResponseBody
    public Object getAllData(){
        return  easyUiTreeService.exemple();

    }


}
