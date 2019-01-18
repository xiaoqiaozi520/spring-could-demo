//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cn.ymdd.container.runtime.session;

import cn.ymdd.container.runtime.fence.HttpFenceContent;
import cn.ymdd.container.runtime.fence.HttpFenceContext;
import cn.ymdd.framework.toolkit.util.CollectionUtil;
import cn.ymdd.framework.toolkit.util.StringUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public abstract class SessionInterceptor implements RequestInterceptor {
    public SessionInterceptor() {
    }

    protected abstract boolean isPropagateHeader(RequestTemplate var1);

    public final void apply(RequestTemplate template) {
        if (this.isPropagateHeader(template)) {
            HttpFenceContent content = HttpFenceContext.getUpHttpFenceContent();
            if (content != null) {
                Map<String, String> headers = content.getHeaders();
                if (CollectionUtil.isNotEmpty(headers)) {
                    Iterator var4 = headers.entrySet().iterator();

                    while(var4.hasNext()) {
                        Entry<String, String> header = (Entry)var4.next();
                        if (StringUtil.isNotEmpty((String)header.getValue())) {
                            template.header((String)header.getKey(), new String[]{(String)header.getValue()});
                        }
                    }
                }
            }
        }

        Map<String, Collection<String>> org = template.headers();
        template.header("X-Fence-Id", new String[]{HttpFenceContext.getId()});
        if (CollectionUtil.isEmpty(org) || CollectionUtil.isEmpty((Collection)org.get("Content-Type"))) {
            template.header("Content-Type", new String[]{"application/json;charset=UTF-8"});
        }

    }
}
