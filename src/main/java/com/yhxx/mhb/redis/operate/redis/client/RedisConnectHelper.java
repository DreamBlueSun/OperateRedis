package com.yhxx.mhb.redis.operate.redis.client;

import com.yhxx.mhb.redis.operate.redis.config.RedisConfig;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @description:
 * @auther: MaoHangBin
 * @date: 2019/10/29 9:22
 */

@Component
public class RedisConnectHelper {

    @Autowired
    private RedisConfig redisConfig;

    @Autowired
    private JedisPoolConfig jedisPoolConfig;

    private JedisPool jedisPool;

    private final String PONG = "PONG";

    public boolean connect(String host, int port, String password) {
        setJedisPool(host, port, password);
        Jedis jedis = null;
        try {
            jedis = getJedis();
            String pong = jedis.ping();
            if (StringUtils.equals(pong, PONG)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeJedis(jedis);
        }
        return false;
    }

    private void setJedisPool(String host, int port, String password) {
        if (StringUtils.isBlank(password)) {
            jedisPool = new JedisPool(jedisPoolConfig, host, port, redisConfig.getTimeout());
        } else {
            jedisPool = new JedisPool(jedisPoolConfig, host, port, redisConfig.getTimeout(), password);
        }
    }

    public Jedis getJedis() {
        return jedisPool.getResource();
    }

    public void closeJedis(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }

    public void disconnect() {
        closeJedisPool();
    }

    private void closeJedisPool() {
        if (jedisPool != null) {
            jedisPool.close();
        }
    }

}
