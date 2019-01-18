//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cn.ymdd.container.runtime.fence;

import cn.ymdd.api.code.BaseCode;
import cn.ymdd.api.data.Response;
import cn.ymdd.container.runtime.exception.RunningException;
import cn.ymdd.framework.toolkit.util.StringUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.AntPathMatcher;

public final class HttpFenceTamper extends HttpFence {
    public static final String X_FENCE_URL = "/*";
    public static final String X_BIT_CONTENT_TYPE = "image/";
    public static final String FENCE_REQ_ATTRS = "FENCE_REQ_ATTRS";
    public static final String FENCE_RES_ATTRS = "FENCE_RES_ATTRS";
    public static final String X_MULTIPART_FORM_DATA = "multipart/form-data";
    public static final String X_WWW_FORM_URLECODED = "application/x-www-form-urlencoded";
    private static final Logger log = LoggerFactory.getLogger(HttpFenceTamper.class);
    private final List<HttpFenceChain> fenceChains = Lists.newCopyOnWriteArrayList();
    private final List<Object> pattern = new ArrayList();
    private final AntPathMatcher matcher = new AntPathMatcher();

    public final void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            HttpFenceResponse res = null;
            HttpFenceRequest req = null;

            try {
                String id = HttpFenceHeader.getHttpFenceHeaderID(request);
                res = new HttpFenceResponse((HttpServletResponse)response);
                req = new HttpFenceRequest((HttpServletRequest)request);
                HttpFenceContext.setHttpServletContext(req, res);
                boolean ignore = this.handleIgnoreFence(id, req);
                this.handleRequestFence(id, req, ignore);
                chain.doFilter(req, res);
                this.handleResponseFence(id, res, ignore);
                res.finish();
            } catch (Exception var11) {
                if (log.isErrorEnabled()) {
                    log.error(var11.getMessage(), var11);
                }

                if (res.isCommitted()) {
                    throw new RunningException(BaseCode.ERROR, var11);
                }

                PrintWriter pw = res.getWriter();
                pw.write(toErrorString(var11));
                pw.flush();
            } finally {
                HttpFenceContext.delHttpFenceContent();
                if (req != null && res != null) {
                    req.setAttribute("FENCE_RES_ATTRS", res.readBuffer());
                }

            }
        } else {
            chain.doFilter(request, response);
        }

    }

    public HttpFenceTamper() {
        try {
            Configuration cnf = new PropertiesConfiguration("fence.cnf");
            String prefix = cnf.getString("fence.prefix");
            Iterator var3 = cnf.getList("fence.categroy").iterator();

            while(var3.hasNext()) {
                Object obj = var3.next();
                this.pattern.add(StringUtil.join(new Object[]{prefix, obj}));
            }
        } catch (ConfigurationException var5) {
            if (log.isErrorEnabled()) {
                log.error(var5.getMessage(), var5);
            }
        }

    }

    protected boolean handleIgnoreFence(String id, HttpFenceRequest req) {
        String uri = req.getRequestURI();
        Iterator var4 = this.pattern.iterator();

        Object parn;
        do {
            if (!var4.hasNext()) {
                return false;
            }

            parn = var4.next();
        } while(!this.matcher.match((String)parn, uri));

        HttpFenceContext.setUpHttpFenceContent(new HttpFenceContent(id));
        return true;
    }

    protected void handleRequestFence(String id, HttpFenceRequest req, boolean ignore) {
        try {
            if (StringUtil.isEmpty(req.getCharacterEncoding())) {
                req.setCharacterEncoding("UTF-8");
            }

            if (this.fenceChains.isEmpty()) {
                return;
            }

            HttpFenceContent content = new HttpFenceContent();
            Enumeration headers = req.getHeaderNames();

            while(headers.hasMoreElements()) {
                String name = (String)headers.nextElement();
                content.addHeaders(name, req.getHeader(name));
            }

            content.setId(id);
            content.setIp(req.getRemoteAddr());
            content.setUri(req.getRequestURI());
            content.setMethod(req.getMethod());
            content.setProtocol(req.getProtocol());
            content.setDomain(req.getServerName());
            content.setParameters(req.getParameterMap());
            if (!ignore) {
                byte[] body = req.readBuffer();
                content.setSize(body == null ? 0L : (long)body.length);
                content.setBodies(toBitString(body));
            }

            HttpFenceContext.setUpHttpFenceContent(content);
            Iterator var10 = this.fenceChains.iterator();

            while(var10.hasNext()) {
                HttpFenceChain fc = (HttpFenceChain)var10.next();
                fc.requestHttpFence(content);
            }
        } catch (Exception var8) {
            if (log.isErrorEnabled()) {
                log.error(var8.getMessage(), var8);
            }
        }

    }

    protected void handleResponseFence(String id, HttpFenceResponse res, boolean ignore) {
        try {
            if (StringUtil.isEmpty(res.getCharacterEncoding())) {
                res.setCharacterEncoding("UTF-8");
            }

            if (this.fenceChains.isEmpty()) {
                return;
            }

            HttpFenceContent content = new HttpFenceContent();
            Iterator header = res.getHeaderNames().iterator();

            while(header.hasNext()) {
                String name = (String)header.next();
                content.addHeaders(name, res.getHeader(name));
            }

            content.setHttp(res.getStatus());
            content.setId(id);
            if (!ignore) {
                byte[] body = res.readBuffer();
                content.setSize(body == null ? 0L : (long)body.length);
                content.setBodies(toBitString(body));
            }

            HttpFenceContext.setDownHttpFenceContent(content);
            Iterator var10 = this.fenceChains.iterator();

            while(var10.hasNext()) {
                HttpFenceChain fc = (HttpFenceChain)var10.next();
                fc.responseHttpFence(content);
            }
        } catch (Exception var8) {
            if (log.isErrorEnabled()) {
                log.error(var8.getMessage(), var8);
            }
        }

    }

    public void addFenceChains(List<HttpFenceChain> chains) {
        List var2 = this.fenceChains;
        synchronized(this.fenceChains) {
            this.fenceChains.addAll(chains);
        }
    }

    static final String toErrorString(Exception e) {
        return e instanceof RunningException ? JSON.toJSONString(Response.failure(((RunningException)e).getExceptionCode())) : JSON.toJSONString(Response.failure(BaseCode.ERROR));
    }

    static final String toBitString(byte[] bits) throws UnsupportedEncodingException {
        return bits != null && bits.length > 0 ? new String(bits, "UTF-8") : "";
    }

    static final boolean isUrlEncoded(HttpServletRequest request) {
        String contentType = request.getContentType();
        return "POST".equalsIgnoreCase(request.getMethod()) && contentType != null && (contentType.startsWith("application/x-www-form-urlencoded") || contentType.startsWith("multipart/form-data"));
    }

    static final boolean isBitResponse(HttpServletResponse response) {
        String responseType = response.getContentType();
        return responseType != null && responseType.startsWith("image/");
    }
}
