/*    */ package cn.ymdd.framework.toolkit.util;
/*    */ 
/*    */ import java.io.UnsupportedEncodingException;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ 
/*    */ public abstract class StringUtil extends StringUtils
/*    */ {
/*    */   public static String toUtf8String(byte[] bit)
/*    */   {
/* 14 */     if ((bit == null) || (bit.length == 0))
/* 15 */       return "";
/*    */     try
/*    */     {
/* 18 */       return new String(bit, "UTF-8"); } catch (UnsupportedEncodingException e) {throw new RuntimeException(e.getMessage(), e);
/*    */     }
/* 20 */
/*    */   }
/*    */ 
/*    */   public static Boolean isAllNotEmpty(String[] str)
/*    */   {
/* 26 */     return Boolean.valueOf(!isAnyEmpty(str));
/*    */   }
/*    */ 
/*    */   public static Boolean isNotEmpty(String str) {
/* 30 */     return Boolean.valueOf(!isEmpty(str));
/*    */   }
/*    */ 
/*    */   public static String convertNull(String str) {
/* 34 */     if (isEmpty(str)) {
/* 35 */       return "";
/*    */     }
/* 37 */     return str;
/*    */   }
/*    */ }

/* Location:           E:\demo\git\spring-could-demo\cn\guludai\framework\guludai-framework-toolkit\0.0.1-SNAPSHOT\guludai-framework-toolkit-0.0.1-20180529.160258-22.jar
 * Qualified Name:     cn.guludai.framework.toolkit.util.StringUtil
 * JD-Core Version:    0.6.0
 */