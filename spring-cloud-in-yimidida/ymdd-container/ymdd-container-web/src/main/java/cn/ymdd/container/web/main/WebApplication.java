//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cn.ymdd.container.web.main;

import ch.qos.logback.classic.PatternLayout;
import cn.ymdd.container.runtime.log.AccessId;
import cn.ymdd.container.runtime.profile.RuntimeEnvironment;
import java.util.Iterator;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

@EnableAutoConfiguration(
        exclude = {RedisAutoConfiguration.class}
)
public abstract class WebApplication implements CommandLineRunner, ApplicationContextAware {
    private static final Logger log = LoggerFactory.getLogger(WebApplication.class);

    public WebApplication() {
    }

    public void run(String... args) throws Exception {
        Map<String, WebListener> listeners = RuntimeEnvironment.getBeansByType(WebListener.class);
        Iterator var3 = listeners.values().iterator();

        while(var3.hasNext()) {
            WebListener listener = (WebListener)var3.next();
            listener.initialize();
        }

        if (log.isInfoEnabled()) {
            log.info("Start [ " + RuntimeEnvironment.getProperties("spring.application.name") + " ] service successful...");
        }

    }

    public final void setApplicationContext(ApplicationContext applicationContext) {
        RuntimeEnvironment.initProfile(applicationContext);
    }

    static {
        PatternLayout.defaultConverterMap.put("id", AccessId.class.getName());
    }
}
