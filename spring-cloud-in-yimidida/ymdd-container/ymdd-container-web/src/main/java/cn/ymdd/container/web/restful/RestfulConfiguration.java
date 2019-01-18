package cn.ymdd.container.web.restful;

import cn.ymdd.api.Code;
import cn.ymdd.api.code.BaseCode;
import cn.ymdd.api.data.Response;
import cn.ymdd.container.runtime.auth.AuthorityInterceptor;
import cn.ymdd.container.runtime.auth.AuthorityResolver;
import cn.ymdd.container.runtime.exception.RunningException;
import cn.ymdd.container.runtime.fence.HttpFenceChain;
import cn.ymdd.container.runtime.fence.HttpFenceContent;
import cn.ymdd.container.runtime.fence.HttpFenceContext;
import cn.ymdd.container.runtime.fence.HttpFenceHeader;
import cn.ymdd.container.runtime.fence.HttpFenceTamper;
import cn.ymdd.container.runtime.interceptor.ControllerBeforeInterceptor;
import cn.ymdd.container.runtime.interceptor.ControllerBeforeResult;
import cn.ymdd.container.runtime.interceptor.ControllerRoundInterceptor;
import cn.ymdd.container.runtime.log.AccessLogFence;
import cn.ymdd.container.runtime.profile.RuntimeEnvironment;
import cn.ymdd.container.runtime.session.SessionInterceptor;
import cn.ymdd.container.runtime.standard.StandardInterceptor;
import cn.ymdd.container.runtime.time.TimeInterceptor;
import cn.ymdd.framework.toolkit.util.CollectionUtil;
import cn.ymdd.framework.toolkit.util.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.support.spring.FastJsonJsonView;
import com.google.common.collect.Lists;
import feign.Request.Options;
import feign.RequestTemplate;
import feign.Retryer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.MethodParameter;
import org.springframework.core.NamedThreadLocal;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

abstract class RestfulConfiguration extends WebMvcConfigurationSupport {
    protected static final String ERROR = "/error";
    protected static final String WRONG = "/wrong";
    protected static final AntPathMatcher matcher = new AntPathMatcher();
    protected static final Logger log = LoggerFactory.getLogger(RestfulConfiguration.class);

    protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.addAll(RestfulConverter.ALL_CONVERTER);
    }

    @Bean
    @Primary
    public RequestMappingHandlerAdapter requestMappingHandlerAdapter() {
        return super.requestMappingHandlerAdapter();
    }

    @Bean
    @Primary
    public HttpMessageConverters addMessageConverters() {
        return new HttpMessageConverters(false, RestfulConverter.ALL_CONVERTER);
    }

    @Bean
    @Primary
    @Autowired
    public ObjectFactory<HttpMessageConverters> addMessageFactory(HttpMessageConverters converters) {
        return (ObjectFactory) () -> converters;
    }

    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TimeInterceptor());
        registry.addInterceptor(new StandardInterceptor());
        registry.addInterceptor(new AuthorityInterceptor());
        registry.addInterceptor(new HandlerInterceptorAdapter() {
            public Object preData(HandlerMethod handler) throws Exception {
                HttpFenceContent content = HttpFenceContext.getUpHttpFenceContent();
                if ((content == null) || (!content.hasBody())) {
                    return null;
                }
                if (content.getData() == null) {
                    for (MethodParameter arg : handler.getMethodParameters()) {
                        ResolvableType targetType = ResolvableType.forType(arg.getNestedGenericParameterType());
                        boolean argResponse = targetType.isAssignableFrom(HttpServletResponse.class);
                        boolean argRequest = targetType.isAssignableFrom(HttpServletRequest.class);
                        boolean argModel = targetType.isAssignableFrom(Model.class);
                        if ((!argModel) && (!argRequest) && (!argResponse)) {
                            Object target = JSON.parseObject(content.getBodies(), targetType.getRawClass());
                            content.setData(((RestfulConverter) RestfulConverter.JSON_CONVERTER).read(targetType, target));
                            content.setClazz(targetType.getRawClass());
                        }
                    }
                }
                return content.getData();
            }

            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                try {
                    String uri = request.getRequestURI();
                    String web = request.getContextPath();
                    for (String url : Arrays.asList(new String[]{web + "/error", web + "/wrong"})) {
                        if (RestfulConfiguration.matcher.match(url, uri)) {
                            return true;
                        }
                    }
                    List<ControllerBeforeInterceptor> interceptors = RestfulConfiguration.this.addControllerInterceptor();
                    if (CollectionUtil.isEmpty(interceptors)) {
                        return true;
                    }
                    boolean ignore = false;
                    Object data = preData((HandlerMethod) handler);
                    for (ControllerBeforeInterceptor interceptor : interceptors) {
                        List white = interceptor.getIgnoreUri();
                        Iterator localIterator3;
                        if (CollectionUtil.isNotEmpty(white))
                            for (localIterator3 = white.iterator(); localIterator3.hasNext(); ) {
                                if (RestfulConfiguration.matcher.match((String) localIterator3.next(), uri)) {
                                    ignore = true;
                                    break;
                                }
                            }
                        String url;
                        if (ignore) {
                            continue;
                        }
                        List<String> black = interceptor.getForbidUri();
                        if (CollectionUtil.isNotEmpty(black)) {
                            black.forEach(blackUrl -> {
                                if (RestfulConfiguration.matcher.match(blackUrl, uri)) {
                                    throw new RunningException(BaseCode.FORBIDDEN, true);
                                }
                            });
                        }
                        ControllerBeforeResult result = interceptor.preHandle(request, response, data);
                        if (result.isFailure()) {
                            throw new RunningException(result.getCode(), result.getMessage(), result.isIgnore());
                        }
                    }
                    return true;
                } catch (Exception e) {
                    if (!(e instanceof RunningException))
                        throw new RunningException(BaseCode.ERROR, e);
                    return false;
                }

            }

            public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
                    throws Exception {
                try {
                    for (ControllerBeforeInterceptor interceptor : RestfulConfiguration.this.addControllerInterceptor())
                        if ((interceptor instanceof ControllerRoundInterceptor))
                            ((ControllerRoundInterceptor) interceptor).postHandle(request, response, modelAndView);
                } catch (Exception e) {
                    if (!(e instanceof RunningException)) {
                        throw new RunningException(BaseCode.ERROR, e);
                    }
                    throw e;
                }
            }

            public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
                try {
                    for (ControllerBeforeInterceptor interceptor : RestfulConfiguration.this.addControllerInterceptor())
                        if ((interceptor instanceof ControllerRoundInterceptor))
                            ((ControllerRoundInterceptor) interceptor).afterCompletion(request, response, ex);
                } catch (Exception e) {
                    if (!(e instanceof RunningException)) {
                        throw new RunningException(BaseCode.ERROR, e);
                    }
                    throw e;
                }
            }
        });
        super.addInterceptors(registry);
    }

    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        try {
            Map defaults = RestfulProperties.RESTFUL_STATIC_RESOURCE_VALUE;
            String resources = RuntimeEnvironment.getProperties("spring.static.resources");
            if (StringUtil.isNotEmpty(resources).booleanValue()) {
                for (String resource : StringUtil.split(resources, ",")) {
                    defaults.put("/" + resource + "/**", new String[]{"classpath:/" + resource + "/"});
                }
            }
            if (CollectionUtil.isNotEmpty(defaults))
                for (String as = defaults.entrySet().iterator(); ((Iterator) ? ??).hasNext(); ){
                Map.Entry entry = (Map.Entry) ((Iterator) ? ??).next();
                registry.addResourceHandler(new String[]{(String) entry.getKey()}).addResourceLocations((String[]) entry.getValue());
            }
        } catch (Exception e) {
            if (log.isDebugEnabled())
                log.debug("[ GULUDAI ] Resource error '" + e.getMessage() + "'", e);
        }
    }

    protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new AuthorityResolver());
        super.addArgumentResolvers(argumentResolvers);
    }

    protected abstract List<ControllerBeforeInterceptor> addControllerInterceptor();

    @Bean
    @Primary
    protected SessionInterceptor addSessionInterceptor() {
        return new SessionInterceptor() {
            protected boolean isPropagateHeader(RequestTemplate template) {
                return true;
            }
        };
    }

    @Bean
    @Order(-2147483648)
    protected FilterRegistrationBean addFenceRegistration() {
        FilterRegistrationBean filter = new FilterRegistrationBean();
        List chains = Lists.newArrayList();
        HttpFenceTamper tamper = new HttpFenceTamper();
        addFenceChains(chains);
        tamper.addFenceChains(chains);
        filter.addUrlPatterns(new String[]{"/*"});
        filter.setOrder(-2147483648);
        filter.setFilter(tamper);
        return filter;
    }

    protected void addFenceChains(List<HttpFenceChain> chains) {
        chains.add(new AccessLogFence("Access"));
    }

    @Bean
    @Primary
    public Request.Options addClientOptions() {
        Integer[] timeout = RestfulProperties.getClientTimeout();
        return new Request.Options(timeout[0].intValue(), timeout[1].intValue());
    }

    @Bean
    @Primary
    protected Retryer addClientRetryer() {
        return Retryer.NEVER_RETRY;
    }

    @ExceptionHandler({Exception.class})
    protected ModelAndView addExceptionHandler(HttpServletRequest request, Exception e) {
        boolean ignore = false;
        try {
            Code code;
            Code code;
            if ((e instanceof RunningException)) {
                ignore = ((RunningException) e).isIgnoreException();
                code = ((RunningException) e).getExceptionCode();
            } else {
                Code code;
                if ((e instanceof JSONException))
                    code = BaseCode.BADREQUEST;
                else
                    code = BaseCode.ERROR;
            }
            Map model = new HashMap(code) {
            };
            if (MediaType.APPLICATION_JSON.includes(HttpFenceHeader.getHttpFenceMediaType(request))) {
                localModelAndView = new ModelAndView(new FastJsonJsonView(), model);
                return localModelAndView;
            }
            RestfulController.context.set(code);
            ModelAndView localModelAndView = new ModelAndView("forward:/error", model);
            return localModelAndView;
        } finally {
            if ((!ignore) &&
                    (log.isErrorEnabled()))
                log.error("[ GULUDAI ] Restful error '" + e.getMessage() + "'", e);
        }
        throw localObject;
    }

    @Primary
    @Controller
    protected static class RestfulController
            implements ErrorController {
        static final NamedThreadLocal<Code> context = new NamedThreadLocal("error");

        static final Code getErrorCode(int code) {
            HttpStatus status = HttpStatus.valueOf(code == 0 ? 500 : code);
            if (context.get() == null)
                return new Code(status) {
                    public String getMessage() {
                        return this.val$status.getReasonPhrase();
                    }

                    public String getCode() {
                        return String.valueOf(this.val$status.value());
                    }
                };
            return (Code) context.get();
        }

        @ResponseBody
        @RequestMapping(path = {"/wrong"})
        public <T> Response<T> getErrorJson(HttpServletRequest request, HttpServletResponse response) {
            try {
                response.setHeader("Content-Type", HttpFenceHeader.getHttpFenceHeaderType(request));
                Response localResponse = Response.failure(getErrorCode(response.getStatus()));
                return localResponse;
            } finally {
                context.remove();
            }
            throw localObject;
        }

        @RequestMapping({"/error"})
        public String getErrorHtml(HttpServletRequest request, HttpServletResponse response, Model model) {
            MediaType contentType = HttpFenceHeader.getHttpFenceMediaType(request);
            if (MediaType.APPLICATION_JSON.isCompatibleWith(contentType))
                return "forward:/wrong";
            try {
                Code code = getErrorCode(response.getStatus());
                HttpStatus status = HttpStatus.valueOf(Integer.parseInt(code.getCode()));
                model.addAttribute("message", code.getMessage());
                model.addAttribute("code", code.getCode());
                if (status.is4xxClientError()) {
                    str = "error/40X";
                    return str;
                }
                String str = "error/50X";
                return str;
            } finally {
                context.remove();
            }
            throw localObject;
        }

        public String getErrorPath() {
            return "/error";
        }
    }
}