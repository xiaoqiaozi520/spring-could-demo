package cn.ymdd.framework.cache.client;

import cn.ymdd.framework.cache.command.RedisBatch;
import cn.ymdd.framework.cache.command.RedisTransaction;
import org.springframework.data.redis.connection.StringRedisConnection;

public abstract interface RedisClient extends StringRedisConnection, RedisTransaction, RedisBatch
{
  public static final String HASH_CODE = "hashCode";
  public static final String EQUALS = "equals";
  public static final String CLOSE = "close";
}

/* Location:           E:\demo\git\spring-could-demo\cn\guludai\framework\guludai-framework-cache\0.0.1-SNAPSHOT\guludai-framework-cache-0.0.1-20180604.040631-15.jar
 * Qualified Name:     cn.guludai.framework.cache.client.RedisClient
 * JD-Core Version:    0.6.0
 */