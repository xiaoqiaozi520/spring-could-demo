/*    */ package cn.guludai.api.code;
/*    */ 
/*    */ import cn.guludai.api.Code;
/*    */ 
/*    */ public enum BaseCode
/*    */   implements Code
/*    */ {
/* 13 */   ERROR("500", "异常"), 
/* 14 */   SUCCESS("200", "成功"), 
/* 15 */   FORBIDDEN("403", "拒绝"), 
/* 16 */   BADREQUEST("400", "数据格式错误"), 
/* 17 */   NOTFOUND("404", "找不到资源或服务"), 
/* 18 */   TIMEOUT("408", "请求超时"), 
/* 19 */   MEDIATYPE("415", "不支持资源类型");
/*    */ 
/*    */   public final String code;
/*    */   public final String message;
/*    */ 
/* 24 */   private BaseCode(String code, String message) { this.message = message;
/* 25 */     this.code = code;
/*    */   }
/*    */ 
/*    */   public String getMessage()
/*    */   {
/* 30 */     return this.message;
/*    */   }
/*    */ 
/*    */   public String getCode()
/*    */   {
/* 35 */     return this.code;
/*    */   }
/*    */ }

/* Location:           C:\Users\Administrator\Desktop\container\guludai-container-api\0.0.1-SNAPSHOT\guludai-container-api-0.0.1-20180207.054839-32.jar
 * Qualified Name:     cn.guludai.api.code.BaseCode
 * JD-Core Version:    0.6.0
 */