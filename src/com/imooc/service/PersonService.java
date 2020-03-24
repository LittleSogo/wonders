package com.imooc.service;

import java.util.List;

import com.imooc.model.People;
import com.imooc.model.TPerson;

public interface PersonService {

	/**
	 * 数据列表
	 * @return
	 */
	public List<TPerson> getAll();

	/**
	 * 分页查询
	 * @param tPerson
	 * @return
	 */
	public List<TPerson> findByPage(TPerson tPerson);



	public List<People> getAllList();

	public List<TPerson> getPaginationList(String sql,int firstRow, int rowPerPage);
}
