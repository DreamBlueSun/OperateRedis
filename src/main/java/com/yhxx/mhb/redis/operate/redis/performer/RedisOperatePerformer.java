package com.yhxx.mhb.redis.operate.redis.performer;

import com.yhxx.mhb.redis.operate.constant.RedisConstant;
import com.yhxx.mhb.redis.operate.constant.ReturnValueConstant;
import com.yhxx.mhb.redis.operate.redis.client.RedisClusterClient;
import com.yhxx.mhb.redis.operate.redis.client.RedisMonomerClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

/**
 * @description:
 * @auther: MaoHangBin
 * @date: 2019/10/31 10:27
 */

@Component
public class RedisOperatePerformer {

    @Autowired
    private RedisConnectPerformer connectPerformer;

    public String set(String key, String value) {
        return ReturnValueConstant.DEVELOPING;
    }

    public String set(String key, String value, String nxxx, String expx, long time, Jedis jedis) {
        return ReturnValueConstant.DEVELOPING;
    }

    public String get(String key) {
        String result = ReturnValueConstant.OPERATION_EXCEPTION;
        int type = connectPerformer.getType();
        if (type == RedisConstant.REDIS_TYPE_MONOMER) {
            Jedis jedis = connectPerformer.getJedis();
            try {
                result = RedisMonomerClient.get(key, jedis);
            } catch (Exception e) {
                result = ReturnValueConstant.OPERATION_EXCEPTION;
            } finally {
                connectPerformer.closeJedis(jedis);
            }
        }
        if (type == RedisConstant.REDIS_TYPE_CLUSTER) {
            JedisCluster jedisCluster = connectPerformer.getJedisCluster();
            try {
                result = RedisClusterClient.get(key, jedisCluster);
            } catch (Exception e) {
                result = ReturnValueConstant.OPERATION_EXCEPTION;
            }
        }
        return result;
    }

    public String expire(String key, int seconds, Jedis jedis) {
        return ReturnValueConstant.DEVELOPING;
    }

    public String del(String key) {
        String result = ReturnValueConstant.OPERATION_EXCEPTION;
        int type = connectPerformer.getType();
        if (type == RedisConstant.REDIS_TYPE_MONOMER) {
            Jedis jedis = connectPerformer.getJedis();
            try {
                RedisMonomerClient.del(key, jedis);
                result = ReturnValueConstant.SUCCESS;
            } catch (Exception e) {
                result = ReturnValueConstant.OPERATION_EXCEPTION;
            } finally {
                connectPerformer.closeJedis(jedis);
            }
        }
        if (type == RedisConstant.REDIS_TYPE_CLUSTER) {
            JedisCluster jedisCluster = connectPerformer.getJedisCluster();
            try {
                RedisClusterClient.del(key, jedisCluster);
                result = ReturnValueConstant.SUCCESS;
            } catch (Exception e) {
                result = ReturnValueConstant.OPERATION_EXCEPTION;
            }
        }
        return result;
    }

    public String hDel(String key, String[] fields, Jedis jedis) {
        return ReturnValueConstant.DEVELOPING;

    }

}
