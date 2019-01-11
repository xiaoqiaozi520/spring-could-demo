/*    */ package cn.guludai.container.web.main;
/*    */ 
/*    */ import ch.qos.logback.classic.PatternLayout;
/*    */ import cn.guludai.container.runtime.log.AccessId;
/*    */ import cn.guludai.container.runtime.profile.RuntimeEnvironment;
/*    */ import java.util.Map;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ import org.springframework.boot.CommandLineRunner;
/*    */ import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
/*    */ import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
/*    */ import org.springframework.context.ApplicationContext;
/*    */ import org.springframework.context.ApplicationContextAware;
/*    */ 
/*    */ @EnableAutoConfiguration(exclude={RedisAutoConfiguration.class})
/*    */ public abstract class WebApplication
/*    */   implements CommandLineRunner, ApplicationContextAware
/*    */ {
/* 26 */   private static final Logger log = LoggerFactory.getLogger(WebApplication.class);
/*    */ 
/*    */   public void run(String[] args)
/*    */     throws Exception
/*    */   {
/* 33 */     Map listeners = RuntimeEnvironment.getBeansByType(WebListener.class);
/* 34 */     for (WebListener listener : listeners.values()) {
/* 35 */       listener.initialize();
/*    */     }
/* 37 */     if (log.isInfoEnabled())
/* 38 */       log.info("Start [ " + RuntimeEnvironment.getProperties("spring.application.name") + " ] service successful...");
/*    */   }
/*    */ 
/*    */   public final void setApplicationContext(ApplicationContext applicationContext)
/*    */   {
/* 44 */     RuntimeEnvironment.initProfile(applicationContext);
/*    */   }
/*    */ 
/*    */   static
/*    */   {
/* 28 */     PatternLayout.defaultConverterMap.put("id", AccessId.class.getName());
/*    */   }
/*    */ }

/* Location:           C:\Users\Administrator\Desktop\container\guludai-container-web\0.0.1-SNAPSHOT\guludai-container-web-0.0.1-20180428.084950-107.jar
 * Qualified Name:     cn.guludai.container.web.main.WebApplication
 * JD-Core Version:    0.6.0
 */