/*     */ package cn.ymdd.container.runtime.auth;
/*     */ 
/*     */ import com.alibaba.fastjson.JSON;
/*     */ import com.alibaba.fastjson.serializer.SerializerFeature;
/*     */ import java.io.Serializable;
/*     */ import java.util.Base64;
/*     */ import java.util.Base64.Encoder;
/*     */ 
/*     */ public class AuthoritySession
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 8417542918116335407L;
/*     */   public static final String X_Authority_Header = "X-Authority";
/*     */   private String userId;
/*     */   private String token;
/*     */   private String mobile;
/*     */   private Integer isonline;
/*     */   private String vNum;
/*     */   private String channel;
/*     */   private String device;
/*     */   private String udid;
/*     */   private String clientTime;
/*     */   private String clientIp;
/*     */   private long lastLoginTime;
/*     */ 
/*     */   public String getUserId()
/*     */   {
/*  54 */     return this.userId;
/*     */   }
/*     */ 
/*     */   public void setUserId(String userId) {
/*  58 */     this.userId = userId;
/*     */   }
/*     */ 
/*     */   public String getToken() {
/*  62 */     return this.token;
/*     */   }
/*     */ 
/*     */   public void setToken(String token) {
/*  66 */     this.token = token;
/*     */   }
/*     */ 
/*     */   public String getMobile() {
/*  70 */     return this.mobile;
/*     */   }
/*     */ 
/*     */   public void setMobile(String mobile) {
/*  74 */     this.mobile = mobile;
/*     */   }
/*     */ 
/*     */   public Integer getIsonline() {
/*  78 */     return this.isonline;
/*     */   }
/*     */ 
/*     */   public void setIsonline(Integer isonline) {
/*  82 */     this.isonline = isonline;
/*     */   }
/*     */ 
/*     */   public String getvNum() {
/*  86 */     return this.vNum;
/*     */   }
/*     */ 
/*     */   public void setvNum(String vNum) {
/*  90 */     this.vNum = vNum;
/*     */   }
/*     */ 
/*     */   public String getChannel() {
/*  94 */     return this.channel;
/*     */   }
/*     */ 
/*     */   public void setChannel(String channel) {
/*  98 */     this.channel = channel;
/*     */   }
/*     */ 
/*     */   public String getDevice() {
/* 102 */     return this.device;
/*     */   }
/*     */ 
/*     */   public void setDevice(String device) {
/* 106 */     this.device = device;
/*     */   }
/*     */ 
/*     */   public String getUdid() {
/* 110 */     return this.udid;
/*     */   }
/*     */ 
/*     */   public void setUdid(String udid) {
/* 114 */     this.udid = udid;
/*     */   }
/*     */ 
/*     */   public String getClientTime() {
/* 118 */     return this.clientTime;
/*     */   }
/*     */ 
/*     */   public void setClientTime(String clientTime) {
/* 122 */     this.clientTime = clientTime;
/*     */   }
/*     */ 
/*     */   public String getClientIp() {
/* 126 */     return this.clientIp;
/*     */   }
/*     */ 
/*     */   public void setClientIp(String clientIp) {
/* 130 */     this.clientIp = clientIp;
/*     */   }
/*     */ 
/*     */   public long getLastLoginTime() {
/* 134 */     return this.lastLoginTime;
/*     */   }
/*     */ 
/*     */   public void setLastLoginTime(long lastLoginTime) {
/* 138 */     this.lastLoginTime = lastLoginTime;
/*     */   }
/*     */ 
/*     */   public final String toJsonString() {
/* 142 */     return JSON.toJSONString(this);
/*     */   }
/*     */ 
/*     */   public String toBase64String() {
/* 146 */     return Base64.getEncoder().encodeToString(JSON.toJSONBytes(this, new SerializerFeature[0]));
/*     */   }
/*     */ }

/* Location:           E:\demo\git\spring-could-demo\cn\guludai\container\guludai-container-runtime\0.0.1-SNAPSHOT\guludai-container-runtime-0.0.1-20180627.035509-108.jar
 * Qualified Name:     cn.guludai.container.runtime.auth.AuthoritySession
 * JD-Core Version:    0.6.0
 */