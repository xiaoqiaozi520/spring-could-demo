/*    */ package cn.ymdd.container.runtime.interceptor;
/*    */ 
/*    */ import cn.ymdd.api.Code;
/*    */ import cn.ymdd.api.code.BaseCode;
/*    */ 
/*    */ public final class ControllerBeforeResult
/*    */ {
/* 17 */   public static final ControllerBeforeResult SUCCESS = createResult(BaseCode.SUCCESS, true);
/* 18 */   public static final ControllerBeforeResult ERROR = createResult(BaseCode.ERROR, false);
/*    */   private final boolean ignore;
/*    */   private final String message;
/*    */   private final Code code;
/*    */ 
/*    */   private ControllerBeforeResult(Code code, String message, boolean ignore)
/*    */   {
/* 25 */     this.message = message;
/* 26 */     this.ignore = ignore;
/* 27 */     this.code = code;
/*    */   }
/*    */ 
/*    */   public boolean isIgnore() {
/* 31 */     return this.ignore;
/*    */   }
/*    */ 
/*    */   public String getMessage() {
/* 35 */     return this.message;
/*    */   }
/*    */ 
/*    */   public Code getCode() {
/* 39 */     return this.code;
/*    */   }
/*    */ 
/*    */   public final boolean isFailure() {
/* 43 */     return !isSuccess();
/*    */   }
/*    */ 
/*    */   public final boolean isSuccess() {
/* 47 */     return (getCode() == BaseCode.SUCCESS) || ("200".equals(getCode().getCode()));
/*    */   }
/*    */ 
/*    */   public static final ControllerBeforeResult createResult(Code code, boolean ignore) {
/* 51 */     return new ControllerBeforeResult(code, code.getMessage(), ignore);
/*    */   }
/*    */ 
/*    */   public static final ControllerBeforeResult createResult(Code code, String message, boolean ignore) {
/* 55 */     return new ControllerBeforeResult(code, message, ignore);
/*    */   }
/*    */ }

/* Location:           E:\demo\git\spring-could-demo\cn\guludai\container\guludai-container-runtime\0.0.1-SNAPSHOT\guludai-container-runtime-0.0.1-20180627.035509-108.jar
 * Qualified Name:     cn.guludai.container.runtime.interceptor.ControllerBeforeResult
 * JD-Core Version:    0.6.0
 */