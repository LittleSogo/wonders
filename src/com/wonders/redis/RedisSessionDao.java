package com.wonders.redis;

import com.wonders.redis.util.JedisUtil;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.SerializationUtils;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author ：zj
 * @date ：Created in 2020/3/24 14:27
 * @description：重写redis crud方法
 * @modified By：
 * @version: $
 * https://blog.csdn.net/u011870280/article/details/60960520?depth_1-utm_source=distribute.pc_relevant.none-task&utm_source=distribute.pc_relevant.none-task
 */
@Component
public class RedisSessionDao extends AbstractSessionDAO {

    private static Logger logger = LoggerFactory.getLogger(RedisSessionDao.class);

    @Resource
    private JedisUtil jedisUtil;

    private final String SHIRO_SESSION_PREFIX = "imooc:session:";

    private byte[] getKey(String key){
        return (SHIRO_SESSION_PREFIX+key).getBytes();
    }
    private void saveSession(Session session){
        if(session!=null &&session.getId()!=null){
            byte[] key = getKey(session.getId().toString());

            byte[] value = SerializationUtils.serialize(session);

            //设置值
            jedisUtil.set(key,value);
            //设置超时时间
            jedisUtil.expire(key,600);
        }

    }

    @Override
    protected Serializable doCreate(Session session) {

        System.out.println("==========>>session read <<===========");

        Serializable serializable = generateSessionId(session);
        //将session 和sessionid 进行捆绑
        assignSessionId(session,serializable);

        saveSession(session);

        return serializable;
    }

    @Override
    protected Session doReadSession(Serializable serializable) {
        if (serializable ==null){
            return null;
        }
        byte[] key = getKey(serializable.toString());
        byte[] value = jedisUtil.get(key);

        return (Session) SerializationUtils.deserialize(value);
    }

    @Override
    public void update(Session session) throws UnknownSessionException {
        saveSession(session);
    }

    @Override
    public void delete(Session session) {
        if(session!=null && session.getId()!=null){
            byte[] key = getKey(session.getId().toString());

            jedisUtil.del(key);
        }

    }

    /**
     * 获取s所有存活的session
     * @return
     */
    @Override
    public Collection<Session> getActiveSessions() {

       Set<byte[]> keys = jedisUtil.getKeys(SHIRO_SESSION_PREFIX);

       Set<Session> sessions = new HashSet<Session>();
       if(CollectionUtils.isEmpty(keys)){
            return sessions;
       }
       //遍历所有的key值
       for (byte[] key:keys){
           Session session = (Session) SerializationUtils.deserialize(jedisUtil.get(key));
           sessions.add(session);
       }
        return sessions;
    }
}
