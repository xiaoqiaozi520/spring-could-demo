package cn.ymdd.container.runtime.fence;


import java.util.function.Supplier;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public final class HttpFenceContext {
    private static final ThreadLocal<HttpFenceContext> ContextHolder = ThreadLocal.withInitial(() -> {
        return new HttpFenceContext();
    });
    private HttpFenceContent upBodies;
    private HttpFenceContent downBodies;
    private ServletResponse response;
    private ServletRequest request;

    public HttpFenceContext() {
    }

    public static String getId() {
        return hasUpHttpFenceContent() ? getUpHttpFenceContent().getId() : "main";
    }

    public static ServletRequest getServletRequest() {
        return ((HttpFenceContext)ContextHolder.get()).request;
    }

    public static ServletResponse getServletResponse() {
        return ((HttpFenceContext)ContextHolder.get()).response;
    }

    public static boolean hasUpHttpFenceContent() {
        return ((HttpFenceContext)ContextHolder.get()).upBodies != null;
    }

    public static boolean hasDownHttpFenceContent() {
        return ((HttpFenceContext)ContextHolder.get()).downBodies != null;
    }

    public static HttpFenceContent getUpHttpFenceContent() {
        return ((HttpFenceContext)ContextHolder.get()).upBodies;
    }

    public static HttpFenceContent getDownHttpFenceContent() {
        return ((HttpFenceContext)ContextHolder.get()).downBodies;
    }

    static void setHttpServletContext(ServletRequest request, ServletResponse response) {
        ((HttpFenceContext)ContextHolder.get()).response = response;
        ((HttpFenceContext)ContextHolder.get()).request = request;
    }

    static HttpFenceContent setUpHttpFenceContent(HttpFenceContent content) {
        return ((HttpFenceContext)ContextHolder.get()).upBodies = content;
    }

    static HttpFenceContent setDownHttpFenceContent(HttpFenceContent content) {
        return ((HttpFenceContext)ContextHolder.get()).downBodies = content;
    }

    static void delHttpFenceContent() {
        ContextHolder.remove();
    }
}
