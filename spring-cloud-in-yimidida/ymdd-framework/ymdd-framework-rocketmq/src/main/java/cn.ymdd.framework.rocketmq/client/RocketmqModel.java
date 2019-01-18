/*    */ package cn.ymdd.framework.rocketmq.client;
/*    */ 
/*    */ import cn.ymdd.framework.rocketmq.bind.RocketMQ;
/*    */ import cn.ymdd.framework.rocketmq.standard.MQMetadata;
/*    */ import cn.ymdd.framework.toolkit.util.StringUtil;
/*    */ 
/*    */ class RocketmqModel
/*    */ {
/*    */   private final String name;
/*    */   private final String address;
/*    */   private final String group;
/*    */   private final String topic;
/*    */   private final String tag;
/*    */   private final int consumer;
/*    */ 
/*    */   private RocketmqModel(String name, String address, String group, String topic, String tag, int consumer)
/*    */   {
/* 19 */     this.tag = (StringUtil.isEmpty(tag) ? "*" : tag);
/* 20 */     this.consumer = consumer;
/* 21 */     this.address = address;
/* 22 */     this.group = group;
/* 23 */     this.topic = topic;
/* 24 */     this.name = name;
/*    */   }
/*    */ 
/*    */   public int getConsumer() {
/* 28 */     return this.consumer;
/*    */   }
/*    */ 
/*    */   public String getName() {
/* 32 */     return StringUtil.join(new String[] { this.name, this.group });
/*    */   }
/*    */ 
/*    */   public String getAddress() {
/* 36 */     return this.address;
/*    */   }
/*    */ 
/*    */   public String getGroup() {
/* 40 */     if (StringUtil.isEmpty(this.group)) {
/* 41 */       throw new NullPointerException("Group");
/*    */     }
/* 43 */     return this.group;
/*    */   }
/*    */ 
/*    */   public String getTopic() {
/* 47 */     if (StringUtil.isEmpty(this.topic)) {
/* 48 */       throw new NullPointerException("Topic");
/*    */     }
/* 50 */     return this.topic;
/*    */   }
/*    */ 
/*    */   public String getTag() {
/* 54 */     return this.tag;
/*    */   }
/*    */ 
/*    */   public final RocketmqModel checkModel() {
/* 58 */     if (StringUtil.isEmpty(this.name)) {
/* 59 */       throw new NullPointerException("Name");
/*    */     }
/* 61 */     if (StringUtil.isEmpty(this.address)) {
/* 62 */       throw new NullPointerException("Address");
/*    */     }
/* 64 */     if (StringUtil.isEmpty(this.group)) {
/* 65 */       throw new NullPointerException("Group");
/*    */     }
/* 67 */     if (StringUtil.isEmpty(this.topic)) {
/* 68 */       throw new NullPointerException("Topic");
/*    */     }
/* 70 */     return this;
/*    */   }
/*    */ 
/*    */   public static final RocketmqModel createRocketmqModel(RocketMQ annotation, String name, String address) {
/* 74 */     return new RocketmqModel(name, address, annotation.group(), annotation.topic(), annotation.tag(), annotation.consumer()).checkModel();
/*    */   }
/*    */ 
/*    */   public static final RocketmqModel createRocketmqModel(MQMetadata metadata, String name, String address) {
/* 78 */     return new RocketmqModel(name, address, metadata.getGroup(), metadata.getTopic(), metadata.getTag(), metadata.getConsumer()).checkModel();
/*    */   }
/*    */ }

/* Location:           E:\demo\git\spring-could-demo\cn\guludai\framework\guludai-framework-rocketmq\0.0.1-SNAPSHOT\guludai-framework-rocketmq-0.0.1-20180208.025258-52.jar
 * Qualified Name:     cn.guludai.framework.rocketmq.client.RocketmqModel
 * JD-Core Version:    0.6.0
 */