/*     */ package cn.ymdd.container.runtime.fence;
/*     */ 
/*     */ import cn.ymdd.framework.toolkit.util.StringUtil;
/*     */ import java.io.Serializable;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class HttpFenceContent
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 5604851301103465446L;
/*  18 */   private Map<String, String[]> parameters = new HashMap();
/*  19 */   private Map<String, String> headers = new HashMap();
/*     */   private String Protocol;
/*     */   private String domain;
/*     */   private String method;
/*     */   private String bodies;
/*  24 */   private int http = -1;
/*     */   private String uri;
/*     */   private String ip;
/*     */   private long size;
/*     */   private String id;
/*     */   private transient Class<?> clazz;
/*     */   private transient Object data;
/*     */ 
/*     */   public HttpFenceContent()
/*     */   {
/*     */   }
/*     */ 
/*     */   public HttpFenceContent(String id)
/*     */   {
/*  35 */     setId(id);
/*     */   }
/*     */ 
/*     */   public boolean hasBody() {
/*  39 */     return (this.bodies != null) && (!StringUtil.isEmpty(this.bodies));
/*     */   }
/*     */ 
/*     */   public String[] getParameter(String key)
/*     */   {
/*  46 */     return (String[])this.parameters.get(key);
/*     */   }
/*     */ 
/*     */   public Map<String, String[]> getParameters() {
/*  50 */     return this.parameters;
/*     */   }
/*     */ 
/*     */   public void setParameters(Map<String, String[]> parameters) {
/*  54 */     this.parameters = parameters;
/*     */   }
/*     */ 
/*     */   public void addParameters(String key, String value) {
/*  58 */     addParameters(key, new String[] { value });
/*     */   }
/*     */ 
/*     */   public void addParameters(String key, String[] value) {
/*  62 */     this.parameters.put(key, value);
/*     */   }
/*     */ 
/*     */   public String getHeader(String key) {
/*  66 */     return (String)this.headers.get(key);
/*     */   }
/*     */ 
/*     */   public Map<String, String> getHeaders() {
/*  70 */     return this.headers;
/*     */   }
/*     */ 
/*     */   public void setHeaders(Map<String, String> headers) {
/*  74 */     this.headers = headers;
/*     */   }
/*     */ 
/*     */   public void addHeaders(String key, String value) {
/*  78 */     this.headers.put(key, value);
/*     */   }
/*     */ 
/*     */   public String getBodies() {
/*  82 */     return this.bodies;
/*     */   }
/*     */ 
/*     */   public void setBodies(String bodies) {
/*  86 */     this.bodies = (bodies == null ? "" : bodies);
/*     */   }
/*     */ 
/*     */   public int getHttp() {
/*  90 */     return this.http;
/*     */   }
/*     */ 
/*     */   public void setHttp(int http) {
/*  94 */     this.http = http;
/*     */   }
/*     */ 
/*     */   public String getProtocol() {
/*  98 */     return this.Protocol;
/*     */   }
/*     */ 
/*     */   public void setProtocol(String protocol) {
/* 102 */     this.Protocol = protocol;
/*     */   }
/*     */ 
/*     */   public String getDomain() {
/* 106 */     return this.domain;
/*     */   }
/*     */ 
/*     */   public void setDomain(String domain) {
/* 110 */     this.domain = domain;
/*     */   }
/*     */ 
/*     */   public String getMethod() {
/* 114 */     return this.method;
/*     */   }
/*     */ 
/*     */   public void setMethod(String method) {
/* 118 */     this.method = method;
/*     */   }
/*     */ 
/*     */   public String getUri() {
/* 122 */     return this.uri;
/*     */   }
/*     */ 
/*     */   public void setUri(String uri) {
/* 126 */     this.uri = uri;
/*     */   }
/*     */ 
/*     */   public String getIp() {
/* 130 */     return this.ip;
/*     */   }
/*     */ 
/*     */   public void setIp(String ip) {
/* 134 */     this.ip = ip;
/*     */   }
/*     */ 
/*     */   public long getSize() {
/* 138 */     return this.size;
/*     */   }
/*     */ 
/*     */   public void setSize(long size) {
/* 142 */     this.size = size;
/*     */   }
/*     */ 
/*     */   public String getId() {
/* 146 */     return this.id;
/*     */   }
/*     */ 
/*     */   public void setId(String id) {
/* 150 */     this.id = id;
/*     */   }
/*     */ 
/*     */   public Class<?> getClazz() {
/* 154 */     return this.clazz;
/*     */   }
/*     */ 
/*     */   public void setClazz(Class<?> clazz) {
/* 158 */     this.clazz = clazz;
/*     */   }
/*     */ 
/*     */   public Object getData() {
/* 162 */     return this.data;
/*     */   }
/*     */ 
/*     */   public void setData(Object data) {
/* 166 */     this.data = data;
/*     */   }
/*     */ }

/* Location:           E:\demo\git\spring-could-demo\cn\guludai\container\guludai-container-runtime\0.0.1-SNAPSHOT\guludai-container-runtime-0.0.1-20180627.035509-108.jar
 * Qualified Name:     cn.guludai.container.runtime.fence.HttpFenceContent
 * JD-Core Version:    0.6.0
 */