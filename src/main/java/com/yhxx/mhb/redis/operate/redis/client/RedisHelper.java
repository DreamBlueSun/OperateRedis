package com.yhxx.mhb.redis.operate.redis.client;

import com.yhxx.mhb.redis.operate.redis.annotation.SkipRedis;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Component
public class RedisHelper {

    private JedisPool jedisPool;

    private String lastHost;

    private int lastPort;

    public Jedis getJedis(JedisPoolConfig jedisPoolConfig, String host, int port) {
        checkJedisPool(jedisPoolConfig, host, port);
        return jedisPool.getResource();
    }

    public Jedis getJedis(JedisPoolConfig jedisPoolConfig, String host, int port, String password) {
        checkJedisPool(jedisPoolConfig, host, port, password);
        return jedisPool.getResource();
    }

    public void checkJedisPool(JedisPoolConfig jedisPoolConfig, String host, int port) {
        if (jedisPool == null) {
            setJedisPool(jedisPoolConfig, host, port);
        } else {
            if (!StringUtils.equals(lastHost, host) || lastPort != port) {
                setJedisPool(jedisPoolConfig, host, port);
            }
        }
    }

    public void checkJedisPool(JedisPoolConfig jedisPoolConfig, String host, int port, String password) {
        if (jedisPool == null) {
            setJedisPool(jedisPoolConfig, host, port, password);
        } else {
            if (!StringUtils.equals(lastHost, host) || lastPort != port) {
                setJedisPool(jedisPoolConfig, host, port, password);
            }
        }
    }

    private void setJedisPool(JedisPoolConfig jedisPoolConfig, String host, int port) {
        jedisPool = new JedisPool(jedisPoolConfig, host, port, getTimeOut(jedisPoolConfig));
        lastHost = host;
        lastPort = port;
    }

    private void setJedisPool(JedisPoolConfig jedisPoolConfig, String host, int port, String password) {
        jedisPool = new JedisPool(jedisPoolConfig, host, port, getTimeOut(jedisPoolConfig), password);
        lastHost = host;
        lastPort = port;
    }

    private int getTimeOut(JedisPoolConfig jedisPoolConfig) {
        Long maxWaitMillis = jedisPoolConfig.getMaxWaitMillis();
        return maxWaitMillis.intValue();
    }

    public void closeJedis(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }

    public String set(String key, String value, Jedis jedis) {
        return jedis.set(key, value);
    }

    public String set(String key, String value, String nxxx, String expx, long time, Jedis jedis) {
        return jedis.set(key, value, nxxx, expx, time);
    }

    public String get(String key, Jedis jedis) {
        return jedis.get(key);
    }

    public Long hSet(String key, String field, String value, Jedis jedis) {
        return jedis.hset(key, field, value);
    }

    public String hSet(String key, Object object, Jedis jedis) {
        Class<?> aClass = object.getClass();
        Field[] fields = aClass.getDeclaredFields();
        for (Field field : fields) {
            SkipRedis annotation = field.getAnnotation(SkipRedis.class);//只要变量上面有这个注解,就略过
            if (annotation != null) {
                continue;
            }
            String name = field.getName();//变量的名字,也就是我们等会向redis中放的fieldname
            try {
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(name, aClass);//在指定的类中查找执行的属性名
                if (propertyDescriptor != null) {
                    Method readMethod = propertyDescriptor.getReadMethod();//获取get方法
                    if (readMethod != null) {
                        Object result = readMethod.invoke(object);
                        if (result != null) {
                            hSet(key, name, result.toString(), jedis);
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

    public String hGet(String key, String filed, Jedis jedis) {
        return jedis.hget(key, filed);
    }

    public <T> T hGet(String key, Class<T> clazz, Jedis jedis) {
        //创建对象
        T t = null;
        try {
            t = clazz.newInstance();
            //遍历属性
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                //根据属性名去redis中找值
                String fieldName = field.getName();//按照我们的存的规则.这个就是redis只给中hash的field
                String result = hGet(key, fieldName, jedis);
                if (result != null) {
                    //找到值就赋值进去
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, clazz);//在指定的类中查找执行的属性名
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

    public Long expire(String key, int seconds, Jedis jedis) {
        return jedis.expire(key, seconds);
    }

    public Long del(String key, Jedis jedis) {
        return jedis.del(key);
    }

    public Long hDel(String key, String[] fields, Jedis jedis) {
        return jedis.hdel(key, fields);
    }
}
