/*    */ package cn.ymdd.container.runtime.auth;
/*    */ 
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ import org.springframework.core.MethodParameter;
/*    */ import org.springframework.web.bind.annotation.RequestBody;
/*    */ import org.springframework.web.bind.support.WebDataBinderFactory;
/*    */ import org.springframework.web.context.request.NativeWebRequest;
/*    */ import org.springframework.web.method.support.HandlerMethodArgumentResolver;
/*    */ import org.springframework.web.method.support.ModelAndViewContainer;
/*    */ 
/*    */ public class AuthorityResolver
/*    */   implements HandlerMethodArgumentResolver
/*    */ {
/* 20 */   private static final Logger log = LoggerFactory.getLogger(AuthorityResolver.class);
/*    */ 
/*    */   public boolean supportsParameter(MethodParameter parameter)
/*    */   {
/* 24 */     boolean context = parameter.getParameterType() == AuthorityContext.class;
/* 25 */     boolean session = parameter.getParameterType() == AuthoritySession.class;
/* 26 */     return ((context) || (session)) && (parameter.getParameterAnnotation(RequestBody.class) == null);
/*    */   }
/*    */ 
/*    */   public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception
/*    */   {
/* 31 */     if (log.isDebugEnabled()) {
/* 32 */       log.debug("[ GULUDAI ] Please use the 'AuthorityContext.getAuthoritySession()' method for session information");
/*    */     }
/* 34 */     if (parameter.getParameterType() == AuthorityContext.class) {
/* 35 */       return AuthorityContext.getAuthorityContext();
/*    */     }
/* 37 */     if (parameter.getParameterType() == AuthoritySession.class) {
/* 38 */       return AuthorityContext.getAuthoritySession();
/*    */     }
/* 40 */     return new AuthoritySession();
/*    */   }
/*    */ }

/* Location:           E:\demo\git\spring-could-demo\cn\guludai\container\guludai-container-runtime\0.0.1-SNAPSHOT\guludai-container-runtime-0.0.1-20180627.035509-108.jar
 * Qualified Name:     cn.guludai.container.runtime.auth.AuthorityResolver
 * JD-Core Version:    0.6.0
 */