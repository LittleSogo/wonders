package com.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;


import java.lang.reflect.ParameterizedType;  
import java.lang.reflect.Type;
import java.util.Map;

/**
 * dao层通用方法
 * 参考：https://www.iteye.com/blog/meigesir-1483806
 * @author zj
 *
 */
public class BaseDao<T,PK extends Serializable> {
	
	 private SessionFactory sessionFactory;  
	    //Class<?>:防止报黄线,意思是传入任意类型都行,无所谓,也可以使用Object  
	    private Class<?> entityClass;  
	      
	    public BaseDao(){  
	        //new出一个子类对象，父类中的this是子类中的this  
	        entityClass = getGenericParameterType(this.getClass());  
	    } 
	    
	    
	    //get  
	    public Session getSession(){  
	        return sessionFactory.getCurrentSession();  
	    }  
	      
	    //set  
	    @Autowired  
	    public void setSessionFactory(SessionFactory sessionFactory) {  
	        this.sessionFactory = sessionFactory;  
	    }  
	      
	    public void saveOrUpdate(T t){  
	        getSession().saveOrUpdate(t);  
	    }  
	      
	    public void del(PK pk){  
	        getSession().delete(findById(pk));  
	    }  
	      
	    public void del(T t){  
	        getSession().delete(t);  
	    }  
	      
	    @SuppressWarnings("unchecked")  
	    public T findById(PK pk){  
	        return (T) getSession().get(entityClass, pk);  
	    }

	/**
	 * 查询所有数据
	 * @return 数据列表
	 */
	@SuppressWarnings("unchecked")
	    public List<T> findAll(){  
	        Criteria criteria = getSession().createCriteria(entityClass);  
	        return criteria.list();  
	    }

	/**
	 * 获取所有条数
	 * @return
	 */
	public int getTotalNum(){
	    	return this.findAll().size();
		}

	/**
	 * hql方式查询数据
	 * @param map
	 * @param start
	 * @param count
	 * @return 数据列表
	 */
	public List<T> findByPage(Map map, int start, int count){
	        Criteria criteria = getSession().createCriteria(entityClass);
			//criteria.add(Restrictions.eq("",""));
	        criteria.setFirstResult(start);  
	        criteria.setMaxResults(count);  
	        return criteria.list();  
	}

	/**
	 * 根据sql 查询数据
	 * @param sql
	 * @return 数据集合
	 */
	public List<T> findListBySql(String sql){
	    	 List<T> arrayList = new ArrayList<T>();
	    	    SQLQuery q = getSession().createSQLQuery(sql);
	    	    List<Object[]> queryList = q.list();
	    	    for (Object[] o : queryList) {
	    	     T t =  findById((PK) o[0]);
	    	      arrayList.add(t);
	    	    }
			return arrayList;
	    	
	    }

	/**
	 * sql 语句分页查询
	 * @param sql
	 * @param firstRow
	 * @param rowPerPage
	 * @return
	 */
	public List<T> getPaginationList(String sql,int firstRow, int rowPerPage){
		SQLQuery sqlQuery = getSession().createSQLQuery(sql.toString()).addEntity(this.entityClass);
		sqlQuery.setFirstResult(firstRow);
		sqlQuery.setMaxResults(rowPerPage);

		return sqlQuery.list();

	}
	    
	    /*
	     * 通过反射实现返回的实际参数化类型T
	     */
	    public static Class<?> getGenericParameterType(Class<?> clazz){  
	        //返回表示此Class所表示的实体（类、接口、基本类型或 void）的直接超类的Type。如果超类是参数化类型，则返回的Type对象必须准确反映源代码中所使用的实际类型参数。  
	        Type type = clazz.getGenericSuperclass();  
	        //强制转化为参数化类型：Collection<String>  
	        ParameterizedType pt = (ParameterizedType) type;  
	        //返回表示此类型的实际类型参数的 Type 对象的数组。   
	        Type[] types = pt.getActualTypeArguments();  
	        return (Class<?>) types[0];  
	    }  
	   

}
