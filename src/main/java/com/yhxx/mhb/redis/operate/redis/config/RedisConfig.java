package com.yhxx.mhb.redis.operate.redis.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfig {
    @Value("${redis.host}")
    private String host;
    @Value("${redis.port}")
    private int port;
    @Value("${redis.timeout}")
    private int timeout;
//    @Value("${redis.password}")
//    private String password;

    @Bean
    public JedisPool jedisPool(JedisPoolConfig config) {
        JedisPool jedisPool = null;
//        if (password != null) {
//            jedisPool = new JedisPool(config, host, port, timeout, password);
//        } else {
            jedisPool = new JedisPool(config, host, port, timeout);
//        }
        return jedisPool;
    }

    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(1024);
        jedisPoolConfig.setMinIdle(100);
        jedisPoolConfig.setMaxTotal(2048);
        jedisPoolConfig.setMaxWaitMillis(timeout);
        return jedisPoolConfig;
    }
}
