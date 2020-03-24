package com.imooc.service.impl;

import java.util.List;

import com.imooc.model.People;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imooc.dao.PersonDao;
import com.imooc.model.TPerson;
import com.imooc.service.PersonService;

@Service
@Transactional
public class PersonServiceImpl implements PersonService {

	
	@Autowired
	private PersonDao personDao;





	@Override
	public List<TPerson> getAll() {
		// TODO Auto-generated method stub
		return personDao.findAll();
	}

	@Override
	public List<TPerson> findByPage(TPerson tPerson) {
		return personDao.findByPage(null,tPerson.getOffset(),tPerson.getPageNumber());
	}

	public List<People> getAllList(){

		return null;
	}

	@Override
	public List<TPerson> getPaginationList(String sql, int firstRow, int rowPerPage) {
		return personDao.getPaginationList(sql,firstRow,rowPerPage);
	}


}
