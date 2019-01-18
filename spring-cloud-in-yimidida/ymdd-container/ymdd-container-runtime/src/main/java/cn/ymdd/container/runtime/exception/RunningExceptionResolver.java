/*    */ package cn.ymdd.container.runtime.exception;
/*    */ 
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ import org.springframework.web.servlet.HandlerExceptionResolver;
/*    */ import org.springframework.web.servlet.ModelAndView;
/*    */ 
/*    */ public class RunningExceptionResolver
/*    */   implements HandlerExceptionResolver
/*    */ {
/*    */   public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
/*    */   {
/* 19 */     return null;
/*    */   }
/*    */ }

/* Location:           E:\demo\git\spring-could-demo\cn\guludai\container\guludai-container-runtime\0.0.1-SNAPSHOT\guludai-container-runtime-0.0.1-20180627.035509-108.jar
 * Qualified Name:     cn.guludai.container.runtime.exception.RunningExceptionResolver
 * JD-Core Version:    0.6.0
 */