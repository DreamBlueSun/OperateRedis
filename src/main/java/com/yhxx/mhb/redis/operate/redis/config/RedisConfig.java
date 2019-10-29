package com.yhxx.mhb.redis.operate.redis.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfig {

    @Value("${redis.maxIdle}")
    private int maxIdle;

    @Value("${redis.minIdle}")
    private int minIdle;

    @Value("${redis.maxTotal}")
    private int maxTotal;

    @Value("${redis.timeout}")
    private int timeout;

    @Value("${jedisCluder.connectionTimeout}")
    private int connectionTimeout;

    @Value("${jedisCluder.soTimeout}")
    private int soTimeout;

    @Value("${jedisCluder.maxAttempts}")
    private int maxAttempts;

    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMinIdle(minIdle);
        jedisPoolConfig.setMaxTotal(maxTotal);
        jedisPoolConfig.setMaxWaitMillis(timeout);
        jedisPoolConfig.setTestOnBorrow(true);
        return jedisPoolConfig;
    }

    public int getTimeout() {
        return timeout;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public int getSoTimeout() {
        return soTimeout;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }
}
