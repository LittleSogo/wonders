package com.imooc.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imooc.dao.DemoDao;
import com.imooc.model.People;
import com.imooc.service.DemoService;
/**
 * service层 需整体添加@Transactional
 * @author zj
 *
 */

@Service
@Transactional
public class DemoServiceImpl implements DemoService {
	
	@Autowired
	private DemoDao demoDao;

	@Override
	public void save(People p) {
		demoDao.saveOrUpdate(p);
	}

	@Override
	public People findByNameAndPassword(People people) {
		
		return demoDao.findByNameAndPassword(people);
	}

	@Override
	public void del(Integer id) {
		// TODO Auto-generated method stub
		demoDao.del(id);
	}

	@Override
	public People findById(Integer id) {
		// TODO Auto-generated method stub
		return demoDao.findById(id);
	}

	@Override
	public List<People> findListBySql(String sql) {
		// TODO Auto-generated method stub
		return demoDao.findListBySql(sql);
	}
	
	
	

}
