//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cn.ymdd.container.runtime.spring;

import com.google.common.collect.Maps;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public final class MemoryClearApplicationListener implements ApplicationListener<ContextRefreshedEvent>, ApplicationContextAware {
    private final Map<String, MemoryClearEvent> events = Maps.newConcurrentMap();

    public MemoryClearApplicationListener() {
    }

    public void onApplicationEvent(ContextRefreshedEvent event) {
        Iterator var2 = this.events.values().iterator();

        while(var2.hasNext()) {
            MemoryClearEvent evt = (MemoryClearEvent)var2.next();
            evt.clearMemory();
        }

        ClassUtilCleaner.clearMemory();
        this.clearClassLoaderCaches(Thread.currentThread().getContextClassLoader());
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map var2 = this.events;
        synchronized(this.events) {
            this.events.putAll(applicationContext.getBeansOfType(MemoryClearEvent.class));
        }
    }

    private void clearClassLoaderCaches(ClassLoader classLoader) {
        if (classLoader != null) {
            try {
                Method clearCacheMethod = classLoader.getClass().getDeclaredMethod("clearMemory");
                clearCacheMethod.invoke(classLoader);
            } catch (Exception var3) {
                ;
            }

            this.clearClassLoaderCaches(classLoader.getParent());
        }

    }
}

