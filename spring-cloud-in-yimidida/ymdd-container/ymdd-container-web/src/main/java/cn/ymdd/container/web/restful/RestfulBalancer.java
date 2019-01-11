/*     */ package cn.guludai.container.web.restful;
/*     */ 
/*     */ import cn.guludai.api.code.BaseCode;
/*     */ import cn.guludai.container.runtime.exception.RunningException;
/*     */ import cn.guludai.container.runtime.fence.HttpFenceContext;
/*     */ import cn.guludai.framework.toolkit.util.CollectionUtil;
/*     */ import cn.guludai.framework.toolkit.util.StringUtil;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.netflix.util.Pair;
/*     */ import com.netflix.zuul.ZuulFilter;
/*     */ import com.netflix.zuul.context.RequestContext;
/*     */ import java.net.URI;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.springframework.http.HttpStatus;
/*     */ 
/*     */ abstract class RestfulBalancer extends ZuulFilter
/*     */ {
/*     */   private final String action;
/*     */   private final boolean should;
/*     */ 
/*     */   public RestfulBalancer(String action, boolean should)
/*     */   {
/*  36 */     this.should = should;
/*  37 */     this.action = action;
/*     */   }
/*     */ 
/*     */   public Object run()
/*     */   {
/*  42 */     return null;
/*     */   }
/*     */ 
/*     */   public final String filterType()
/*     */   {
/*  47 */     return this.action;
/*     */   }
/*     */ 
/*     */   public final boolean shouldFilter()
/*     */   {
/*  52 */     return this.should;
/*     */   }
/*     */ 
/*     */   public final int filterOrder()
/*     */   {
/*  57 */     return 0;
/*     */   }
/*     */ 
/*     */   final class RestfulPostBalancer extends RestfulBalancer
/*     */   {
/*     */     public RestfulPostBalancer()
/*     */     {
/*  88 */       super(true);
/*     */     }
/*     */ 
/*     */     public String cover(URI referer, URI redirect) {
/*  92 */       if (StringUtil.isAllNotEmpty(new String[] { referer.getHost(), redirect.getPath() }).booleanValue()) {
/*  93 */         int port = referer.getPort() == -1 ? 80 : referer.getPort();
/*  94 */         return "http://" + referer.getHost() + ":" + port + redirect.getPath();
/*     */       }
/*  96 */       throw new RunningException(BaseCode.BADREQUEST);
/*     */     }
/*     */ 
/*     */     public String cover(HttpServletRequest request, Map<String, Pair<String, String>> pairs)
/*     */     {
/*     */       try {
/* 102 */         Pair pair = (Pair)pairs.get("Location".toLowerCase());
/* 103 */         if (pair == null) {
/* 104 */           return "";
/*     */         }
/* 106 */         URI redirect = new URI((String)pair.second());
/* 107 */         String location = request.getHeader("Referer");
/* 108 */         if (StringUtil.isNotEmpty(location).booleanValue()) {
/* 109 */           return cover(new URI(location), redirect);
/*     */         }
/* 111 */         String host = request.getHeader("Host");
/* 112 */         if (StringUtil.isNotEmpty(host).booleanValue()) {
/* 113 */           location = "http://" + host + ":" + request.getServerPort();
/* 114 */           return cover(new URI(location), redirect);
/*     */         }
/* 116 */         throw new RunningException(BaseCode.ERROR);
/*     */       } catch (Exception e) {
/*     */       }
/* 119 */       throw new RunningException(BaseCode.ERROR, e);
/*     */     }
/*     */ 
/*     */     public Object run()
/*     */     {
/*     */       try
/*     */       {
/* 126 */         RequestContext context = RequestContext.getCurrentContext();
/* 127 */         if (HttpStatus.valueOf(context.getResponseStatusCode()).is3xxRedirection()) {
/* 128 */           Map pairs = Maps.newHashMap();
/* 129 */           for (Iterator localIterator = context.getOriginResponseHeaders().iterator(); localIterator.hasNext(); ) { pair = (Pair)localIterator.next();
/* 130 */             pairs.put(((String)pair.first()).toLowerCase(), pair);
/*     */           }
/*     */           Pair pair;
/* 132 */           HttpServletRequest request = context.getRequest();
/* 133 */           for (Pair pair : context.getZuulResponseHeaders()) {
/* 134 */             String key = ((String)pair.first()).toLowerCase();
/* 135 */             if (pairs.get(key) == null) {
/* 136 */               pairs.put(key, pair);
/*     */             }
/*     */           }
/* 139 */           String location = cover(request, pairs);
/* 140 */           context.getZuulResponseHeaders().clear();
/* 141 */           pairs.remove("Location".toLowerCase());
/* 142 */           context.getZuulResponseHeaders().addAll(pairs.values());
/* 143 */           context.addZuulResponseHeader("Location", location);
/*     */         }
/*     */       } catch (Exception e) {
/* 146 */         throw new RunningException(BaseCode.ERROR, e);
/*     */       }
/* 148 */       return null;
/*     */     }
/*     */   }
/*     */ 
/*     */   final class RestfulPreBalancer extends RestfulBalancer
/*     */   {
/*     */     public RestfulPreBalancer()
/*     */     {
/*  63 */       super(true);
/*     */     }
/*     */ 
/*     */     public Object run()
/*     */     {
/*     */       try {
/*  69 */         RequestContext context = RequestContext.getCurrentContext();
/*  70 */         Map headers = context.getZuulRequestHeaders();
/*  71 */         if ((CollectionUtil.isEmpty(headers)) || (StringUtil.isEmpty((CharSequence)headers.get("Content-Type")))) {
/*  72 */           context.addZuulRequestHeader("Content-Type", context.getRequest().getHeader("Content-Type"));
/*     */         }
/*  74 */         context.addZuulRequestHeader("X-Fence-Id", HttpFenceContext.getId());
/*  75 */         if (StringUtil.isEmpty((CharSequence)headers.get("Cookie")))
/*  76 */           context.addZuulRequestHeader("Cookie", context.getRequest().getHeader("Cookie"));
/*     */       }
/*     */       catch (Exception e) {
/*  79 */         throw new RunningException(BaseCode.ERROR, e);
/*     */       }
/*  81 */       return null;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Users\Administrator\Desktop\container\guludai-container-web\0.0.1-SNAPSHOT\guludai-container-web-0.0.1-20180428.084950-107.jar
 * Qualified Name:     cn.guludai.container.web.restful.RestfulBalancer
 * JD-Core Version:    0.6.0
 */