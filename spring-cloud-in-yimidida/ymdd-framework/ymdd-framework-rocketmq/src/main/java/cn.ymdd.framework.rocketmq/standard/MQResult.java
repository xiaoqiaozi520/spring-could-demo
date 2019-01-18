/*    */ package cn.ymdd.framework.rocketmq.standard;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ public class MQResult
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = -6889783200074671444L;
/*    */   private final boolean status;
/*    */   private Exception track;
/*    */   private String message;
/*    */ 
/*    */   public static MQResult success()
/*    */   {
/* 19 */     return new MQResult(true);
/*    */   }
/*    */ 
/*    */   public static MQResult failure() {
/* 23 */     return new MQResult(false);
/*    */   }
/*    */ 
/*    */   public static MQResult failure(String errorMessage) {
/* 27 */     return failure(errorMessage, null);
/*    */   }
/*    */ 
/*    */   public static MQResult failure(String errorMessage, Exception errorException) {
/* 31 */     MQResult mqr = new MQResult(false);
/* 32 */     mqr.message = errorMessage;
/* 33 */     mqr.track = errorException;
/* 34 */     return mqr;
/*    */   }
/*    */ 
/*    */   public static MQResult failure(Exception errorException) {
/* 38 */     MQResult mqr = new MQResult(false);
/* 39 */     mqr.message = errorException.getMessage();
/* 40 */     mqr.track = errorException;
/* 41 */     return mqr;
/*    */   }
/*    */ 
/*    */   private MQResult(boolean status) {
/* 45 */     this.status = status;
/*    */   }
/*    */ 
/*    */   public boolean isSuccess() {
/* 49 */     return this.status;
/*    */   }
/*    */ 
/*    */   public boolean isFailure() {
/* 53 */     return !isSuccess();
/*    */   }
/*    */ 
/*    */   public String getMessage() {
/* 57 */     return this.message;
/*    */   }
/*    */ 
/*    */   public Exception getTrack() {
/* 61 */     return this.track;
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 66 */     return "MQResult [status=" + this.status + ", track=" + this.track + ", message=" + this.message + "]";
/*    */   }
/*    */ }

/* Location:           E:\demo\git\spring-could-demo\cn\guludai\framework\guludai-framework-rocketmq\0.0.1-SNAPSHOT\guludai-framework-rocketmq-0.0.1-20180208.025258-52.jar
 * Qualified Name:     cn.guludai.framework.rocketmq.standard.MQResult
 * JD-Core Version:    0.6.0
 */