package com.yhxx.mhb.redis.operate.Controller;

import com.yhxx.mhb.redis.operate.constant.ReturnValueConstant;
import com.yhxx.mhb.redis.operate.redis.client.RedisHelper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @PackageName: com.yhxx.mhb.demo.Controller
 * @Description:
 * @auther: maohangbin
 * @Date: 2019/7/9 17:03
 */

@RestController
@RequestMapping("/operateRedis")
public class OperateRedisController {

    @Autowired
    private RedisHelper redisHelper;

    @Autowired
    private JedisPoolConfig jedisPoolConfig;

    public String connectRedis(String host, int port, String password) {
        try {
            if (StringUtils.isBlank(password)) {
                redisHelper.checkJedisPool(jedisPoolConfig, host, port);
            } else {
                redisHelper.checkJedisPool(jedisPoolConfig, host, port, password);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "ok";
    }

    /**
     * @param key
     * @Author MaoHangBin
     * @Description 获取key对应的值
     * @Date 2019/7/9 17:22
     * @Return java.lang.String
     **/
    @RequestMapping("/get")
    public String get(String key, String host, int port) {
        if (StringUtils.isBlank(key)) {
            return ReturnValueConstant.THE_KEY_IS_NULL;
        }
        String value = null;
        Jedis jedis = null;
        try {
            jedis = redisHelper.getJedis(jedisPoolConfig, host, port);
            value = redisHelper.get(key, jedis);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            redisHelper.closeJedis(jedis);
        }
        return value == null ? ReturnValueConstant.THE_KEY_IS_NULL : value;
    }


}
