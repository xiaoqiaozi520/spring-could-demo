package cn.ymdd.container.runtime.log;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import cn.ymdd.container.runtime.fence.HttpFenceContent;
import cn.ymdd.container.runtime.fence.HttpFenceContext;

public final class AccessId extends ClassicConverter
{
    public String convert(ILoggingEvent event)
    {
        HttpFenceContent content = HttpFenceContext.getUpHttpFenceContent();
        if (content == null) {
            content = HttpFenceContext.getDownHttpFenceContent();
        }
        if (content != null) {
            return content.getId();
        }
        return "main";
    }
}

