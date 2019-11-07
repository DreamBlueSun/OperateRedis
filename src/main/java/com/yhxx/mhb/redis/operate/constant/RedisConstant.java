package com.yhxx.mhb.redis.operate.constant;

/**
 * @description:
 * @auther: MaoHangBin
 * @date: 2019/10/31 10:13
 */

public interface RedisConstant {

    /**
     * Redis类型：关闭
     */
    int REDIS_TYPE_CLOSE = 0;

    /**
     * Redis类型：单体
     */
    int REDIS_TYPE_MONOMER = 1;

    /**
     * Redis类型：集群
     */
    int REDIS_TYPE_CLUSTER = 2;

    /**
     * Redis数据类型：String
     */
    String DATA_TYPE_STRING = "string";

    /**
     * Redis数据类型：hash
     */
    String DATA_TYPE_HASH = "hash";

    /**
     * Redis数据类型：list
     */
    String DATA_TYPE_LIST = "list";

    /**
     * Redis数据类型：set
     */
    String DATA_TYPE_SET = "set";

    /**
     * Redis数据类型：zset
     */
    String DATA_TYPE_ZSET = "zset";

}
