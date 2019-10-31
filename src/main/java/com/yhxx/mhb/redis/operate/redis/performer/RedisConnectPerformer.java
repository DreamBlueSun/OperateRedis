package com.yhxx.mhb.redis.operate.redis.performer;

import com.yhxx.mhb.redis.operate.constant.RedisConstant;
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
public class RedisConnectPerformer {

    @Autowired
    private RedisConfig redisConfig;

    @Autowired
    private JedisPoolConfig jedisPoolConfig;

    private JedisPool jedisPool;

    private JedisCluster jedisCluster;

    private int type = RedisConstant.REDIS_TYPE_CLOSE;

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
        try {
            type = RedisConstant.REDIS_TYPE_MONOMER;
            if (StringUtils.isBlank(password)) {
                jedisPool = new JedisPool(jedisPoolConfig, host, port, redisConfig.getTimeout());
            } else {
                jedisPool = new JedisPool(jedisPoolConfig, host, port, redisConfig.getTimeout(), password);
            }
        } catch (Exception e) {
            type = RedisConstant.REDIS_TYPE_CLOSE;
        }
    }

    private void setJedisCluster(String host, int port, String password) {
        try {
            type = RedisConstant.REDIS_TYPE_CLUSTER;
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
        } catch (Exception e) {
            type = RedisConstant.REDIS_TYPE_CLOSE;
        }
    }

    private final String TEST_CONNECTIVITY = "TEST_CONNECTIVITY";

    private boolean jedisConnectedSuccess() {
        if (type != RedisConstant.REDIS_TYPE_CLOSE) {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                jedis.get(TEST_CONNECTIVITY);
                return true;
            } catch (Exception e) {
                closeJedisPool();
                return false;
            } finally {
                closeJedis(jedis);
            }
        }
        return false;
    }

    private boolean jedisCluderConnectedSuccess() {
        if (type != RedisConstant.REDIS_TYPE_CLOSE) {
            try {
                jedisCluster.get(TEST_CONNECTIVITY);
                return true;
            } catch (Exception e) {
                closeJedisCluder();
                return false;
            }
        }
        return false;
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
        if (type == RedisConstant.REDIS_TYPE_MONOMER) {
            closeJedisPool();
        }
        if (type == RedisConstant.REDIS_TYPE_CLUSTER) {
            closeJedisCluder();
        }
    }

    private void closeJedisCluder() {
        if (jedisCluster != null) {
            type = RedisConstant.REDIS_TYPE_CLOSE;
            try {
                jedisCluster.close();
            } catch (IOException e) {
                jedisCluster = null;
            }
        }
    }

    private void closeJedisPool() {
        if (jedisPool != null) {
            type = RedisConstant.REDIS_TYPE_CLOSE;
            jedisPool.close();
        }
    }

}
