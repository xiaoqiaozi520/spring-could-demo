/*    */ package cn.ymdd.api.data;
/*    */
/*    */ import com.alibaba.fastjson.annotation.JSONField;
/*    */ import io.swagger.annotations.Api;
/*    */ import io.swagger.annotations.ApiModelProperty;
/*    */ import java.io.Serializable;
/*    */ import org.springframework.util.StringUtils;
/*    */
/*    */ @Api(description="公共请求体")
/*    */ public class Request<T>
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = -1957570250720785288L;
/*    */
/*    */   @JSONField(ordinal=1)
/*    */   @ApiModelProperty(value="用户的ID", required=true)
/*    */   private String uid;
/*    */
/*    */   @JSONField(ordinal=2)
/*    */   @ApiModelProperty(value="用户登录授权指令", required=true)
/*    */   private String token;
/*    */
/*    */   @JSONField(ordinal=3)
/*    */   @ApiModelProperty(value="所有请求数据的签名", required=true)
/*    */   private String signature;
/*    */
/*    */   @JSONField(ordinal=4)
/*    */   @ApiModelProperty(value="请求JSON数据体", required=true)
/*    */   private T data;
/*    */
/*    */   public String getUid()
/*    */   {
/* 40 */     return this.uid;
/*    */   }
/*    */
/*    */   public Request<T> setUid(String uid) {
/* 44 */     this.uid = uid;
/* 45 */     return this;
/*    */   }
/*    */
/*    */   public String getToken() {
/* 49 */     return this.token;
/*    */   }
/*    */
/*    */   public Request<T> setToken(String token) {
/* 53 */     this.token = token;
/* 54 */     return this;
/*    */   }
/*    */
/*    */   public String getSignature() {
/* 58 */     return this.signature;
/*    */   }
/*    */
/*    */   public Request<T> setSignature(String signature) {
/* 62 */     this.signature = signature;
/* 63 */     return this;
/*    */   }
/*    */
/*    */   public T getData() {
/* 67 */     return this.data;
/*    */   }
/*    */
/*    */   public Request<T> setData(Object data)
/*    */   {
/* 72 */     this.data = (T) data;
/* 73 */     return this;
/*    */   }
/*    */   @JSONField(serialize=false)
/*    */   public boolean hasData() {
/* 78 */     return (this.data != null) && (!StringUtils.isEmpty(this.data.toString()));
/*    */   }
/*    */
/*    */   public static final <T> Request<T> newRequest(T data)
/*    */   {
/* 89 */     Request req = new Request();
/* 90 */     req.setData(data);
/* 91 */     return req;
/*    */   }
/*    */ }

/* Location:           C:\Users\Administrator\Desktop\container\guludai-container-api\0.0.1-SNAPSHOT\guludai-container-api-0.0.1-20180207.054839-32.jar
 * Qualified Name:     cn.guludai.api.data.Request
 * JD-Core Version:    0.6.0
 */