package com.yhxx.mhb.redis.operate.redis.client;

import com.yhxx.mhb.redis.operate.redis.config.RedisConfig;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.*;

import java.io.IOException;

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

    private JedisCluster jedisCluster;

    private int type = 0;

    private final String PONG = "PONG";

    private final String TEST_CONNECTIVITY = "TEST_CONNECTIVITY";

    public boolean connect(String host, int port, String password) {
        setJedisPool(host, port, password);
        if (jedisConnectedSuccess()) {
            return true;
        } else {
            setJedisCluster(host, port, password);
            return jedisCluderConnectedSuccess();
        }
    }

    private void setJedisPool(String host, int port, String password) {
        if (StringUtils.isBlank(password)) {
            jedisPool = new JedisPool(jedisPoolConfig, host, port, redisConfig.getTimeout());
        } else {
            jedisPool = new JedisPool(jedisPoolConfig, host, port, redisConfig.getTimeout(), password);
        }
        type = 1;
    }

    private void setJedisCluster(String host, int port, String password) {
        HostAndPort hostAndPort = new HostAndPort(host, port);
        if (StringUtils.isBlank(password)) {
            JedisCluster jedisCluster = new JedisCluster(hostAndPort, redisConfig.getConnectionTimeout(),
                    redisConfig.getSoTimeout(), redisConfig.getMaxAttempts(), jedisPoolConfig);
            this.jedisCluster = jedisCluster;
        } else {
            JedisCluster jedisCluster = new JedisCluster(hostAndPort, redisConfig.getConnectionTimeout(),
                    redisConfig.getSoTimeout(), redisConfig.getMaxAttempts(), password, jedisPoolConfig);
            this.jedisCluster = jedisCluster;
        }
        type = 2;
    }

    private boolean jedisConnectedSuccess() {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            if (StringUtils.equals(jedis.ping(), PONG)) {
                return true;
            } else {
                closeJedisPool();
                return false;
            }
        } catch (Exception e) {
            closeJedisPool();
            return false;
        } finally {
            closeJedis(jedis);
        }
    }

    private boolean jedisCluderConnectedSuccess() {
        try {
            jedisCluster.get(TEST_CONNECTIVITY);
            return true;
        } catch (Exception e) {
            closeJedisCluder();
            return false;
        }
    }

    public int getType() {
        return this.type;
    }

    public Jedis getJedis() {
        return jedisPool.getResource();
    }

    public JedisCluster getJedisCluster() {
        return jedisCluster;
    }

    public void closeJedis(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }

    public void disconnect() {
        if (type == 1) {
            closeJedisPool();
        }
        if (type == 2) {
            closeJedisCluder();
        }
    }

    private void closeJedisCluder() {
        if (jedisCluster != null) {
            type = 0;
            try {
                jedisCluster.close();
            } catch (IOException e) {
                jedisCluster = null;
            }
        }
    }

    private void closeJedisPool() {
        if (jedisPool != null) {
            type = 0;
            jedisPool.close();
        }
    }

}
