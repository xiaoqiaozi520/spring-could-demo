//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cn.ymdd.container.web.restful;

import cn.ymdd.api.code.BaseCode;
import cn.ymdd.container.runtime.exception.RunningException;
import cn.ymdd.container.runtime.fence.HttpFenceContext;
import cn.ymdd.framework.toolkit.util.CollectionUtil;
import cn.ymdd.framework.toolkit.util.StringUtil;
import com.google.common.collect.Maps;
import com.netflix.util.Pair;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import java.net.URI;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;

abstract class RestfulBalancer extends ZuulFilter {
    private final String action;
    private final boolean should;

    public RestfulBalancer(String action, boolean should) {
        this.should = should;
        this.action = action;
    }

    public Object run() {
        return null;
    }

    public final String filterType() {
        return this.action;
    }

    public final boolean shouldFilter() {
        return this.should;
    }

    public final int filterOrder() {
        return 0;
    }

    final class RestfulPostBalancer extends RestfulBalancer {
        public RestfulPostBalancer() {
            super("post", true);
        }

        public String cover(URI referer, URI redirect) {
            if (StringUtil.isAllNotEmpty(new String[]{referer.getHost(), redirect.getPath()})) {
                int port = referer.getPort() == -1 ? 80 : referer.getPort();
                return "http://" + referer.getHost() + ":" + port + redirect.getPath();
            } else {
                throw new RunningException(BaseCode.BADREQUEST);
            }
        }

        public String cover(HttpServletRequest request, Map<String, Pair<String, String>> pairs) {
            try {
                Pair<String, String> pair = (Pair)pairs.get("Location".toLowerCase());
                if (pair == null) {
                    return "";
                } else {
                    URI redirect = new URI((String)pair.second());
                    String location = request.getHeader("Referer");
                    if (StringUtil.isNotEmpty(location)) {
                        return this.cover(new URI(location), redirect);
                    } else {
                        String host = request.getHeader("Host");
                        if (StringUtil.isNotEmpty(host)) {
                            location = "http://" + host + ":" + request.getServerPort();
                            return this.cover(new URI(location), redirect);
                        } else {
                            throw new RunningException(BaseCode.ERROR);
                        }
                    }
                }
            } catch (Exception var7) {
                throw new RunningException(BaseCode.ERROR, var7);
            }
        }

        public Object run() {
            try {
                RequestContext context = RequestContext.getCurrentContext();
                if (HttpStatus.valueOf(context.getResponseStatusCode()).is3xxRedirection()) {
                    Map<String, Pair<String, String>> pairs = Maps.newHashMap();
                    Iterator var3 = context.getOriginResponseHeaders().iterator();

                    while(var3.hasNext()) {
                        Pair<String, String> pair = (Pair)var3.next();
                        pairs.put(((String)pair.first()).toLowerCase(), pair);
                    }

                    HttpServletRequest request = context.getRequest();
                    Iterator var9 = context.getZuulResponseHeaders().iterator();

                    while(var9.hasNext()) {
                        Pair<String, String> pairx = (Pair)var9.next();
                        String key = ((String)pairx.first()).toLowerCase();
                        if (pairs.get(key) == null) {
                            pairs.put(key, pairx);
                        }
                    }

                    String location = this.cover((HttpServletRequest)request, (Map)pairs);
                    context.getZuulResponseHeaders().clear();
                    pairs.remove("Location".toLowerCase());
                    context.getZuulResponseHeaders().addAll(pairs.values());
                    context.addZuulResponseHeader("Location", location);
                }

                return null;
            } catch (Exception var7) {
                throw new RunningException(BaseCode.ERROR, var7);
            }
        }
    }

    final class RestfulPreBalancer extends RestfulBalancer {
        public RestfulPreBalancer() {
            super("pre", true);
        }

        public Object run() {
            try {
                RequestContext context = RequestContext.getCurrentContext();
                Map<String, String> headers = context.getZuulRequestHeaders();
                if (CollectionUtil.isEmpty(headers) || StringUtil.isEmpty((CharSequence)headers.get("Content-Type"))) {
                    context.addZuulRequestHeader("Content-Type", context.getRequest().getHeader("Content-Type"));
                }

                context.addZuulRequestHeader("X-Fence-Id", HttpFenceContext.getId());
                if (StringUtil.isEmpty((CharSequence)headers.get("Cookie"))) {
                    context.addZuulRequestHeader("Cookie", context.getRequest().getHeader("Cookie"));
                }

                return null;
            } catch (Exception var3) {
                throw new RunningException(BaseCode.ERROR, var3);
            }
        }
    }
}
