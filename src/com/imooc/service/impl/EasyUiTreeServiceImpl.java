package com.imooc.service.impl;

import com.common.BaseDao;
import com.imooc.service.EasyUiTreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ：zj
 * @date ：Created in 2020/3/17 16:20
 * @description：
 * @modified By：
 * @version: $
 */
@Service
public class EasyUiTreeServiceImpl implements EasyUiTreeService {

    @Autowired
    private BaseDao baseDao;

    @Override
    public List<String> getIdsByParentId(String id) {
        return null;
    }

    public List exemple(){
        return baseDao.findAllByHQL(" from EasyuiTree t");

    }
}
