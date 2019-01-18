/*    */ package cn.ymdd.framework.cache.client;
/*    */ 
/*    */ import cn.ymdd.framework.toolkit.util.ClassUtil;
/*    */ import cn.ymdd.framework.toolkit.util.StringUtil;
/*    */ import org.springframework.context.EnvironmentAware;
/*    */ import org.springframework.core.env.Environment;
/*    */ import redis.clients.jedis.JedisPoolConfig;
/*    */ 
/*    */ class RedisConfig extends JedisPoolConfig
/*    */   implements EnvironmentAware
/*    */ {
/*    */   public static final String REDIS_PREFIX = "redis";
/* 19 */   private Boolean inited = Boolean.valueOf(false);
/*    */   private String hostName;
/*    */   private String password;
/* 22 */   private int database = 0;
/* 23 */   private int timeout = 2500;
/* 24 */   private int port = 6379;
/*    */ 
/*    */   public RedisConfig()
/*    */   {
/* 28 */     setMaxWaitMillis(5000L);
/* 29 */     setMinEvictableIdleTimeMillis(600000L);
/* 30 */     setTimeBetweenEvictionRunsMillis(30000L);
/*    */   }
/*    */ 
/*    */   public synchronized void setEnvironment(Environment environment)
/*    */   {
/* 35 */     if ((!this.inited.booleanValue()) && ((this.inited = Boolean.valueOf(true)).booleanValue())) {
/* 36 */       Class clazz = RedisConfig.class;
/* 37 */       while ((clazz = clazz.getSuperclass()) != Object.class)
/* 38 */         for (String name : ClassUtil.getDeclaredFieldNames(clazz)) {
/* 39 */           String value = environment.getProperty("redis." + name);
/* 40 */           if (StringUtil.isNotEmpty(value).booleanValue())
/* 41 */             ClassUtil.setDeclaredFieldValue(this, name, value);
/*    */         }
/*    */     }
/*    */   }
/*    */ 
/*    */   public String getHostName()
/*    */   {
/* 49 */     return this.hostName;
/*    */   }
/*    */ 
/*    */   public void setHostName(String hostName) {
/* 53 */     this.hostName = hostName;
/*    */   }
/*    */ 
/*    */   public String getPassword() {
/* 57 */     return this.password;
/*    */   }
/*    */ 
/*    */   public void setPassword(String password) {
/* 61 */     this.password = password;
/*    */   }
/*    */ 
/*    */   public int getDatabase() {
/* 65 */     return this.database;
/*    */   }
/*    */ 
/*    */   public void setDatabase(int database) {
/* 69 */     this.database = database;
/*    */   }
/*    */ 
/*    */   public int getTimeout() {
/* 73 */     return this.timeout;
/*    */   }
/*    */ 
/*    */   public void setTimeout(int timeout) {
/* 77 */     this.timeout = timeout;
/*    */   }
/*    */ 
/*    */   public int getPort() {
/* 81 */     return this.port;
/*    */   }
/*    */ 
/*    */   public void setPort(int port) {
/* 85 */     this.port = port;
/*    */   }
/*    */ }

/* Location:           E:\demo\git\spring-could-demo\cn\guludai\framework\guludai-framework-cache\0.0.1-SNAPSHOT\guludai-framework-cache-0.0.1-20180604.040631-15.jar
 * Qualified Name:     cn.guludai.framework.cache.client.RedisConfig
 * JD-Core Version:    0.6.0
 */