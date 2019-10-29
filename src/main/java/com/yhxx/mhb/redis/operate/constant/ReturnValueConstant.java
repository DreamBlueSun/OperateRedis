package com.yhxx.mhb.redis.operate.constant;

/**
 * @author MaoHangBin
 * @description 提示信息
 * @date 2019/10/23 15:34
 */
public interface ReturnValueConstant {

    /**
     * 操作成功
     */
    String SUCCESS = "成功";
    /**
     * 操作异常
     */
    String OPERATION_EXCEPTION = "操作异常";
    /**
     * 操作异常
     */
    String CONNECT_SUCCESS = "连接成功";
    /**
     * 操作异常
     */
    String CONNECT_FAILD = "连接失败";
    /**
     * 当前key不存在
     */
    String THE_KEY_IS_NULL = "当前key不存在";

}
