package com.wonders.redis.util;

import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.ResourceBundle;
import java.util.Set;

/**
 * @author ：zj
 * @date ：Created in 2020/3/24 14:56
 * @description：redis操作工具类
 * @modified By：
 * @version: $
 */
@Component
public class JedisUtil {

    private static JedisPool jedisPool = null;
    static  {
        //使用ResourceBundle类读取配置文件
        ResourceBundle resourceBundle = ResourceBundle.getBundle("jedis");
        //拿到数据信息
        String host = resourceBundle.getString("jedis.host");
        int port = Integer.parseInt(resourceBundle.getString("jedis.port"));
        int maxTotal = Integer.parseInt(resourceBundle.getString("jedis.maxTotal"));
        int maxIdle = Integer.parseInt(resourceBundle.getString("jedis.maxIdle"));
        //设置配置信息
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMaxTotal(maxTotal);
        //初始化
        jedisPool = new JedisPool(jedisPoolConfig, host, port);
    }
    //获取redis操作对象
    public static Jedis getJedis() {
        return jedisPool.getResource();
    }

    public static void main(String[] args) {
        Jedis jedis = getJedis();

    }

    public byte[] set(byte[] key, byte[] value) {
        Jedis jedis = getJedis();
        try {
            jedis.set(key,value);
            return value;
        } finally {
            jedis.close();
        }


    }

    public void expire(byte[] key, int i) {
        Jedis jedis = getJedis();
        try {
            jedis.expire(key,i);
        } finally {
            jedis.close();
        }

    }

    public byte[] get(byte[] key) {
        Jedis jedis = getJedis();
        try {
           return jedis.get(key);
        } finally {
            jedis.close();
        }

    }

    public void del(byte[] key) {
        Jedis jedis = getJedis();
        try {
            jedis.del(key);
        } finally {
            jedis.close();
        }

    }

    public Set<byte[]> getKeys(String shiro_session_prefix) {
        Jedis jedis = getJedis();
        try {
            return jedis.keys((shiro_session_prefix+"*").getBytes());
        } finally {
            jedis.close();
        }
    }
}
