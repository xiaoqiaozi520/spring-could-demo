package cn.ymdd.framework.cache.command;

import cn.ymdd.framework.cache.client.RedisResult;
import java.util.List;

public abstract interface RedisBatch
{
  public static final String BATCH = "batch";

  public abstract List<RedisResult> batch(RedisBatchAdvice paramRedisBatchAdvice);
}

/* Location:           E:\demo\git\spring-could-demo\cn\guludai\framework\guludai-framework-cache\0.0.1-SNAPSHOT\guludai-framework-cache-0.0.1-20180604.040631-15.jar
 * Qualified Name:     cn.guludai.framework.cache.command.RedisBatch
 * JD-Core Version:    0.6.0
 */