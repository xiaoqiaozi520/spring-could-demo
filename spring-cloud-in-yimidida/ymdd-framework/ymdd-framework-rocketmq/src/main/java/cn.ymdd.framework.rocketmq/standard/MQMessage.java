/*    */ package cn.ymdd.framework.rocketmq.standard;
/*    */ 
/*    */ import cn.ymdd.framework.toolkit.id.GUID;
/*    */ import cn.ymdd.framework.toolkit.util.StringUtil;
/*    */ import com.alibaba.fastjson.JSON;
/*    */ import java.io.Serializable;
/*    */ import java.nio.charset.Charset;
/*    */ 
/*    */ public class MQMessage
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 4728813641798269532L;
/*    */   protected final Object content;
/*    */   protected final String tag;
/*    */   protected final String key;
/*    */   private final boolean json;
/*    */ 
/*    */   public MQMessage(Object content)
/*    */   {
/* 26 */     this(null, content, null, false);
/*    */   }
/*    */ 
/*    */   public MQMessage(Object content, String tag) {
/* 30 */     this(null, content, tag, false);
/*    */   }
/*    */ 
/*    */   public MQMessage(String key, Object content, String tag, boolean json) {
/* 34 */     this.key = (StringUtil.isEmpty(key) ? GUID.randomGUID() : key);
/* 35 */     this.content = content;
/* 36 */     this.json = json;
/* 37 */     this.tag = tag;
/*    */   }
/*    */ 
/*    */   public final synchronized <T> T getContent(Class<T> clz)
/*    */   {
/* 42 */     if (null == this.content) {
/* 43 */       return null;
/*    */     }
/* 45 */     if (this.json) {
/* 46 */       return JSON.parseObject((String)this.content, clz);
/*    */     }
/* 48 */     return (T) this.content;
/*    */   }
/*    */ 
/*    */   public final synchronized byte[] getContent()
/*    */   {
/* 53 */     if (null == this.content) {
/* 54 */       return null;
/*    */     }
/* 56 */     if (this.json) {
/* 57 */       return ((String)this.content).getBytes(Charset.forName("UTF-8"));
/*    */     }
/* 59 */     return JSON.toJSONString(this.content).getBytes(Charset.forName("UTF-8"));
/*    */   }
/*    */ 
/*    */   public String getTag()
/*    */   {
/* 64 */     return this.tag == null ? "" : this.tag;
/*    */   }
/*    */ 
/*    */   public String getKey() {
/* 68 */     return this.key;
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 73 */     return new StringBuilder().append("MQMessage [key=").append(this.key).append(", content=").append(this.json ? this.content : JSON.toJSONString(this.content)).append(", tag=").append(getTag()).append("]").toString();
/*    */   }
/*    */ }

/* Location:           E:\demo\git\spring-could-demo\cn\guludai\framework\guludai-framework-rocketmq\0.0.1-SNAPSHOT\guludai-framework-rocketmq-0.0.1-20180208.025258-52.jar
 * Qualified Name:     cn.guludai.framework.rocketmq.standard.MQMessage
 * JD-Core Version:    0.6.0
 */