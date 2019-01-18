/*     */ package cn.ymdd.framework.rocketmq.standard;
/*     */ 
/*     */ import com.alibaba.fastjson.annotation.JSONField;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class MQError
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -8988257675130816717L;
/*     */ 
/*     */   @JSONField(ordinal=0)
/*     */   private String topic;
/*     */ 
/*     */   @JSONField(ordinal=1)
/*     */   private String tag;
/*     */ 
/*     */   @JSONField(ordinal=2)
/*     */   private String key;
/*     */ 
/*     */   @JSONField(ordinal=3)
/*     */   private boolean dead;
/*     */ 
/*     */   @JSONField(ordinal=4)
/*     */   private int times;
/*     */ 
/*     */   @JSONField(ordinal=5)
/*     */   private String error;
/*     */ 
/*     */   @JSONField(ordinal=6)
/*     */   private Object content;
/*     */ 
/*     */   private MQError()
/*     */   {
/*  39 */     this.times = 1;
/*     */   }
/*     */ 
/*     */   public static final MQError createMQError(String topic, boolean dead, String error, MQMessage message) {
/*  43 */     return createMQError(topic, 0, dead, error, message);
/*     */   }
/*     */ 
/*     */   public static final MQError createMQError(String topic, int times, boolean dead, String error, MQMessage message) {
/*  47 */     MQError bug = new MQError();
/*  48 */     bug.setTopic(topic);
/*  49 */     bug.setTimes(times <= 0 ? 1 : times);
/*  50 */     bug.setError(error);
/*  51 */     if (message != null) {
/*  52 */       bug.setKey(message.getKey());
/*  53 */       bug.setTag(message.getTag());
/*  54 */       bug.setContent(message.getContent(String.class));
/*     */     }
/*  56 */     return bug;
/*     */   }
/*     */ 
/*     */   public String getTopic() {
/*  60 */     return this.topic;
/*     */   }
/*     */ 
/*     */   public void setTopic(String topic) {
/*  64 */     this.topic = topic;
/*     */   }
/*     */ 
/*     */   public String getTag() {
/*  68 */     return this.tag;
/*     */   }
/*     */ 
/*     */   public void setTag(String tag) {
/*  72 */     this.tag = tag;
/*     */   }
/*     */ 
/*     */   public int getTimes() {
/*  76 */     return this.times;
/*     */   }
/*     */ 
/*     */   public void setTimes(int times) {
/*  80 */     this.times = times;
/*     */   }
/*     */ 
/*     */   public boolean isDead() {
/*  84 */     return this.dead;
/*     */   }
/*     */ 
/*     */   public void setDead(boolean dead) {
/*  88 */     this.dead = dead;
/*     */   }
/*     */ 
/*     */   public String getKey() {
/*  92 */     return this.key;
/*     */   }
/*     */ 
/*     */   public void setKey(String key) {
/*  96 */     this.key = key;
/*     */   }
/*     */ 
/*     */   public String getError() {
/* 100 */     return this.error;
/*     */   }
/*     */ 
/*     */   public void setError(String error) {
/* 104 */     this.error = error;
/*     */   }
/*     */ 
/*     */   public Object getContent() {
/* 108 */     return this.content;
/*     */   }
/*     */ 
/*     */   public void setContent(Object content) {
/* 112 */     this.content = content;
/*     */   }
/*     */ }

/* Location:           E:\demo\git\spring-could-demo\cn\guludai\framework\guludai-framework-rocketmq\0.0.1-SNAPSHOT\guludai-framework-rocketmq-0.0.1-20180208.025258-52.jar
 * Qualified Name:     cn.guludai.framework.rocketmq.standard.MQError
 * JD-Core Version:    0.6.0
 */