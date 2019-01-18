/*    */ package cn.ymdd.framework.rocketmq.standard;
/*    */ 
/*    */ import cn.ymdd.framework.toolkit.util.StringUtil;
/*    */ 
/*    */ public final class MQMetadata
/*    */ {
/*    */   public static final int DEFALUT_CONSUMER = 5;
/* 15 */   private int consumer = 5;
/*    */   private final String group;
/*    */   private final String topic;
/*    */   private final String tag;
/*    */ 
/*    */   public MQMetadata(String group, String topic)
/*    */   {
/* 19 */     this(group, topic, null, 5);
/*    */   }
/*    */ 
/*    */   public MQMetadata(String group, String topic, String tag) {
/* 23 */     this(group, topic, tag, 5);
/*    */   }
/*    */ 
/*    */   public MQMetadata(String group, String topic, String tag, int consumer) {
/* 27 */     this.tag = (StringUtil.isEmpty(tag) ? "*" : tag);
/* 28 */     this.consumer = consumer;
/* 29 */     this.group = group;
/* 30 */     this.topic = topic;
/*    */   }
/*    */ 
/*    */   public int getConsumer() {
/* 34 */     return this.consumer;
/*    */   }
/*    */ 
/*    */   public String getGroup() {
/* 38 */     if (StringUtil.isEmpty(this.group)) {
/* 39 */       throw new NullPointerException("Group");
/*    */     }
/* 41 */     return this.group;
/*    */   }
/*    */ 
/*    */   public String getTopic() {
/* 45 */     if (StringUtil.isEmpty(this.topic)) {
/* 46 */       throw new NullPointerException("Topic");
/*    */     }
/* 48 */     return this.topic;
/*    */   }
/*    */ 
/*    */   public String getTag() {
/* 52 */     return this.tag;
/*    */   }
/*    */ }

/* Location:           E:\demo\git\spring-could-demo\cn\guludai\framework\guludai-framework-rocketmq\0.0.1-SNAPSHOT\guludai-framework-rocketmq-0.0.1-20180208.025258-52.jar
 * Qualified Name:     cn.guludai.framework.rocketmq.standard.MQMetadata
 * JD-Core Version:    0.6.0
 */