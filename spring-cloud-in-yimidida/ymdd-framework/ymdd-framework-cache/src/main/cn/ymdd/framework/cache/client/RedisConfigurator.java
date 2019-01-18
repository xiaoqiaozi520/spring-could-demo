/*    */ package cn.ymdd.framework.cache.client;
/*    */ 
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ import org.springframework.boot.autoconfigure.AutoConfigureBefore;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
/*    */ import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
/*    */ import org.springframework.boot.context.properties.ConfigurationProperties;
/*    */ import org.springframework.context.annotation.Bean;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.context.annotation.Primary;
/*    */ import org.springframework.data.redis.connection.RedisConnectionFactory;
/*    */ import org.springframework.data.redis.core.RedisTemplate;
/*    */ 
/*    */ @Configuration
/*    */ @ConditionalOnMissingBean({RedisClient.class})
/*    */ @AutoConfigureBefore({RedisAutoConfiguration.class})
/*    */ @ConfigurationProperties(prefix="redis")
/*    */ @ConditionalOnProperty(prefix="redis", name={"hostName", "port"})
/*    */ public class RedisConfigurator extends RedisConfig
/*    */ {
/* 29 */   private static final Logger log = LoggerFactory.getLogger(RedisConfigurator.class);
/*    */ 
/*    */   public RedisConfigurator() {
/* 32 */     if (log.isInfoEnabled())
/* 33 */       log.info("[ GULUDAI ] Inited redis configurator..."); 
/*    */   }
/*    */ 
/*    */   @Bean({"redisClient"})
/*    */   @ConditionalOnMissingBean(name={"redisClient"})
/*    */   protected RedisClient createRedisClient(RedisConnectionFactory factory) {
/* 40 */     return ((RedisFactory)factory).getJedisTemplate();
/*    */   }
/* 46 */   @Bean({"redisTemplate"})
/*    */   @ConditionalOnMissingBean(name={"redisTemplate"})
/*    */   protected RedisTemplate<Object, Object> createRedisTemplate(RedisConnectionFactory factory) { RedisTemplate template = new RedisTemplate();
/* 47 */     template.setConnectionFactory(factory);
/* 48 */     return template; } 
/*    */   @Bean
/*    */   @Primary
/*    */   protected RedisConnectionFactory createRedisFactory() {
/* 54 */     return RedisFactory.createJedisFactory(this);
/*    */   }
/*    */ }
