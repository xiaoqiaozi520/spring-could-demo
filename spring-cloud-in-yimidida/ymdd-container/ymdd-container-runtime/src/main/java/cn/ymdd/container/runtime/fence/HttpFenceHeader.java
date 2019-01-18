/*    */ package cn.ymdd.container.runtime.fence;
/*    */ 
/*    */ import cn.ymdd.framework.toolkit.id.GUID;
/*    */ import cn.ymdd.framework.toolkit.util.StringUtil;
/*    */ import javax.servlet.ServletRequest;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import org.springframework.http.MediaType;
/*    */ 
/*    */ public class HttpFenceHeader
/*    */ {
/*    */   public static final String HTTP_FENCE_HEADER = "X-Fence-Id";
/*    */ 
/*    */   public static final String getHttpFenceHeaderID(ServletRequest request)
/*    */   {
/* 23 */     if ((request instanceof HttpServletRequest)) {
/* 24 */       String fenceID = ((HttpServletRequest)request).getHeader("X-Fence-Id");
/* 25 */       if (StringUtil.isEmpty(fenceID)) {
/* 26 */         return GUID.randomGUID();
/*    */       }
/* 28 */       return fenceID;
/*    */     }
/*    */ 
/* 31 */     throw new IllegalStateException("Not support");
/*    */   }
/*    */ 
/*    */   public static final MediaType getHttpFenceMediaType(ServletRequest request)
/*    */   {
/* 36 */     String type = getHttpFenceHeaderType(request);
/* 37 */     if (StringUtil.isNotEmpty(type)) {
/* 38 */       return MediaType.valueOf(type);
/*    */     }
/* 40 */     return null;
/*    */   }
/*    */ 
/*    */   public static final String getHttpFenceHeaderType(ServletRequest request)
/*    */   {
/*    */     try {
/* 46 */       HttpServletRequest req = (HttpServletRequest)request;
/* 47 */       if (StringUtil.isNotEmpty(req.getHeader("Content-Type"))) {
/* 48 */         return req.getContentType();
/*    */       }
/* 50 */       String accept = req.getHeader("Accept");
/* 51 */       if (StringUtil.isNotEmpty(accept)) {
/* 52 */         for (String acp : accept.split(",")) {
/* 53 */           if (acp.indexOf("*/*") == -1) {
/* 54 */             return acp;
/*    */           }
/*    */         }
/*    */       }
/* 58 */       return null; } catch (Exception e) {
/*    */     }
/* 60 */     return null;
/*    */   }
/*    */ }

/* Location:           E:\demo\git\spring-could-demo\cn\guludai\container\guludai-container-runtime\0.0.1-SNAPSHOT\guludai-container-runtime-0.0.1-20180627.035509-108.jar
 * Qualified Name:     cn.guludai.container.runtime.fence.HttpFenceHeader
 * JD-Core Version:    0.6.0
 */