package cn.ymdd.framework.cache.command;

import cn.ymdd.framework.cache.client.RedisResult;
import java.util.List;

public abstract interface RedisTransaction
{
  public static final String TRANSACTION = "transaction";

  public abstract List<RedisResult> transaction(String paramString, RedisTransactionAdvice paramRedisTransactionAdvice);

  public abstract List<RedisResult> transaction(String[] paramArrayOfString, RedisTransactionAdvice paramRedisTransactionAdvice);

  public abstract List<RedisResult> transaction(byte[] paramArrayOfByte, RedisTransactionAdvice paramRedisTransactionAdvice);

  public abstract List<RedisResult> transaction(byte[][] paramArrayOfByte, RedisTransactionAdvice paramRedisTransactionAdvice);
}

/* Location:           E:\demo\git\spring-could-demo\cn\guludai\framework\guludai-framework-cache\0.0.1-SNAPSHOT\guludai-framework-cache-0.0.1-20180604.040631-15.jar
 * Qualified Name:     cn.guludai.framework.cache.command.RedisTransaction
 * JD-Core Version:    0.6.0
 */