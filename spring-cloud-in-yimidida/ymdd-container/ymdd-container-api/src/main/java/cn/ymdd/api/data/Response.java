/*     */ package cn.ymdd.api.data;
/*     */ 
/*     */ import cn.ymdd.api.Code;
/*     */ import cn.ymdd.api.code.BaseCode;
/*     */ import com.alibaba.fastjson.annotation.JSONField;
/*     */ import io.swagger.annotations.Api;
/*     */ import io.swagger.annotations.ApiModelProperty;
/*     */ import java.io.Serializable;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ @Api(description="公共响应体")
/*     */ public class Response<T>
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 23304424250156394L;
/*     */ 
/*     */   @JSONField(ordinal=1)
/*     */   @ApiModelProperty(value="响应的状态", required=true)
/*     */   private boolean status;
/*     */ 
/*     */   @JSONField(ordinal=2)
/*     */   @ApiModelProperty(value="响应的结果码", required=true)
/*     */   private String code;
/*     */ 
/*     */   @JSONField(ordinal=3)
/*     */   @ApiModelProperty(value="响应的信息提示", required=true)
/*     */   private String message;
/*     */ 
/*     */   @JSONField(ordinal=4)
/*     */   @ApiModelProperty(value="所有响应数据的签名", required=true)
/*     */   private String signature;
/*     */ 
/*     */   @JSONField(ordinal=5)
/*     */   @ApiModelProperty(value="响应的JSON数据体对象", required=true)
/*     */   private T data;
/*     */ 
/*     */   public String getCode()
/*     */   {
/*  51 */     return this.code;
/*     */   }
/*     */ 
/*     */   public Response<T> setCode(String code) {
/*  55 */     this.code = code;
/*  56 */     return this;
/*     */   }
/*     */ 
/*     */   public String getMessage() {
/*  60 */     return this.message;
/*     */   }
/*     */ 
/*     */   public Response<T> setMessage(String message) {
/*  64 */     this.message = message;
/*  65 */     return this;
/*     */   }
/*     */ 
/*     */   public String getSignature() {
/*  69 */     return this.signature;
/*     */   }
/*     */ 
/*     */   public <T> Response<T> setSignature(String signature)
/*     */   {
/*  74 */     this.signature = signature;
/*  75 */     return (Response<T>) this;
/*     */   }
/*     */ 
/*     */   public T getData() {
/*  79 */     return this.data;
/*     */   }
/*     */ 
/*     */   public Response<T> setData(Object data)
/*     */   {
/*  84 */     this.data = (T) data;
/*  85 */     return this;
/*     */   }
/*     */ 
/*     */   public boolean isStatus() {
/*  89 */     return this.status;
/*     */   }
/*     */ 
/*     */   public Response<T> setStatus(boolean status) {
/*  93 */     this.status = status;
/*  94 */     return this;
/*     */   }
/*     */   @JSONField(serialize=false)
/*     */   public boolean isSuccess() {
/*  99 */     return (this.status) && ("200".equals(this.code));
/*     */   }
/*     */   @JSONField(serialize=false)
/*     */   public boolean isFailure() {
/* 104 */     return !isSuccess();
/*     */   }
/*     */   @JSONField(serialize=false)
/*     */   public boolean hasData() {
/* 109 */     return (this.data != null) && (!StringUtils.isEmpty(this.data.toString()));
/*     */   }
/*     */ 
/*     */   public static final <T> Response<T> success()
/*     */   {
/* 118 */     return success(BaseCode.SUCCESS);
/*     */   }
/*     */ 
/*     */   public static final <T> Response<T> success(Code code)
/*     */   {
/* 129 */     return success(code, null);
/*     */   }
/*     */ 
/*     */   public static final <T> Response<T> success(Code code, Object data)
/*     */   {
/* 143 */     Response cr = new Response();
/* 144 */     cr.message = code.getMessage();
/* 145 */     cr.code = code.getCode();
/* 146 */     cr.data = data;
/* 147 */     cr.status = true;
/* 148 */     return cr;
/*     */   }
/*     */ 
/*     */   public static final <T> Response<T> failure(Code code)
/*     */   {
/* 159 */     return failure(code, code.getMessage(), null);
/*     */   }
/*     */ 
/*     */   public static final <T> Response<T> failure(Code code, Object data)
/*     */   {
/* 172 */     return failure(code, code.getMessage(), data);
/*     */   }
/*     */ 
/*     */   public static final <T> Response<T> failure(Code code, String message)
/*     */   {
/* 185 */     return failure(code, message, null);
/*     */   }
/*     */ 
/*     */   public static final <T> Response<T> failure(Code code, String message, Object data)
/*     */   {
/* 201 */     Response cr = new Response();
/* 202 */     cr.code = code.getCode();
/* 203 */     cr.message = message;
/* 204 */     cr.data = data;
/* 205 */     cr.status = false;
/* 206 */     return cr;
/*     */   }
/*     */ 
/*     */   public static final <T> Response<T> failure(Code code, Exception exception) {
/* 210 */     return failure(code, exception, null);
/*     */   }
/*     */ 
/*     */   public static final <T> Response<T> failure(Code code, Exception exception, Object data) {
/* 214 */     return failure(code, exception, null, data);
/*     */   }
/*     */ 
/*     */   public static final <T> Response<T> failure(Code code, Exception exception, String message, Object data)
/*     */   {
/* 219 */     Response cr = new Response();
/* 220 */     cr.message = ((message == null) || (message.isEmpty()) ? exception.getMessage() : message);
/* 221 */     cr.code = code.getCode();
/* 222 */     cr.data = data;
/* 223 */     cr.status = false;
/* 224 */     return cr;
/*     */   }
/*     */ }

/* Location:           C:\Users\Administrator\Desktop\container\guludai-container-api\0.0.1-SNAPSHOT\guludai-container-api-0.0.1-20180207.054839-32.jar
 * Qualified Name:     cn.guludai.api.data.Response
 * JD-Core Version:    0.6.0
 */