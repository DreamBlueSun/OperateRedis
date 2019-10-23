package com.yhxx.mhb.redis.operate.redis.client;

import redis.clients.jedis.Jedis;

public interface RedisClientInterface {
    Jedis getJedis();

    void closeJedis(Jedis jedis);

    String set(String key, String value, Jedis jedis);

    String set(String key, String value, String nxxx, String expx, long time, Jedis jedis);

    String get(String key, Jedis jedis);

    Long hSet(String key, String field, String value, Jedis jedis);

    String hSet(String key, Object object, Jedis jedis);

    String hGet(String key, String filed, Jedis jedis);

    <T> T hGet(String key, Class<T> clazz, Jedis jedis);

    Long expire(String key, int seconds, Jedis jedis);

    Long del(String key, Jedis jedis);

    Long hDel(String key, String[] fields, Jedis jedis);
}
