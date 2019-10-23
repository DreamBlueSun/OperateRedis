package com.yhxx.mhb.demo.Controller;

import com.yhxx.mhb.demo.constant.ReturnValueConstant;
import com.yhxx.mhb.demo.redis.client.RedisClient;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

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
    private RedisClient redisClient;

    /**
     * @param key
     * @Author MaoHangBin
     * @Description 获取key对应的值
     * @Date 2019/7/9 17:22
     * @Return java.lang.String
     **/
    @RequestMapping("/getValue")
    public String getValue(String key) {
        if (StringUtils.isBlank(key)) {
            return ReturnValueConstant.THE_KEY_IS_NULL;
        }
        Jedis jedis = redisClient.getJedis();
        String value = redisClient.get(key, jedis);

        return value == null ? ReturnValueConstant.THE_KEY_IS_NULL : value;
    }

}
