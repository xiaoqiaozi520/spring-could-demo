/*    */ package cn.ymdd.framework.cache.client;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ public class RedisResult
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 2601880672875865655L;
/*    */   private String command;
/*    */   private Object result;
/*    */ 
/*    */   public RedisResult()
/*    */   {
/*    */   }
/*    */ 
/*    */   public RedisResult(String command, Object result)
/*    */   {
/* 22 */     this.command = command;
/* 23 */     this.result = result;
/*    */   }
/*    */ 
/*    */   public String getCommand() {
/* 27 */     return this.command;
/*    */   }
/*    */ 
/*    */   public void setCommand(String command) {
/* 31 */     this.command = command;
/*    */   }
/*    */ 
/*    */   public Object getResult() {
/* 35 */     return this.result;
/*    */   }
/*    */ 
/*    */   public void setResult(Object result) {
/* 39 */     this.result = result;
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 44 */     return "RedisResult [command=" + this.command + ", result=" + this.result + "]";
/*    */   }
/*    */ }

/* Location:           E:\demo\git\spring-could-demo\cn\guludai\framework\guludai-framework-cache\0.0.1-SNAPSHOT\guludai-framework-cache-0.0.1-20180604.040631-15.jar
 * Qualified Name:     cn.guludai.framework.cache.client.RedisResult
 * JD-Core Version:    0.6.0
 */