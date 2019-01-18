/*    */ package cn.ymdd.container.runtime.auth;
/*    */
/*    */ import cn.ymdd.api.code.BaseCode;
/*    */ import cn.ymdd.container.runtime.exception.RunningException;
/*    */ import com.alibaba.fastjson.JSON;
/*    */ import java.nio.charset.Charset;
/*    */ import java.util.Base64;
/*    */ import java.util.Base64.Decoder;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
/*    */
/*    */ public class AuthorityInterceptor extends HandlerInterceptorAdapter
/*    */ {
/* 26 */   private static final Logger log = LoggerFactory.getLogger(AuthorityInterceptor.class);
/*    */
/*    */   public final boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
/*    */   {
/* 30 */     String auth = request.getHeader("X-Authority");
/* 31 */     if ((auth == null) || (auth.isEmpty())) {
/* 32 */       if (log.isDebugEnabled()) {
/* 33 */         log.debug("[ GULUDAI ] authority session is empty.");
/*    */       }
/* 35 */       return true;
/*    */     }
/*    */     try {
/* 38 */       String ras = new String(Base64.getDecoder().decode(auth), Charset.forName("UTF-8"));
/* 39 */       AuthoritySession session = (AuthoritySession)JSON.parseObject(ras, AuthoritySession.class);
/* 40 */       AuthorityContext.setAuthoritySession(session);
/* 41 */       if (log.isDebugEnabled()) {
/* 42 */         log.debug("[ GULUDAI ] authority session [ " + ras + " ]");
/*    */       }
/* 44 */       return true;
/*    */     } catch (Exception e) {
/* 46 */       if (log.isErrorEnabled())
/* 47 */         log.error(e.getMessage(), e);
                 throw new RunningException(BaseCode.FORBIDDEN, e);
/*    */     }
/* 49 */
/*    */   }
/*    */
/*    */   public final void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
/*    */     throws Exception
/*    */   {
/* 55 */     AuthorityContext.delAuthorityContext();
/*    */   }
/*    */ }

/* Location:           E:\demo\git\spring-could-demo\cn\guludai\container\guludai-container-runtime\0.0.1-SNAPSHOT\guludai-container-runtime-0.0.1-20180627.035509-108.jar
 * Qualified Name:     cn.guludai.container.runtime.auth.AuthorityInterceptor
 * JD-Core Version:    0.6.0
 */