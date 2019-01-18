//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cn.ymdd.container.runtime.auth;

import cn.ymdd.api.code.BaseCode;
import cn.ymdd.container.runtime.exception.RunningException;
import com.alibaba.fastjson.JSON;
import java.nio.charset.Charset;
import java.util.Base64;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public final class AuthorityProcessor extends HandlerInterceptorAdapter {
    private static final Logger log = LoggerFactory.getLogger(AuthorityProcessor.class);

    public AuthorityProcessor() {
    }

    public final boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String auth = request.getHeader("X-Authority");
        if (auth != null && !auth.isEmpty()) {
            try {
                String ras = new String(Base64.getDecoder().decode(auth), Charset.forName("UTF-8"));
                AuthoritySession session = (AuthoritySession)JSON.parseObject(ras, AuthoritySession.class);
                AuthorityContext.setAuthoritySession(session);
                if (log.isDebugEnabled()) {
                    log.debug("[ GULUDAI ] authority session [ " + ras + " ]");
                }

                return true;
            } catch (Exception var7) {
                if (log.isErrorEnabled()) {
                    log.error(var7.getMessage(), var7);
                }

                throw new RunningException(BaseCode.FORBIDDEN, var7);
            }
        } else {
            if (log.isDebugEnabled()) {
                log.debug("[ GULUDAI ] authority session is empty.");
            }

            return true;
        }
    }

    public final void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        AuthorityContext.delAuthorityContext();
    }
}
