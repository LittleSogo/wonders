package com.common.impl;

import com.common.BaseDao;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author ：zj
 * @date ：Created in 2020/3/17 14:14
 * @description：通用dao类
 * @modified By：
 * @version: $
 *
 * 参考：https://www.cnblogs.com/sharpest/p/5865732.html
 */
@Transactional
@Repository
public class BaseDaoImpl implements BaseDao {
    @Autowired
    public SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    public void addObject(Object object) {
        sessionFactory.getCurrentSession().save(object);
    }

    public List findAllByHQL(String hql) {
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        return query.list();
    }

    public List findAllByHQL(String hql, Object[] args) {
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        for (int i = 0; i < args.length; i++) {
            System.out.println(args[i]);
            query.setParameter(i, args[i]);
        }
        return query.list();
    }


    public Object findObjectByHQL(String hql) {
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        List list = query.list();
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    public Object findObjectByHQL(String hql, Object[] args) {
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        System.out.println(args[0]);
        for (int i = 0; i < args.length; i++) {
            System.out.println(args[i]);
            query.setParameter(i, args[i]);
        }
        List list = query.list();
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    public List findPage(final String hql,final int page,final int size,final Object[] args) {
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        for (int i = 0; i < args.length; i++) {
            query.setParameter(i, args[i]);
        }
        query.setMaxResults(size);
        query.setFirstResult(page);
        List<Object> list = query.list();
        return list;
    }

    public List findPage(final String hql, final int page, final int size) {
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setMaxResults(size);
        query.setFirstResult(page);
        List<Object> list = query.list();
        return list;
    }

    public void delObject(Object object) {
        sessionFactory.getCurrentSession().delete(object);
    }


    public void updateObject(Object object) {
        sessionFactory.getCurrentSession().update(object);
    }


    public void updateObjectByHQL(String hql) {
        sessionFactory.getCurrentSession().createQuery(hql).executeUpdate();
    }


    public Object findObjectBySQL(String sql) {
        Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);

        List list = query.list();
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    public List findAllBySql(String sql) {
        Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
        return query.list();
    }

    public void updateObjectByHQL(String hql, Object[] params) {
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        for (int i = 0; i < params.length; i++) {
            query.setParameter(i, params[i]);
        }
        query.executeUpdate();
    }
}
