package cn.ymdd.framework.cache.command;

import cn.ymdd.framework.cache.client.RedisClient;
import java.lang.reflect.Method;
import java.util.List;

public abstract interface RedisAdvice
{
  public abstract <E> E execute(RedisClient paramRedisClient, Method paramMethod, Object[] paramArrayOfObject);

  public abstract boolean prepare(RedisClient paramRedisClient, Method paramMethod, Object[] paramArrayOfObject);

  public abstract <T> List<T> commit(RedisClient paramRedisClient, Method paramMethod, Object[] paramArrayOfObject);
}

/* Location:           E:\demo\git\spring-could-demo\cn\guludai\framework\guludai-framework-cache\0.0.1-SNAPSHOT\guludai-framework-cache-0.0.1-20180604.040631-15.jar
 * Qualified Name:     cn.guludai.framework.cache.command.RedisAdvice
 * JD-Core Version:    0.6.0
 */