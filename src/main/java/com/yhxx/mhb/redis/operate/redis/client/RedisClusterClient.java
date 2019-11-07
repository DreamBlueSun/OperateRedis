package com.yhxx.mhb.redis.operate.redis.client;

import com.alibaba.fastjson.JSON;
import com.yhxx.mhb.redis.operate.constant.RedisConstant;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class RedisClusterClient {

    public static TreeSet<String> keys(JedisCluster jedisCluster) {
        TreeSet<String> keys = new TreeSet<>();
        Map<String, JedisPool> clusterNodes = jedisCluster.getClusterNodes();
        for (String nodeKey : clusterNodes.keySet()) {
            JedisPool jedisPool = clusterNodes.get(nodeKey);
            Jedis jedis = jedisPool.getResource();
            try {
                keys.addAll(jedis.keys("*"));
            } catch (Exception e) {
            } finally {
                jedis.close();
            }
        }
        return keys;
    }

    public static String getByType(String key, JedisCluster jedisCluster) {
        String result;
        String type = jedisCluster.type(key);
        switch (type) {
            case RedisConstant.DATA_TYPE_STRING:
                result = get(key, jedisCluster);
                break;
            case RedisConstant.DATA_TYPE_HASH:
                result = hGetAll(key, jedisCluster);
                break;
            case RedisConstant.DATA_TYPE_LIST:
                result = lRange(key, jedisCluster);
                break;
            case RedisConstant.DATA_TYPE_SET:
                result = sMembers(key, jedisCluster);
                break;
            case RedisConstant.DATA_TYPE_ZSET:
                result = zRange(key, jedisCluster);
                break;
            default:
                result = null;
                break;
        }
        return result;
    }

    private static String get(String key, JedisCluster jedisCluster) {
        return jedisCluster.get(key);
    }

    private static String hGetAll(String key, JedisCluster jedisCluster) {
        Map<String, String> stringMap = jedisCluster.hgetAll(key);
        return JSON.toJSONString(stringMap);
    }

    private static String lRange(String key, JedisCluster jedisCluster) {
        List<String> stringList = jedisCluster.lrange(key, 0, -1);
        return JSON.toJSONString(stringList);
    }

    private static String sMembers(String key, JedisCluster jedisCluster) {
        Set<String> stringSet = jedisCluster.smembers(key);
        return JSON.toJSONString(stringSet);
    }

    private static String zRange(String key, JedisCluster jedisCluster) {
        Set<String> stringZSet = jedisCluster.zrange(key, 0, -1);
        return JSON.toJSONString(stringZSet);
    }

    public static Long del(String key, JedisCluster jedisCluster) {
        return jedisCluster.del(key);
    }

}
