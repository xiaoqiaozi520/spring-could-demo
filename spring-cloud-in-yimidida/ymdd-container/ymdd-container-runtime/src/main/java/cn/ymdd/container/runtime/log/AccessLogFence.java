package cn.ymdd.container.runtime.log;

import cn.ymdd.container.runtime.fence.HttpFenceChain;
import cn.ymdd.container.runtime.fence.HttpFenceContent;
import cn.ymdd.container.runtime.profile.RuntimeEnvironment;
import cn.ymdd.container.runtime.profile.RuntimeProfile;
import com.alibaba.fastjson.JSON;
import java.text.SimpleDateFormat;
import org.slf4j.LoggerFactory;

public class AccessLogFence
        implements HttpFenceChain
{
    public static final String ACCESS_LOGGER_NAME = "Access";
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss SSS";
    private final org.slf4j.Logger log;

    public AccessLogFence(String loggerName)
    {
        this.log = LoggerFactory.getLogger(loggerName);
    }

    private static void AccessLogReset(org.slf4j.Logger log) {
        if (!(log instanceof ch.qos.logback.classic.Logger)) {
            throw new IllegalStateException("Not support " + log.getClass() + " logger");
        }
        ch.qos.logback.classic.Logger lg = (ch.qos.logback.classic.Logger)log;
        if (lg.isAdditive()) {
            RuntimeProfile profile = RuntimeEnvironment.getProfile();
            if ((profile != null) &&
                    (RuntimeProfile.LOCAL != profile) &&
                    ((log instanceof ch.qos.logback.classic.Logger)))
                ((ch.qos.logback.classic.Logger)log).setAdditive(false);
        }
    }

    public void requestHttpFence(HttpFenceContent content)
    {
        AccessRequestLog requestLog = new AccessRequestLog();
        requestLog.requestTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS").format(Long.valueOf(System.currentTimeMillis()));
        requestLog.requestParamters = content.getParameters();
        requestLog.requestProtocol = content.getProtocol();
        requestLog.requestHeaders = content.getHeaders();
        requestLog.requestMethod = content.getMethod();
        requestLog.requestDomain = content.getDomain();
        requestLog.requestBody = content.getBodies();
        requestLog.requestSize = content.getSize();
        requestLog.requestUri = content.getUri();
        requestLog.requestIp = content.getIp();
        requestLog.id = String.valueOf(content.getId());
        org.slf4j.Logger lg = this.log;
        if (lg == null) {
            lg = LoggerFactory.getLogger(AccessLogFence.class);
        }
        if (!"Access".equals(lg.getName())) {
            throw new NullPointerException("Not found 'Access' name logger");
        }
        AccessLogReset(lg);
        if (lg.isInfoEnabled())
            lg.info(JSON.toJSONString(requestLog));
    }

    public void responseHttpFence(HttpFenceContent content)
    {
        AccessResponseLog responseLog = new AccessResponseLog();
        responseLog.responseTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS").format(Long.valueOf(System.currentTimeMillis()));
        responseLog.responseHeaders = content.getHeaders();
        responseLog.responseBody = content.getBodies();
        responseLog.responseSize = content.getSize();
        responseLog.responseCode = content.getHttp();
        responseLog.id = String.valueOf(content.getId());
        org.slf4j.Logger lg = this.log;
        if (lg == null) {
            lg = LoggerFactory.getLogger(AccessLogFence.class);
        }
        if (!"Access".equals(lg.getName())) {
            throw new NullPointerException("Not found 'Access' name logger");
        }
        AccessLogReset(lg);
        if (lg.isInfoEnabled())
            lg.info(JSON.toJSONString(responseLog));
    }
}

 