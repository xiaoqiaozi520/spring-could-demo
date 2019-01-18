//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cn.ymdd.container.runtime.standard;

import cn.ymdd.api.code.BaseCode;
import cn.ymdd.container.runtime.exception.RunningException;
import cn.ymdd.container.runtime.fence.HttpFenceHeader;
import cn.ymdd.container.runtime.profile.RuntimeEnvironment;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public final class StandardInterceptor extends HandlerInterceptorAdapter {
    public static final String ACTIVEATE = "spring.standard.active";
    private static final Logger log = LoggerFactory.getLogger(StandardInterceptor.class);
    private boolean activated = Boolean.parseBoolean(RuntimeEnvironment.getProperties("spring.standard.active", "false"));
    private final List<Object> otherPattern = new ArrayList();
    private final List<Object> jsonPattern = new ArrayList();
    private final AntPathMatcher matcher = new AntPathMatcher();

    public StandardInterceptor() {
        try {
            Configuration cnf = new PropertiesConfiguration("standard.cnf");
            this.otherPattern.addAll(cnf.getList("standard.html"));
            this.otherPattern.addAll(cnf.getList("standard.skip"));
            this.jsonPattern.addAll(cnf.getList("standard.json"));
            if (this.otherPattern.isEmpty()) {
                throw new NullPointerException("path");
            }

            if (this.jsonPattern.isEmpty()) {
                throw new NullPointerException("json");
            }
        } catch (ConfigurationException var2) {
            if (log.isErrorEnabled()) {
                log.error(var2.getMessage(), var2);
            }
        }

    }

    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object obj) throws Exception {
        if (this.activated) {
            String uri = req.getRequestURI();
            Iterator var5 = this.otherPattern.iterator();

            Object json;
            do {
                if (!var5.hasNext()) {
                    var5 = this.jsonPattern.iterator();

                    while(var5.hasNext()) {
                        json = var5.next();
                        if (this.matcher.match((String)json, uri)) {
                            MediaType type = HttpFenceHeader.getHttpFenceMediaType(req);
                            if (MediaType.APPLICATION_JSON.includes(type)) {
                                return true;
                            }

                            if (MediaType.MULTIPART_FORM_DATA.includes(type)) {
                                return true;
                            }
                        }
                    }

                    throw new RunningException(BaseCode.FORBIDDEN, "Error 'uri' type [" + uri + "]");
                }

                json = var5.next();
            } while(!this.matcher.match((String)json, uri));

            return true;
        } else {
            return true;
        }
    }
}
