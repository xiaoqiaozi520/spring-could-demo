/*     */ package cn.guludai.container.web.restful;
/*     */ 
/*     */ import cn.guludai.api.Code;
/*     */ import cn.guludai.api.code.BaseCode;
/*     */ import cn.guludai.api.data.Response;
/*     */ import cn.guludai.container.runtime.auth.AuthorityInterceptor;
/*     */ import cn.guludai.container.runtime.auth.AuthorityResolver;
/*     */ import cn.guludai.container.runtime.exception.RunningException;
/*     */ import cn.guludai.container.runtime.fence.HttpFenceChain;
/*     */ import cn.guludai.container.runtime.fence.HttpFenceContent;
/*     */ import cn.guludai.container.runtime.fence.HttpFenceContext;
/*     */ import cn.guludai.container.runtime.fence.HttpFenceHeader;
/*     */ import cn.guludai.container.runtime.fence.HttpFenceTamper;
/*     */ import cn.guludai.container.runtime.interceptor.ControllerBeforeInterceptor;
/*     */ import cn.guludai.container.runtime.interceptor.ControllerRoundInterceptor;
/*     */ import cn.guludai.container.runtime.log.AccessLogFence;
/*     */ import cn.guludai.container.runtime.profile.RuntimeEnvironment;
/*     */ import cn.guludai.container.runtime.session.SessionInterceptor;
/*     */ import cn.guludai.container.runtime.standard.StandardInterceptor;
/*     */ import cn.guludai.container.runtime.time.TimeInterceptor;
/*     */ import cn.guludai.framework.toolkit.util.CollectionUtil;
/*     */ import cn.guludai.framework.toolkit.util.StringUtil;
/*     */ import com.alibaba.fastjson.JSON;
/*     */ import com.alibaba.fastjson.JSONException;
/*     */ import com.alibaba.fastjson.support.spring.FastJsonJsonView;
/*     */ import com.google.common.collect.Lists;
/*     */ import feign.Request.Options;
/*     */ import feign.RequestTemplate;
/*     */ import feign.Retryer;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.ObjectFactory;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.boot.autoconfigure.web.ErrorController;
/*     */ import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
/*     */ import org.springframework.boot.web.servlet.FilterRegistrationBean;
/*     */ import org.springframework.context.annotation.Bean;
/*     */ import org.springframework.context.annotation.Primary;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.NamedThreadLocal;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.core.annotation.Order;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.converter.HttpMessageConverter;
/*     */ import org.springframework.stereotype.Controller;
/*     */ import org.springframework.ui.Model;
/*     */ import org.springframework.util.AntPathMatcher;
/*     */ import org.springframework.web.bind.annotation.ExceptionHandler;
/*     */ import org.springframework.web.bind.annotation.RequestMapping;
/*     */ import org.springframework.web.bind.annotation.ResponseBody;
/*     */ import org.springframework.web.method.HandlerMethod;
/*     */ import org.springframework.web.method.support.HandlerMethodArgumentResolver;
/*     */ import org.springframework.web.servlet.ModelAndView;
/*     */ import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
/*     */ import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistration;
/*     */ import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
/*     */ import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
/*     */ import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
/*     */ import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
/*     */ 
/*     */ abstract class RestfulConfiguration extends WebMvcConfigurationSupport
/*     */ {
/*     */   protected static final String ERROR = "/error";
/*     */   protected static final String WRONG = "/wrong";
/*  86 */   protected static final AntPathMatcher matcher = new AntPathMatcher();
/*  87 */   protected static final Logger log = LoggerFactory.getLogger(RestfulConfiguration.class);
/*     */ 
/*     */   protected void configureMessageConverters(List<HttpMessageConverter<?>> converters)
/*     */   {
/*  91 */     converters.addAll(RestfulConverter.ALL_CONVERTER);
/*     */   }
/*     */   @Bean
/*     */   @Primary
/*     */   public RequestMappingHandlerAdapter requestMappingHandlerAdapter() {
/*  98 */     return super.requestMappingHandlerAdapter();
/*     */   }
/* 104 */   @Bean
/*     */   @Primary
/*     */   public HttpMessageConverters addMessageConverters() { return new HttpMessageConverters(false, RestfulConverter.ALL_CONVERTER); } 
/* 111 */   @Bean
/*     */   @Primary
/*     */   @Autowired
/*     */   public ObjectFactory<HttpMessageConverters> addMessageFactory(HttpMessageConverters converters) { return new ObjectFactory(converters)
/*     */     {
/*     */       public HttpMessageConverters getObject() throws BeansException {
/* 114 */         return this.val$converters;
/*     */       }
/*     */     }; }
/*     */ 
/*     */   protected void addInterceptors(InterceptorRegistry registry)
/*     */   {
/* 121 */     registry.addInterceptor(new TimeInterceptor());
/* 122 */     registry.addInterceptor(new StandardInterceptor());
/* 123 */     registry.addInterceptor(new AuthorityInterceptor());
/* 124 */     registry.addInterceptor(new HandlerInterceptorAdapter()
/*     */     {
/*     */       public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
/*     */       {
/*     */         try {
/* 129 */           List interceptors = RestfulConfiguration.this.addControllerInterceptor();
/* 130 */           if (CollectionUtil.isEmpty(interceptors)) {
/* 131 */             return super.preHandle(request, response, handler);
/*     */           }
/* 133 */           HttpFenceContent content = HttpFenceContext.getUpHttpFenceContent();
/*     */           boolean argResponse;
/* 134 */           if ((content != null) && (content.getData() == null) && (StringUtil.isNotEmpty(content.getBodies()).booleanValue())) {
/* 135 */             if (HandlerMethod.class.isAssignableFrom(handler.getClass()))
/* 136 */               for (MethodParameter arg : ((HandlerMethod)handler).getMethodParameters()) {
/* 137 */                 ResolvableType targetType = ResolvableType.forType(arg.getNestedGenericParameterType());
/* 138 */                 argResponse = targetType.isAssignableFrom(HttpServletResponse.class);
/* 139 */                 boolean argRequest = targetType.isAssignableFrom(HttpServletRequest.class);
/* 140 */                 boolean argModel = targetType.isAssignableFrom(Model.class);
/* 141 */                 if ((!argModel) && (!argRequest) && (!argResponse)) {
/* 142 */                   Object target = JSON.parseObject(content.getBodies(), targetType.getRawClass());
/* 143 */                   content.setData(((RestfulConverter)RestfulConverter.JSON_CONVERTER).read(targetType, target));
/* 144 */                   content.setClazz(targetType.getRawClass());
/*     */                 }
/*     */               }
/*     */             else {
/* 148 */               throw new IllegalStateException("Not support");
/*     */             }
/*     */           }
/* 151 */           String uri = request.getRequestURI(); String web = request.getContextPath();
/* 152 */           for (String url : Arrays.asList(new String[] { web + "/error", web + "/wrong" })) {
/* 153 */             if (RestfulConfiguration.matcher.match(url, uri)) {
/* 154 */               return true;
/*     */             }
/*     */           }
/* 157 */           for (ControllerBeforeInterceptor interceptor : interceptors) {
/* 158 */             List white = interceptor.preIgnoreWhiteUris();
/* 159 */             if (CollectionUtil.isNotEmpty(white))
/* 160 */               for (argResponse = white.iterator(); argResponse.hasNext(); ) { url = (String)argResponse.next();
/* 161 */                 if (RestfulConfiguration.matcher.match(url, uri))
/* 162 */                   return true;
/*     */               }
/*     */             String url;
/* 166 */             List black = interceptor.preForbidBlackUris();
/* 167 */             if (CollectionUtil.isNotEmpty(black)) {
/* 168 */               for (String url : black) {
/* 169 */                 if (RestfulConfiguration.matcher.match(url, uri)) {
/* 170 */                   throw new RunningException(BaseCode.FORBIDDEN);
/*     */                 }
/*     */               }
/*     */             }
/* 174 */             Object data = content == null ? null : content.getData();
/* 175 */             if (!interceptor.preHandle(request, response, data)) {
/* 176 */               throw new RunningException(BaseCode.FORBIDDEN);
/*     */             }
/*     */           }
/* 179 */           return super.preHandle(request, response, handler);
/*     */         } catch (Exception e) {
/* 181 */           if (!(e instanceof RunningException))
/* 182 */             throw new RunningException(BaseCode.ERROR, e);
/*     */         }
/* 184 */         throw e;
/*     */       }
/*     */ 
/*     */       public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
/*     */         throws Exception
/*     */       {
/*     */         try
/*     */         {
/* 192 */           for (ControllerBeforeInterceptor interceptor : RestfulConfiguration.this.addControllerInterceptor()) {
/* 193 */             if ((interceptor instanceof ControllerRoundInterceptor)) {
/* 194 */               ((ControllerRoundInterceptor)interceptor).postHandle(request, response, modelAndView);
/*     */             }
/*     */           }
/* 197 */           super.postHandle(request, response, handler, modelAndView);
/*     */         } catch (Exception e) {
/* 199 */           if (!(e instanceof RunningException)) {
/* 200 */             throw new RunningException(BaseCode.ERROR, e);
/*     */           }
/* 202 */           throw e;
/*     */         }
/*     */       }
/*     */ 
/*     */       public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception
/*     */       {
/*     */         try
/*     */         {
/* 210 */           for (ControllerBeforeInterceptor interceptor : RestfulConfiguration.this.addControllerInterceptor()) {
/* 211 */             if ((interceptor instanceof ControllerRoundInterceptor)) {
/* 212 */               ((ControllerRoundInterceptor)interceptor).afterCompletion(request, response, ex);
/*     */             }
/*     */           }
/* 215 */           super.afterCompletion(request, response, handler, ex);
/*     */         } catch (Exception e) {
/* 217 */           if (!(e instanceof RunningException)) {
/* 218 */             throw new RunningException(BaseCode.ERROR, e);
/*     */           }
/* 220 */           throw e;
/*     */         }
/*     */       }
/*     */     });
/* 225 */     super.addInterceptors(registry);
/*     */   }
/*     */ 
/*     */   protected void addResourceHandlers(ResourceHandlerRegistry registry)
/*     */   {
/*     */     try {
/* 231 */       Map defaults = RestfulProperties.RESTFUL_STATIC_RESOURCE_VALUE;
/* 232 */       String resources = RuntimeEnvironment.getProperties("spring.static.resources");
/* 233 */       if (StringUtil.isNotEmpty(resources).booleanValue()) {
/* 234 */         for (String resource : StringUtil.split(resources, ",")) {
/* 235 */           defaults.put("/" + resource + "/**", new String[] { "classpath:/" + resource + "/" });
/*     */         }
/*     */       }
/* 238 */       if (CollectionUtil.isNotEmpty(defaults))
/* 239 */         for (??? = defaults.entrySet().iterator(); ((Iterator)???).hasNext(); ) { Map.Entry entry = (Map.Entry)((Iterator)???).next();
/* 240 */           registry.addResourceHandler(new String[] { (String)entry.getKey() }).addResourceLocations((String[])entry.getValue()); }
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 244 */       if (log.isDebugEnabled())
/* 245 */         log.debug("[ GULUDAI ] Resource error '" + e.getMessage() + "'", e);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers)
/*     */   {
/* 252 */     argumentResolvers.add(new AuthorityResolver());
/* 253 */     super.addArgumentResolvers(argumentResolvers); } 
/*     */   protected abstract List<ControllerBeforeInterceptor> addControllerInterceptor();
/*     */ 
/* 261 */   @Bean
/*     */   @Primary
/*     */   protected SessionInterceptor addSessionInterceptor() { return new SessionInterceptor()
/*     */     {
/*     */       protected boolean isPropagateHeader(RequestTemplate template) {
/* 264 */         return true;
/*     */       } } ; }
/*     */ 
/*     */   @Bean
/*     */   @Order(-2147483648)
/*     */   protected FilterRegistrationBean addFenceRegistration() {
/* 272 */     FilterRegistrationBean filter = new FilterRegistrationBean();
/* 273 */     List chains = Lists.newArrayList();
/* 274 */     HttpFenceTamper tamper = new HttpFenceTamper();
/* 275 */     addFenceChains(chains);
/* 276 */     tamper.addFenceChains(chains);
/* 277 */     filter.addUrlPatterns(new String[] { "/*" });
/* 278 */     filter.setOrder(-2147483648);
/* 279 */     filter.setFilter(tamper);
/* 280 */     return filter;
/*     */   }
/*     */ 
/*     */   protected void addFenceChains(List<HttpFenceChain> chains) {
/* 284 */     chains.add(new AccessLogFence("Access"));
/*     */   }
/* 290 */   @Bean
/*     */   @Primary
/*     */   public Request.Options addClientOptions() { Integer[] timeout = RestfulProperties.getClientTimeout();
/* 291 */     return new Request.Options(timeout[0].intValue(), timeout[1].intValue()); } 
/*     */   @Bean
/*     */   @Primary
/*     */   protected Retryer addClientRetryer() {
/* 297 */     return Retryer.NEVER_RETRY;
/*     */   }
/*     */ 
/*     */   @ExceptionHandler({Exception.class})
/*     */   protected ModelAndView addExceptionHandler(HttpServletRequest request, Exception e)
/*     */   {
/*     */     try
/*     */     {
/*     */       Code code;
/*     */       Code code;
/* 305 */       if ((e instanceof RunningException)) {
/* 306 */         code = ((RunningException)e).getExceptionCode();
/*     */       }
/*     */       else
/*     */       {
/*     */         Code code;
/* 307 */         if ((e instanceof JSONException))
/* 308 */           code = BaseCode.BADREQUEST;
/*     */         else
/* 310 */           code = BaseCode.ERROR;
/*     */       }
/* 312 */       Map model = new HashMap(code)
/*     */       {
/*     */       };
/* 319 */       if (MediaType.APPLICATION_JSON.includes(HttpFenceHeader.getHttpFenceMediaType(request))) {
/* 320 */         localModelAndView = new ModelAndView(new FastJsonJsonView(), model);
/*     */         return localModelAndView;
/*     */       }
/* 322 */       RestfulController.context.set(code);
/* 323 */       ModelAndView localModelAndView = new ModelAndView("forward:/error", model);
/*     */       return localModelAndView;
/*     */     }
/*     */     finally {
/* 326 */       if (log.isErrorEnabled())
/* 327 */         log.error("[ GULUDAI ] Restful error '" + e.getMessage() + "'", e); 
/* 327 */     }throw localObject;
/*     */   }
/*     */ 
/*     */   @Primary
/*     */   @Controller
/*     */   protected static class RestfulController implements ErrorController {
/* 335 */     static final NamedThreadLocal<Code> context = new NamedThreadLocal("error");
/*     */ 
/*     */     static final Code getErrorCode(int code) {
/* 338 */       HttpStatus status = HttpStatus.valueOf(code == 0 ? 500 : code);
/* 339 */       if (context.get() != null) {
/* 340 */         return (Code)context.get();
/*     */       }
/* 342 */       return new Code(status)
/*     */       {
/*     */         public String getMessage()
/*     */         {
/* 346 */           return this.val$status.getReasonPhrase();
/*     */         }
/*     */ 
/*     */         public String getCode()
/*     */         {
/* 351 */           return String.valueOf(this.val$status.value());
/*     */         } } ;
/*     */     }
/*     */ 
/*     */     @ResponseBody
/*     */     @RequestMapping(path={"/wrong"})
/*     */     public <T> Response<T> getErrorJson(HttpServletRequest request, HttpServletResponse response) {
/*     */       try {
/* 361 */         response.setHeader("Content-Type", HttpFenceHeader.getHttpFenceHeaderType(request));
/* 362 */         Response localResponse = Response.failure(getErrorCode(response.getStatus()));
/*     */         return localResponse;
/*     */       } finally {
/* 364 */         context.remove(); } throw localObject;
/*     */     }
/*     */ 
/*     */     @RequestMapping({"/error"})
/*     */     public String getErrorHtml(HttpServletRequest request, HttpServletResponse response, Model model) {
/* 370 */       MediaType contentType = HttpFenceHeader.getHttpFenceMediaType(request);
/* 371 */       if (MediaType.APPLICATION_JSON.isCompatibleWith(contentType))
/* 372 */         return "forward:/wrong";
/*     */       try
/*     */       {
/* 375 */         Code code = getErrorCode(response.getStatus());
/* 376 */         HttpStatus status = HttpStatus.valueOf(Integer.parseInt(code.getCode()));
/* 377 */         model.addAttribute("message", code.getMessage());
/* 378 */         model.addAttribute("code", code.getCode());
/* 379 */         if (status.is4xxClientError()) {
/* 380 */           str = "error/40X";
/*     */           return str;
/*     */         }
/* 382 */         String str = "error/50X";
/*     */         return str;
/*     */       }
/*     */       finally {
/* 385 */         context.remove(); } throw localObject;
/*     */     }
/*     */ 
/*     */     public String getErrorPath()
/*     */     {
/* 391 */       return "/error";
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Users\Administrator\Desktop\container\guludai-container-web\0.0.1-SNAPSHOT\guludai-container-web-0.0.1-20180428.084950-107.jar
 * Qualified Name:     cn.guludai.container.web.restful.RestfulConfiguration
 * JD-Core Version:    0.6.0
 */