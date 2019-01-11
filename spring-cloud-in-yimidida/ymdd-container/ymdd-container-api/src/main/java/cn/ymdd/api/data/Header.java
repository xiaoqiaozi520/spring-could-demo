/*    */ package cn.guludai.api.data;
/*    */ 
/*    */ public enum Header
/*    */ {
/* 13 */   CONTENTTYPE("Content-Type"), 
/*    */ 
/* 16 */   REQUESTTYPE("X-Requested-With"), 
/*    */ 
/* 19 */   CHANNELVERSION("X-Channel-Version"), 
/*    */ 
/* 22 */   CHANNEL("X-Channel"), 
/*    */ 
/* 25 */   DEVICE("X-Device"), 
/*    */ 
/* 28 */   UUID("X-Udid"), 
/*    */ 
/* 31 */   CLIENTTIME("X-Client-Time"), 
/*    */ 
/* 34 */   PUBKEY("X-Pub-Key"), 
/*    */ 
/* 37 */   SIGNATURE("X-Signature"), 
/*    */ 
/* 40 */   AUTHORIZATION("X-Authorization");
/*    */ 
/*    */   public static final String X_REQUEST_TIME = "X-Request-Time";
/*    */   public static final String X_RESPONSE_TIME = "X-Response-Time";
/*    */   public final String value;
/*    */ 
/* 49 */   private Header(String value) { this.value = value;
/*    */   }
/*    */ }

/* Location:           C:\Users\Administrator\Desktop\container\guludai-container-api\0.0.1-SNAPSHOT\guludai-container-api-0.0.1-20180207.054839-32.jar
 * Qualified Name:     cn.guludai.api.data.Header
 * JD-Core Version:    0.6.0
 */