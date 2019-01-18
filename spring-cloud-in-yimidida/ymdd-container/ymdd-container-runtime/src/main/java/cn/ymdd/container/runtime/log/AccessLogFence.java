/*    */ package cn.ymdd.container.runtime.log;
/*    */ 
/*    */ import cn.ymdd.container.runtime.fence.HttpFenceChain;
/*    */ import cn.ymdd.container.runtime.fence.HttpFenceContent;
/*    */ import cn.ymdd.container.runtime.profile.RuntimeEnvironment;
/*    */ import cn.ymdd.container.runtime.profile.RuntimeProfile;
/*    */ import com.alibaba.fastjson.JSON;
/*    */ import java.text.SimpleDateFormat;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ public class AccessLogFence
/*    */   implements HttpFenceChain
/*    */ {
/*    */   public static final String ACCESS_LOGGER_NAME = "Access";
/*    */   public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss SSS";
/*    */   private final org.slf4j.Logger log;
/*    */ 
/*    */   public AccessLogFence(String loggerName)
/*    */   {
/* 28 */     this.log = LoggerFactory.getLogger(loggerName);
/*    */   }
/*    */ 
/*    */   private static void AccessLogReset(org.slf4j.Logger log) {
/* 32 */     if (!(log instanceof ch.qos.logback.classic.Logger)) {
/* 33 */       throw new IllegalStateException("Not support " + log.getClass() + " logger");
/*    */     }
/* 35 */     ch.qos.logback.classic.Logger lg = (ch.qos.logback.classic.Logger)log;
/* 36 */     if (lg.isAdditive()) {
/* 37 */       RuntimeProfile profile = RuntimeEnvironment.getProfile();
/* 38 */       if ((profile != null) && 
/* 39 */         (RuntimeProfile.LOCAL != profile) && 
/* 40 */         ((log instanceof ch.qos.logback.classic.Logger)))
/* 41 */         ((ch.qos.logback.classic.Logger)log).setAdditive(false);
/*    */     }
/*    */   }
/*    */ 
/*    */   public void requestHttpFence(HttpFenceContent content)
/*    */   {
/* 50 */     AccessRequestLog requestLog = new AccessRequestLog();
/* 51 */     requestLog.requestTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS").format(Long.valueOf(System.currentTimeMillis()));
/* 52 */     requestLog.requestParamters = content.getParameters();
/* 53 */     requestLog.requestProtocol = content.getProtocol();
/* 54 */     requestLog.requestHeaders = content.getHeaders();
/* 55 */     requestLog.requestMethod = content.getMethod();
/* 56 */     requestLog.requestDomain = content.getDomain();
/* 57 */     requestLog.requestBody = content.getBodies();
/* 58 */     requestLog.requestSize = content.getSize();
/* 59 */     requestLog.requestUri = content.getUri();
/* 60 */     requestLog.requestIp = content.getIp();
/* 61 */     requestLog.id = String.valueOf(content.getId());
/* 62 */     org.slf4j.Logger lg = this.log;
/* 63 */     if (lg == null) {
/* 64 */       lg = LoggerFactory.getLogger(AccessLogFence.class);
/*    */     }
/* 66 */     if (!"Access".equals(lg.getName())) {
/* 67 */       throw new NullPointerException("Not found 'Access' name logger");
/*    */     }
/* 69 */     AccessLogReset(lg);
/* 70 */     if (lg.isInfoEnabled())
/* 71 */       lg.info(JSON.toJSONString(requestLog));
/*    */   }
/*    */ 
/*    */   public void responseHttpFence(HttpFenceContent content)
/*    */   {
/* 77 */     AccessResponseLog responseLog = new AccessResponseLog();
/* 78 */     responseLog.responseTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS").format(Long.valueOf(System.currentTimeMillis()));
/* 79 */     responseLog.responseHeaders = content.getHeaders();
/* 80 */     responseLog.responseBody = content.getBodies();
/* 81 */     responseLog.responseSize = content.getSize();
/* 82 */     responseLog.responseCode = content.getHttp();
/* 83 */     responseLog.id = String.valueOf(content.getId());
/* 84 */     org.slf4j.Logger lg = this.log;
/* 85 */     if (lg == null) {
/* 86 */       lg = LoggerFactory.getLogger(AccessLogFence.class);
/*    */     }
/* 88 */     if (!"Access".equals(lg.getName())) {
/* 89 */       throw new NullPointerException("Not found 'Access' name logger");
/*    */     }
/* 91 */     AccessLogReset(lg);
/* 92 */     if (lg.isInfoEnabled())
/* 93 */       lg.info(JSON.toJSONString(responseLog));
/*    */   }
/*    */ }

/* Location:           E:\demo\git\spring-could-demo\cn\guludai\container\guludai-container-runtime\0.0.1-SNAPSHOT\guludai-container-runtime-0.0.1-20180627.035509-108.jar
 * Qualified Name:     cn.guludai.container.runtime.log.AccessLogFence
 * JD-Core Version:    0.6.0
 */