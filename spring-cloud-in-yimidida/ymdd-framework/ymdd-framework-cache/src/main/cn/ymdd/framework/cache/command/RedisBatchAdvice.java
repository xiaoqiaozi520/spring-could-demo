/*    */ package cn.ymdd.framework.cache.command;
/*    */ 
/*    */ import cn.ymdd.framework.cache.client.RedisClient;
/*    */ import cn.ymdd.framework.cache.client.RedisException;
/*    */ import java.lang.reflect.Method;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ 
/*    */ public abstract class RedisBatchAdvice
/*    */   implements RedisAdvice
/*    */ {
/*    */   public abstract void doInBatch();
/*    */ 
/*    */   public final boolean prepare(RedisClient template, Method method, Object[] args)
/*    */   {
/* 22 */     if ("batch".equalsIgnoreCase(method.getName())) {
/* 23 */       if (template.isPipelined()) {
/* 24 */         throw new UnsupportedOperationException();
/*    */       }
/* 26 */       template.openPipeline();
/* 27 */       return true;
/*    */     }
/*    */ 
/* 30 */     return false;
/*    */   }
/*    */ 
/*    */   public final <E> E execute(RedisClient template, Method method, Object[] args)
/*    */   {
/*    */     try
/*    */     {
/* 37 */       doInBatch();
/* 38 */       return null; } catch (Exception e) {
/*    */     }
/* 40 */     return new RedisException(e.getMessage(), e);
/*    */   }
/*    */ 
/*    */   public final <T> List<T> commit(RedisClient template, Method method, Object[] args)
/*    */   {
/* 47 */     if ("batch".equalsIgnoreCase(method.getName())) {
/* 48 */       return template.closePipeline();
/*    */     }
/* 50 */     return Collections.emptyList();
/*    */   }
/*    */ }

/* Location:           E:\demo\git\spring-could-demo\cn\guludai\framework\guludai-framework-cache\0.0.1-SNAPSHOT\guludai-framework-cache-0.0.1-20180604.040631-15.jar
 * Qualified Name:     cn.guludai.framework.cache.command.RedisBatchAdvice
 * JD-Core Version:    0.6.0
 */