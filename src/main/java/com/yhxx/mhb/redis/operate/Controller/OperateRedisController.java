package com.yhxx.mhb.redis.operate.Controller;

import com.yhxx.mhb.redis.operate.constant.ReturnValueConstant;
import com.yhxx.mhb.redis.operate.redis.client.RedisConnectHelper;
import com.yhxx.mhb.redis.operate.redis.client.RedisOperationHelper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

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
    private RedisConnectHelper connectHelper;

    /**
     * @param host
     * @param port
     * @param password
     * @return java.lang.String
     * @author MaoHangBin
     * @description 连接到redis
     * @date 2019/10/29 10:06
     **/
    @RequestMapping("/connect/{host}/{port}/{password}")
    public String connect(@PathVariable String host, @PathVariable int port, @PathVariable String password) {
        boolean connect = connectHelper.connect(host, port, password);
        if (connect) {
            return ReturnValueConstant.CONNECT_SUCCESS;
        }
        return ReturnValueConstant.CONNECT_FAILD;
    }

    @RequestMapping("/connect/{host}/{port}")
    public String connect(@PathVariable String host, @PathVariable int port) {
        boolean connect = connectHelper.connect(host, port, null);
        if (connect) {
            return ReturnValueConstant.CONNECT_SUCCESS;
        }
        return ReturnValueConstant.CONNECT_FAILD;
    }

    @RequestMapping("/connectCluster/{host}/{port}")
    public String connectCluster(@PathVariable String host, @PathVariable int port) {
//        boolean connect = connectHelper.connectCluster(host, port, null);
//        if (connect) {
//            return ReturnValueConstant.CONNECT_SUCCESS;
//        }
        return ReturnValueConstant.CONNECT_FAILD;
    }

    /**
     * @return java.lang.String
     * @author MaoHangBin
     * @description 断开redis连接
     * @date 2019/10/29 10:10
     **/
    @RequestMapping("/disconnect")
    public String disconnect() {
        connectHelper.disconnect();
        return ReturnValueConstant.SUCCESS;
    }

    /**
     * @param key
     * @Author MaoHangBin
     * @Description 获取key对应的值
     * @Date 2019/7/9 17:22
     * @Return java.lang.String
     **/
    @RequestMapping("/get/{key}")
    public String get(@PathVariable String key) {
        if (StringUtils.isBlank(key)) {
            return ReturnValueConstant.THE_KEY_IS_NULL;
        }
        int type = connectHelper.getType();
        if (type == 1) {
            Jedis jedis = connectHelper.getJedis();
            try {
                return RedisOperationHelper.get(key, jedis);
            } catch (Exception e) {
                return ReturnValueConstant.OPERATION_EXCEPTION;
            } finally {
                connectHelper.closeJedis(jedis);
            }
        }
        if (type == 2) {
            JedisCluster jedisCluster = connectHelper.getJedisCluster();
            try {
                return jedisCluster.get(key);
            } catch (Exception e) {
                return ReturnValueConstant.OPERATION_EXCEPTION;
            }
        }
        return ReturnValueConstant.OPERATION_EXCEPTION;
    }

}
