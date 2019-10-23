package com.yhxx.mhb.redis.operate.redis.client;

import com.yhxx.mhb.redis.operate.redis.annotation.SkipRedis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Component
public class RedisClient implements RedisClientInterface {
    @Autowired
    private JedisPool jedisPool;

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    @Override
    public Jedis getJedis() {
        return jedisPool.getResource();
    }

    @Override
    public void closeJedis(Jedis jedis) {
        jedis.close();
    }

    @Override
    public String set(String key, String value, Jedis jedis) {
        return jedis.set(key, value);
    }

    @Override
    public String set(String key, String value, String nxxx, String expx, long time, Jedis jedis) {
        return jedis.set(key, value, nxxx, expx, time);
    }

    @Override
    public String get(String key, Jedis jedis) {
        return jedis.get(key);
    }

    @Override
    public Long hSet(String key, String field, String value, Jedis jedis) {
        return jedis.hset(key, field, value);
    }

    @Override
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

    @Override
    public String hGet(String key, String filed, Jedis jedis) {
        return jedis.hget(key, filed);
    }

    @Override
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

    @Override
    public Long expire(String key, int seconds, Jedis jedis) {
        return jedis.expire(key, seconds);
    }

    @Override
    public Long del(String key, Jedis jedis) {
        return jedis.del(key);
    }

    @Override
    public Long hDel(String key, String[] fields, Jedis jedis) {
        return jedis.hdel(key, fields);
    }
}
