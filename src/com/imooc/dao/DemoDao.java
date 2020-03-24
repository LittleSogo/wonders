package com.imooc.dao;


import org.hibernate.Query;
import org.springframework.stereotype.Repository;


import com.base.BaseDao;
import com.imooc.model.People;
/**
 * 继承一个通用的baseDao
 * @author zj
 * 
 *
 */
@Repository
public class DemoDao extends BaseDao<People, Integer>{
	
	/**
	 * 自行实现的方法
	 * @param People
	 * @return
	 */
	public People findByNameAndPassword(People People){
		String hql = "from People where name = ? and sex = ?";
		Query query = getSession().createQuery(hql);
		query.setParameter(0, People.getName());
		query.setParameter(1, People.getSex());
		return (People) query.uniqueResult();
	}
}
