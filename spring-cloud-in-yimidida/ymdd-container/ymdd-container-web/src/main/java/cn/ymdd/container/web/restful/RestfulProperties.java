/*    */ package cn.guludai.container.web.restful;
/*    */ 
/*    */ import cn.guludai.container.runtime.profile.RuntimeEnvironment;
/*    */ import cn.guludai.framework.toolkit.util.StringUtil;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ final class RestfulProperties
/*    */ {
/*    */   public static final String RESTFUL_STATIC_RESOURCE = "spring.static.resources";
/* 19 */   public static final Map<String, String[]> RESTFUL_STATIC_RESOURCE_VALUE = new HashMap() { } ;
/*    */   public static final String RESTFUL_CLIENT_TIMEOUT = "feign.timeout";
/*    */   public static final String RESTFUL_CLIENT_TIMEOUT_VALUE = "5000,10000";
/*    */   public static final String RESTFUL_CLIENT_NAME = "ribbon.client.name";
/*    */ 
/*    */   public static final Integer[] getClientTimeout() {
/* 29 */     String[] times = StringUtil.split("5000,10000", ",");
/* 30 */     String time = RuntimeEnvironment.getProperties("feign.timeout");
/* 31 */     return new Integer[] { Integer.valueOf(Integer.parseInt(times[0])), Integer.valueOf(Integer.parseInt(StringUtil.isEmpty(time) ? times[1] : time)) };
/*    */   }
/*    */ }

/* Location:           C:\Users\Administrator\Desktop\container\guludai-container-web\0.0.1-SNAPSHOT\guludai-container-web-0.0.1-20180428.084950-107.jar
 * Qualified Name:     cn.guludai.container.web.restful.RestfulProperties
 * JD-Core Version:    0.6.0
 */