//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cn.ymdd.container.runtime.profile;

import cn.ymdd.framework.toolkit.util.StringUtil;
import com.google.common.collect.Lists;

import java.lang.annotation.Annotation;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

public final class RuntimeEnvironment {
    private static volatile RuntimeProfile profile;
    private static volatile ApplicationContext context;
    private static volatile List<Configuration> propers = Lists.newArrayList();
    private static final Logger log = LoggerFactory.getLogger(RuntimeEnvironment.class);

    public RuntimeEnvironment() {
    }

    public static void initProfile(ApplicationContext context) {
        Class var1 = RuntimeEnvironment.class;
        synchronized(RuntimeEnvironment.class) {
            if (context == null) {
                context = context;
                Environment env = context.getEnvironment();
                profile = RuntimeProfile.findRuntimeProfile(env.getActiveProfiles()[0]);
                Iterator var3 = Lists.newArrayList(new String[]{"", profile.getCode()}).iterator();

                while(var3.hasNext()) {
                    String file = (String)var3.next();
                    String fileName = "application";
                    if (StringUtil.isNotEmpty(file)) {
                        if (profile.isNotLocal()) {
                            fileName = StringUtil.join(new String[]{"./config/", fileName, "-", profile.getCode()});
                        } else {
                            fileName = StringUtil.join(new String[]{fileName, "-", profile.getCode()});
                        }
                    }

                    try {
                        fileName = StringUtil.join(new String[]{fileName, ".properties"});
                        PropertiesConfiguration pros = new PropertiesConfiguration();
                        pros.setEncoding("UTF-8");
                        pros.load(fileName);
                        propers.add(pros);
                    } catch (Exception var8) {
                        if (log.isErrorEnabled()) {
                            log.error(var8.getMessage(), var8);
                        }
                    }
                }

            }
        }
    }

    public static RuntimeProfile getProfile() {
        return profile;
    }

    public static String getProperties(String key) {
        String value = context.getEnvironment().getProperty(key);
        if (StringUtil.isEmpty(value)) {
            throw new NullPointerException(key);
        } else {
            return value;
        }
    }

    public static String getProperties(String key, String defaultValue) {
        try {
            String value = getProperties(key);
            return StringUtil.isEmpty(value) ? defaultValue : value;
        } catch (Exception var3) {
            return defaultValue;
        }
    }

    public static String getPropertiesByUtf8(String key) {
        Iterator var1 = propers.iterator();

        String value;
        do {
            if (!var1.hasNext()) {
                throw new NullPointerException(key);
            }

            Configuration cnf = (Configuration)var1.next();
            value = cnf.getString(key);
        } while(!StringUtil.isNotEmpty(value));

        try {
            return new String(value.getBytes("UTF-8"), "UTF-8");
        } catch (Exception var5) {
            throw new IllegalStateException("Unsupported encoding");
        }
    }

    public static String getPropertiesByUtf8(String key, String defaultValue) {
        try {
            String value = getPropertiesByUtf8(key);
            return StringUtil.isEmpty(value) ? defaultValue : value;
        } catch (Exception var3) {
            return defaultValue;
        }
    }

    public static <T> T getBean(Class<T> beanClass) {
        return context.getBean(beanClass);
    }

    public static <T> T getBean(String beanName, Class<T> beanClass) {
        return context.getBean(beanName, beanClass);
    }

    public static <T> Map<String, T> getBeansByType(Class<T> beanClass) {
        return context.getBeansOfType(beanClass);
    }

    public static <T> Map<String, T> getBeansByAnnotation(Class<T> beanClass) {
        return (Map<String, T>) context.getBeansWithAnnotation((Class<? extends Annotation>) beanClass);
    }
}
