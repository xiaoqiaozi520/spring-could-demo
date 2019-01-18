//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cn.ymdd.container.web.restful;

import cn.ymdd.api.code.BaseCode;
import cn.ymdd.api.data.Response;
import cn.ymdd.container.runtime.exception.RunningException;
import cn.ymdd.container.runtime.interceptor.ControllerBeforeInterceptor;
import cn.ymdd.container.runtime.profile.RuntimeEnvironment;
import cn.ymdd.container.runtime.spring.MemoryClearApplicationListener;
import cn.ymdd.container.web.main.WebContainer;
import cn.ymdd.framework.toolkit.util.ClassUtil;
import cn.ymdd.framework.toolkit.util.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.netflix.client.config.CommonClientConfigKey;
import com.netflix.client.config.IClientConfig;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.http.ZuulServlet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.cloud.netflix.feign.FeignClientsConfiguration;
import org.springframework.cloud.netflix.feign.ribbon.FeignRibbonClientAutoConfiguration;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.route.RibbonCommandContext;
import org.springframework.cloud.netflix.zuul.filters.route.ZuulFallbackProvider;
import org.springframework.cloud.netflix.zuul.filters.route.apache.HttpClientRibbonCommand;
import org.springframework.cloud.netflix.zuul.filters.route.apache.HttpClientRibbonCommandFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.ControllerAdvice;

@Configuration
@AutoConfigureBefore({WebMvcAutoConfiguration.class})
public class RestfulContainer {
    private static final Logger log = LoggerFactory.getLogger(RestfulContainer.class);

    public RestfulContainer() {
    }

    @Primary
    @Configuration
    @ConditionalOnClass({ZuulServlet.class})
    protected class RestfulWebBalancer extends RestfulBalancer {
        @Autowired(
                required = false
        )
        private Set<ZuulFallbackProvider> zuulFallbackProviders = Collections.emptySet();

        public RestfulWebBalancer() {
            super("error", true);
        }

        @Bean
        public RestfulPreBalancer addRestfulPreBalancer() {
            return new RestfulPreBalancer();
        }

        @Bean
        public RestfulBalancer addRestfulPostBalancer() {
            return new RestfulPostBalancer();
        }

        @Bean
        @Primary
        public HttpClientRibbonCommandFactory addRibbonFactory(SpringClientFactory clientFactory, ZuulProperties zuulProperties) {
            return new HttpClientRibbonCommandFactory(clientFactory, zuulProperties, this.zuulFallbackProviders) {
                public HttpClientRibbonCommand create(RibbonCommandContext context) {
                    HttpClientRibbonCommand command = super.create(context);
                    IClientConfig config = (IClientConfig)ClassUtil.getDeclaredFieldValue(command, "config");
                    config.setProperty(CommonClientConfigKey.ConnectTimeout, RestfulProperties.getClientTimeout()[0]);
                    config.setProperty(CommonClientConfigKey.ReadTimeout, RestfulProperties.getClientTimeout()[1]);
                    return command;
                }
            };
        }

        public Object run() {
            RequestContext ctx = RequestContext.getCurrentContext();
            ctx.setResponseBody(JSON.toJSONString(Response.failure(BaseCode.ERROR)));
            Throwable error = ctx.getThrowable();
            if (error == null) {
                error = new RunningException(BaseCode.ERROR, (Throwable)error);
                ctx.setThrowable((Throwable)error);
            }

            if (RestfulContainer.log.isErrorEnabled()) {
                RestfulContainer.log.error(((Throwable)error).getMessage(), (Throwable)error);
            }

            return null;
        }
    }

    @Primary
    @Configuration
    @ControllerAdvice
    @ConditionalOnMissingBean({RestfulConfiguration.class})
    @AutoConfigureBefore({FeignRibbonClientAutoConfiguration.class, FeignClientsConfiguration.class})
    protected class RestfulWebConfiguration extends RestfulConfiguration {
        public RestfulWebConfiguration() {
            if (log.isInfoEnabled()) {
                log.info("[ GULUDAI ] Inited restful web configurator...");
            }

        }

        protected List<ControllerBeforeInterceptor> addControllerInterceptor() {
            Map<String, ControllerBeforeInterceptor> listeners = RuntimeEnvironment.getBeansByType(ControllerBeforeInterceptor.class);
            return (List)(CollectionUtil.isNotEmpty(listeners) ? new ArrayList(listeners.values()) : Collections.EMPTY_LIST);
        }
    }

    @Configuration
    protected class RestfulWebContainer extends WebContainer {
        public RestfulWebContainer() {
            if (RestfulContainer.log.isInfoEnabled()) {
                RestfulContainer.log.info("[ GULUDAI ] Inited restful web container...");
            }

        }

        @Bean
        protected MemoryClearApplicationListener addApplicationListener() {
            return new MemoryClearApplicationListener();
        }
    }
}
