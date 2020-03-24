package com.imooc.service;

import java.util.List;

import com.imooc.model.People;

public interface DemoService {
	/*
	 * 保存
	 * @param p
	 */
	 public void save(People p);
	 
	 /*
	  * 根据条件查询
	  */
	 public People findByNameAndPassword(People people);
	 /**
	  * 删除
	  * @param id
	  */
	 public void del(Integer id);
	 
	 
	 public People findById(Integer id);
	 
	 /**
	  * 根据sql语句查询结果
	  * @param sql
	  * @return
	  */
	 public List<People> findListBySql(String sql);

}
