/*    */ package cn.ymdd.framework.cache.command;
/*    */ 
/*    */ import cn.ymdd.framework.cache.client.RedisClient;
/*    */ import cn.ymdd.framework.cache.client.RedisException;
/*    */ import java.lang.reflect.Method;
/*    */ import java.nio.charset.Charset;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ 
/*    */ public abstract class RedisTransactionAdvice
/*    */   implements RedisAdvice
/*    */ {
/*    */   public abstract void doInTransaction();
/*    */ 
/*    */   public final boolean prepare(RedisClient template, Method method, Object[] args)
/*    */   {
/* 23 */     if ("transaction".equalsIgnoreCase(method.getName())) {
/* 24 */       if ((args[0] instanceof String)) {
/* 25 */         template.watch(new byte[][] { ((String)args[0]).getBytes(Charset.forName("UTF8")) });
/* 26 */       } else if ((args[0] instanceof String[])) {
/* 27 */         String[] keys = (String[])(String[])args[0];
/* 28 */         byte[][] bits = new byte[keys.length][1];
/* 29 */         for (int i = 0; i < keys.length; i++) {
/* 30 */           bits[i] = keys[i].getBytes(Charset.forName("UTF8"));
/*    */         }
/* 32 */         template.watch(bits);
/* 33 */       } else if ((args[0] instanceof byte[][])) {
/* 34 */         template.watch((byte[][])(byte[][])args[0]);
/*    */       } else {
/* 36 */         template.watch(new byte[][] { (byte[])(byte[])args[0] });
/*    */       }
/* 38 */       template.multi();
/* 39 */       return true;
/*    */     }
/* 41 */     return false;
/*    */   }
/*    */ 
/*    */   public final <E> E execute(RedisClient template, Method method, Object[] args)
/*    */   {
/*    */     try
/*    */     {
/* 48 */       doInTransaction();
/* 49 */       return null; } catch (Exception e) {
/*    */     }
/* 51 */     return new RedisException(e.getMessage(), e);
/*    */   }
/*    */ 
/*    */   public final <T> List<T> commit(RedisClient template, Method method, Object[] args)
/*    */   {
/* 58 */     if ("transaction".equalsIgnoreCase(method.getName())) {
/*    */       try {
/* 60 */         List localList = template.exec();
/*    */         return localList;
/*    */       } finally {
/* 62 */         template.unwatch();
/*    */       }
/*    */     }
/* 65 */     return Collections.emptyList();
/*    */   }
/*    */ }

/* Location:           E:\demo\git\spring-could-demo\cn\guludai\framework\guludai-framework-cache\0.0.1-SNAPSHOT\guludai-framework-cache-0.0.1-20180604.040631-15.jar
 * Qualified Name:     cn.guludai.framework.cache.command.RedisTransactionAdvice
 * JD-Core Version:    0.6.0
 */