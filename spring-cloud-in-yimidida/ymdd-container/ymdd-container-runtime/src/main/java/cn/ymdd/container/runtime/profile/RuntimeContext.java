/*    */ package cn.ymdd.container.runtime.profile;
/*    */ 
/*    */ import cn.ymdd.container.runtime.fence.HttpFenceContext;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ 
/*    */ public final class RuntimeContext
/*    */ {
/*    */   public static HttpServletRequest getHttpServletRequest()
/*    */   {
/* 17 */     return (HttpServletRequest)HttpFenceContext.getServletRequest();
/*    */   }
/*    */ 
/*    */   public static HttpServletResponse getHttpServletResponse() {
/* 21 */     return (HttpServletResponse)HttpFenceContext.getServletResponse();
/*    */   }
/*    */ }

/* Location:           E:\demo\git\spring-could-demo\cn\guludai\container\guludai-container-runtime\0.0.1-SNAPSHOT\guludai-container-runtime-0.0.1-20180627.035509-108.jar
 * Qualified Name:     cn.guludai.container.runtime.profile.RuntimeContext
 * JD-Core Version:    0.6.0
 */