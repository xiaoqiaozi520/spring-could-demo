/*    */ package cn.ymdd.container.runtime.log;
/*    */ 
/*    */ import ch.qos.logback.classic.pattern.ClassicConverter;
/*    */ import ch.qos.logback.classic.spi.ILoggingEvent;
/*    */ import cn.ymdd.container.runtime.fence.HttpFenceContent;
/*    */ import cn.ymdd.container.runtime.fence.HttpFenceContext;
/*    */ 
/*    */ public final class AccessId extends ClassicConverter
/*    */ {
/*    */   public String convert(ILoggingEvent event)
/*    */   {
/* 18 */     HttpFenceContent content = HttpFenceContext.getUpHttpFenceContent();
/* 19 */     if (content == null) {
/* 20 */       content = HttpFenceContext.getDownHttpFenceContent();
/*    */     }
/* 22 */     if (content != null) {
/* 23 */       return content.getId();
/*    */     }
/* 25 */     return "main";
/*    */   }
/*    */ }

