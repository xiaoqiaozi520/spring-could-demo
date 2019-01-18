/*    */ package cn.ymdd.container.runtime.time;
/*    */ 
/*    */ import cn.ymdd.container.runtime.fence.HttpFenceHeader;
/*    */ import cn.ymdd.framework.toolkit.util.StringUtil;
/*    */ import java.text.SimpleDateFormat;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ import org.springframework.http.MediaType;
/*    */ import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
/*    */ 
/*    */ public final class TimeInterceptor extends HandlerInterceptorAdapter
/*    */ {
/*    */   public static final String X_REQUEST_TIME = "X-Request-Time";
/*    */   public static final String X_RESPONSE_TIME = "X-Response-Time";
/*    */   public static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss SSS";
/*    */ 
/*    */   public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
/*    */     throws Exception
/*    */   {
/* 31 */     response.setHeader("X-Request-Time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS").format(Long.valueOf(System.currentTimeMillis())));
/* 32 */     if (StringUtil.isEmpty(response.getHeader("Content-Type"))) {
/* 33 */       String contentType = HttpFenceHeader.getHttpFenceHeaderType(request);
/* 34 */       if ((StringUtil.isEmpty(contentType)) || (MediaType.APPLICATION_JSON.includes(MediaType.valueOf(contentType))))
/* 35 */         response.setHeader("Content-Type", "application/json;charset=UTF-8");
/*    */       else {
/* 37 */         response.setHeader("Content-Type", contentType);
/*    */       }
/*    */     }
/* 40 */     return true;
/*    */   }
/*    */ 
/*    */   public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception
/*    */   {
/* 45 */     response.setHeader("X-Response-Time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS").format(Long.valueOf(System.currentTimeMillis())));
/*    */   }
/*    */ }

/* Location:           E:\demo\git\spring-could-demo\cn\guludai\container\guludai-container-runtime\0.0.1-SNAPSHOT\guludai-container-runtime-0.0.1-20180627.035509-108.jar
 * Qualified Name:     cn.guludai.container.runtime.time.TimeInterceptor
 * JD-Core Version:    0.6.0
 */