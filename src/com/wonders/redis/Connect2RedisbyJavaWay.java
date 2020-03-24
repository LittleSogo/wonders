package com.wonders.redis;

import redis.clients.jedis.Jedis;

/**
 * @author ：zj
 * @date ：Created in 2020/3/24 14:46
 * @description：Java方式连接redis
 * @modified By：
 * @version: $
 */
public class Connect2RedisbyJavaWay {
    /**
     * 连接redis服务器
     */
    public static void connection(){
        // 连接本地的 Redis 服务
        Jedis jedis = new Jedis("localhost");
        System.out.println("连接本地的 Redis 服务成功！");
        // 查看服务是否运行
        System.out.println("服务 正在运行: " + jedis.ping());
    }

    public static void main(String[] args) {
        connection();
    }


}
