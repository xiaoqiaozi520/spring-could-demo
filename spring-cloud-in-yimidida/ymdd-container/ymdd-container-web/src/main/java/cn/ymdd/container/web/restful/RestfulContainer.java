/*     */ package cn.guludai.container.web.restful;
/*     */ 
/*     */ import cn.guludai.api.code.BaseCode;
/*     */ import cn.guludai.api.data.Response;
/*     */ import cn.guludai.container.runtime.exception.RunningException;
/*     */ import cn.guludai.container.runtime.interceptor.ControllerBeforeInterceptor;
/*     */ import cn.guludai.container.runtime.profile.RuntimeEnvironment;
/*     */ import cn.guludai.container.web.main.WebContainer;
/*     */ import cn.guludai.framework.toolkit.util.ClassUtil;
/*     */ import cn.guludai.framework.toolkit.util.CollectionUtil;
/*     */ import com.alibaba.fastjson.JSON;
/*     */ import com.netflix.client.config.CommonClientConfigKey;
/*     */ import com.netflix.client.config.IClientConfig;
/*     */ import com.netflix.zuul.context.RequestContext;
/*     */ import com.netflix.zuul.http.ZuulServlet;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.boot.autoconfigure.AutoConfigureBefore;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*     */ import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
/*     */ import org.springframework.cloud.netflix.feign.FeignClientsConfiguration;
/*     */ import org.springframework.cloud.netflix.feign.ribbon.FeignRibbonClientAutoConfiguration;
/*     */ import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
/*     */ import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
/*     */ import org.springframework.cloud.netflix.zuul.filters.route.RibbonCommandContext;
/*     */ import org.springframework.cloud.netflix.zuul.filters.route.ZuulFallbackProvider;
/*     */ import org.springframework.cloud.netflix.zuul.filters.route.apache.HttpClientRibbonCommand;
/*     */ import org.springframework.cloud.netflix.zuul.filters.route.apache.HttpClientRibbonCommandFactory;
/*     */ import org.springframework.context.annotation.Bean;
/*     */ import org.springframework.context.annotation.Configuration;
/*     */ import org.springframework.context.annotation.Primary;
/*     */ import org.springframework.web.bind.annotation.ControllerAdvice;
/*     */ 
/*     */ @Configuration
/*     */ @AutoConfigureBefore({WebMvcAutoConfiguration.class})
/*     */ public class RestfulContainer
/*     */ {
/*  54 */   private static final Logger log = LoggerFactory.getLogger(RestfulContainer.class);
/*     */ 
/*     */   @Primary
/*     */   @Configuration
/*     */   @ConditionalOnClass({ZuulServlet.class})
/*     */   protected class RestfulWebBalancer extends RestfulBalancer
/*     */   {
/*     */ 
/*     */     @Autowired(required=false)
/* 110 */     private Set<ZuulFallbackProvider> zuulFallbackProviders = Collections.emptySet();
/*     */ 
/*     */     public RestfulWebBalancer()
/*     */     {
/*  96 */       super(true);
/*     */     }
/*     */     @Bean
/*     */     public RestfulBalancer addRestfulPreBalancer() {
/* 101 */       return new RestfulBalancer.RestfulPreBalancer(this);
/*     */     }
/*     */     @Bean
/*     */     public RestfulBalancer addRestfulPostBalancer() {
/* 106 */       return new RestfulBalancer.RestfulPostBalancer(this);
/*     */     }
/*     */ 
/*     */     @Bean
/*     */     @Primary
/*     */     public HttpClientRibbonCommandFactory addRibbonFactory(SpringClientFactory clientFactory, ZuulProperties zuulProperties)
/*     */     {
/* 115 */       return new HttpClientRibbonCommandFactory(clientFactory, zuulProperties, this.zuulFallbackProviders)
/*     */       {
/*     */         public HttpClientRibbonCommand create(RibbonCommandContext context)
/*     */         {
/* 120 */           HttpClientRibbonCommand command = super.create(context);
/* 121 */           IClientConfig config = (IClientConfig)ClassUtil.getDeclaredFieldValue(command, "config");
/* 122 */           config.setProperty(CommonClientConfigKey.ConnectTimeout, RestfulProperties.getClientTimeout()[0]);
/* 123 */           config.setProperty(CommonClientConfigKey.ReadTimeout, RestfulProperties.getClientTimeout()[1]);
/* 124 */           return command;
/*     */         }
/*     */       };
/*     */     }
/*     */ 
/*     */     public Object run() {
/* 131 */       RequestContext ctx = RequestContext.getCurrentContext();
/* 132 */       ctx.setResponseBody(JSON.toJSONString(Response.failure(BaseCode.ERROR)));
/* 133 */       Throwable error = ctx.getThrowable();
/* 134 */       if (error == null) {
/* 135 */         error = new RunningException(BaseCode.ERROR, error);
/* 136 */         ctx.setThrowable(error);
/*     */       }
/* 138 */       if (RestfulContainer.log.isErrorEnabled()) {
/* 139 */         RestfulContainer.log.error(error.getMessage(), error);
/*     */       }
/* 141 */       return null;
/*     */     }
/*     */   }
/*     */ 
/*     */   @Primary
/*     */   @Configuration
/*     */   @ControllerAdvice
/*     */   @ConditionalOnMissingBean({RestfulConfiguration.class})
/*     */   @AutoConfigureBefore({FeignRibbonClientAutoConfiguration.class, FeignClientsConfiguration.class})
/*     */   protected class RestfulWebConfiguration extends RestfulConfiguration
/*     */   {
/*     */     public RestfulWebConfiguration()
/*     */     {
/*  73 */       if (log.isInfoEnabled())
/*  74 */         log.info("[ GULUDAI ] Inited restful web configuration...");
/*     */     }
/*     */ 
/*     */     protected List<ControllerBeforeInterceptor> addControllerInterceptor()
/*     */     {
/*  81 */       Map listeners = RuntimeEnvironment.getBeansByType(ControllerBeforeInterceptor.class);
/*  82 */       if (CollectionUtil.isNotEmpty(listeners)) {
/*  83 */         return new ArrayList(listeners.values());
/*     */       }
/*  85 */       return Collections.EMPTY_LIST;
/*     */     }
/*     */   }
/*     */ 
/*     */   @Configuration
/*     */   protected class RestfulWebContainer extends WebContainer
/*     */   {
/*     */     public RestfulWebContainer()
/*     */     {
/*  59 */       if (RestfulContainer.log.isInfoEnabled())
/*  60 */         RestfulContainer.log.info("[ GULUDAI ] Inited restful web container...");
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Users\Administrator\Desktop\container\guludai-container-web\0.0.1-SNAPSHOT\guludai-container-web-0.0.1-20180428.084950-107.jar
 * Qualified Name:     cn.guludai.container.web.restful.RestfulContainer
 * JD-Core Version:    0.6.0
 */