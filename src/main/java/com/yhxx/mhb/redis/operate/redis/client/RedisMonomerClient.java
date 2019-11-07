package com.yhxx.mhb.redis.operate.redis.client;

import com.alibaba.fastjson.JSON;
import com.yhxx.mhb.redis.operate.constant.RedisConstant;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class RedisMonomerClient {

    public static Set<String> keys(Jedis jedis) {
        return jedis.keys("*");
    }
    
    public static String getByType(String key, Jedis jedis) {
        String result;
        String type = jedis.type(key);
        switch (type) {
            case RedisConstant.DATA_TYPE_STRING:
                result = get(key, jedis);
                break;
            case RedisConstant.DATA_TYPE_HASH:
                result = hGetAll(key, jedis);
                break;
            case RedisConstant.DATA_TYPE_LIST:
                result = lRange(key, jedis);
                break;
            case RedisConstant.DATA_TYPE_SET:
                result = sMembers(key, jedis);
                break;
            case RedisConstant.DATA_TYPE_ZSET:
                result = zRange(key, jedis);
                break;
            default:
                result = null;
                break;
        }
        return result;
    }

    private static String get(String key, Jedis jedis) {
        return jedis.get(key);
    }

    private static String hGetAll(String key, Jedis jedis) {
        Map<String, String> stringMap = jedis.hgetAll(key);
        return JSON.toJSONString(stringMap);
    }

    private static String lRange(String key, Jedis jedis) {
        List<String> stringList = jedis.lrange(key, 0, -1);
        return JSON.toJSONString(stringList);
    }

    private static String sMembers(String key, Jedis jedis) {
        Set<String> stringSet = jedis.smembers(key);
        return JSON.toJSONString(stringSet);
    }

    private static String zRange(String key, Jedis jedis) {
        Set<String> stringZSet = jedis.zrange(key, 0, -1);
        return JSON.toJSONString(stringZSet);
    }

    public static Long del(String key, Jedis jedis) {
        return jedis.del(key);
    }

}
