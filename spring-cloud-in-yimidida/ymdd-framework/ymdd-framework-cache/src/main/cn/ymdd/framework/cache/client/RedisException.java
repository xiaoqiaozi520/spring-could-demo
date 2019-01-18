/*    */ package cn.ymdd.framework.cache.client;
/*    */ 
/*    */ public class RedisException extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = 4930884722187267482L;
/*    */ 
/*    */   public RedisException()
/*    */   {
/*    */   }
/*    */ 
/*    */   public RedisException(String message)
/*    */   {
/* 17 */     super(message);
/*    */   }
/*    */ 
/*    */   public RedisException(Throwable cause) {
/* 21 */     super(cause);
/*    */   }
/*    */ 
/*    */   public RedisException(String message, Throwable cause) {
/* 25 */     super(message, cause);
/*    */   }
/*    */ 
/*    */   public RedisException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
/* 29 */     super(message, cause, enableSuppression, writableStackTrace);
/*    */   }
/*    */ }

/* Location:           E:\demo\git\spring-could-demo\cn\guludai\framework\guludai-framework-cache\0.0.1-SNAPSHOT\guludai-framework-cache-0.0.1-20180604.040631-15.jar
 * Qualified Name:     cn.guludai.framework.cache.client.RedisException
 * JD-Core Version:    0.6.0
 */