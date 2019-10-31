package com.yhxx.mhb.redis.operate.redis.client;

import com.yhxx.mhb.redis.operate.redis.annotation.SkipRedis;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
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

    public static String set(String key, String value, JedisCluster jedisCluster) {
        return jedisCluster.set(key, value);
    }

    public static String set(String key, String value, String nxxx, String expx, long time, JedisCluster jedisCluster) {
        return jedisCluster.set(key, value, nxxx, expx, time);
    }

    public static String get(String key, JedisCluster jedisCluster) {
        return jedisCluster.get(key);
    }

    public static Long hSet(String key, String field, String value, JedisCluster jedisCluster) {
        return jedisCluster.hset(key, field, value);
    }

    public static String hSet(String key, Object object, JedisCluster jedisCluster) {
        Class<?> aClass = object.getClass();
        Field[] fields = aClass.getDeclaredFields();
        for (Field field : fields) {
            //只要变量上面有这个注解,就略过
            SkipRedis annotation = field.getAnnotation(SkipRedis.class);
            if (annotation != null) {
                continue;
            }
            //变量的名字,也就是我们等会向redis中放的fieldname
            String name = field.getName();
            try {
                //在指定的类中查找执行的属性名
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(name, aClass);
                if (propertyDescriptor != null) {
                    //获取get方法
                    Method readMethod = propertyDescriptor.getReadMethod();
                    if (readMethod != null) {
                        Object result = readMethod.invoke(object);
                        if (result != null) {
                            hSet(key, name, result.toString(), jedisCluster);
                        }
                    }
                }
            } catch (IntrospectionException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String hGet(String key, String filed, JedisCluster jedisCluster) {
        return jedisCluster.hget(key, filed);
    }

    public static <T> T hGet(String key, Class<T> clazz, JedisCluster jedisCluster) {
        //创建对象
        T t = null;
        try {
            t = clazz.newInstance();
            //遍历属性
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                //根据属性名去redis中找值
                //按照我们的存的规则.这个就是redis只给中hash的field
                String fieldName = field.getName();
                String result = hGet(key, fieldName, jedisCluster);
                if (result != null) {
                    //找到值就赋值进去
                    //在指定的类中查找执行的属性名
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, clazz);
                    if (propertyDescriptor != null) {
                        Method writeMethod = propertyDescriptor.getWriteMethod();
                        if (writeMethod != null) {
                            writeMethod.invoke(t, result);
                        }
                    }
                }
            }

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IntrospectionException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return t;
    }

    public static Long expire(String key, int seconds, JedisCluster jedisCluster) {
        return jedisCluster.expire(key, seconds);
    }

    public static Long del(String key, JedisCluster jedisCluster) {
        return jedisCluster.del(key);
    }

    public static Long hDel(String key, String[] fields, JedisCluster jedisCluster) {
        return jedisCluster.hdel(key, fields);
    }

}
