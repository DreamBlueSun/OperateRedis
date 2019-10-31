package com.yhxx.mhb.redis.operate.Controller;

import com.yhxx.mhb.redis.operate.constant.ReturnValueConstant;
import com.yhxx.mhb.redis.operate.redis.performer.RedisConnectPerformer;
import com.yhxx.mhb.redis.operate.redis.performer.RedisOperatePerformer;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    private RedisConnectPerformer connectPerformer;

    @Autowired
    private RedisOperatePerformer operatePerformer;

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
        boolean connect = connectPerformer.connect(host, port, password);
        if (connect) {
            return ReturnValueConstant.CONNECT_SUCCESS;
        }
        return ReturnValueConstant.CONNECT_FAILD;
    }

    @RequestMapping("/connect/{host}/{port}")
    public String connect(@PathVariable String host, @PathVariable int port) {
        boolean connect = connectPerformer.connect(host, port, null);
        if (connect) {
            return ReturnValueConstant.CONNECT_SUCCESS;
        }
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
        connectPerformer.disconnect();
        return ReturnValueConstant.SUCCESS;
    }

    /**
     * @param key
     * @Author MaoHangBin
     * @Description 获取指定key的值
     * @Date 2019/7/9 17:22
     * @Return java.lang.String
     **/
    @RequestMapping("/get/{key}")
    public String get(@PathVariable String key) {
        try {
            if (StringUtils.isBlank(key)) {
                return ReturnValueConstant.THE_KEY_IS_NULL;
            }
            return operatePerformer.get(key);
        } catch (Exception e) {
            return ReturnValueConstant.OPERATION_EXCEPTION;
        }
    }

    /**
     * @param key
     * @return java.lang.String
     * @author MaoHangBin
     * @description 删除指定key
     * @date 2019/10/31 10:54
     **/
    @RequestMapping("/del/{key}")
    public String del(@PathVariable String key) {
        try {
            if (StringUtils.isBlank(key)) {
                return ReturnValueConstant.THE_KEY_IS_NULL;
            }
            return operatePerformer.del(key);
        } catch (Exception e) {
            return ReturnValueConstant.OPERATION_EXCEPTION;
        }
    }

}
