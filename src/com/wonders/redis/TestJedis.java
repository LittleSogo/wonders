package com.wonders.redis;

import com.wonders.redis.util.JedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class TestJedis {
    public static final Logger logger = LoggerFactory.getLogger(TestJedis.class);
    // Jedispool
    JedisCommands jedisCommands;


    public TestJedis() {
        jedisCommands = JedisUtil.getJedis();
    }

    public void setValue(String key, String value) {
        this.jedisCommands.set(key, value);
    }

    public String getValue(String key) {
        return this.jedisCommands.get(key);
    }

    public static void main(String[] args) {
        TestJedis testJedis = new TestJedis();
        testJedis.setValue("testJedisKey", "testJedisValue");
        logger.info("get value from redis:{}",testJedis.getValue("testJedisKey"));
    }

}
