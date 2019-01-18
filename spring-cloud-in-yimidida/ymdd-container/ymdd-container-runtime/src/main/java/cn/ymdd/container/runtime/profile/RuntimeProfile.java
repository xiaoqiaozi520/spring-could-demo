/*    */ package cn.ymdd.container.runtime.profile;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import java.util.List;
/*    */ 
/*    */ public enum RuntimeProfile
/*    */ {
/* 15 */   LOCAL("local", "本地环境"), DEVP("devp", "开发环境"), TEST("test", "测试环境"), PREP("prep", "预生产环境"), PROD("prod", "生产环境");
/*    */ 
/*    */   public final String code;
/*    */   public final String message;
/*    */ 
/* 20 */   private RuntimeProfile(String code, String message) { this.message = message;
/* 21 */     this.code = code; }
/*    */ 
/*    */   public String getMessage()
/*    */   {
/* 25 */     return this.message;
/*    */   }
/*    */ 
/*    */   public String getCode() {
/* 29 */     return this.code;
/*    */   }
/*    */ 
/*    */   public static final RuntimeProfile findRuntimeProfile(String code) {
/* 33 */     for (RuntimeProfile profile : values()) {
/* 34 */       if (profile.code.equals(code)) {
/* 35 */         return profile;
/*    */       }
/*    */     }
/* 38 */     throw new IllegalStateException("Not support profile");
/*    */   }
/*    */ 
/*    */   public static final List<String> finalAllCode() {
/* 42 */     List files = Lists.newArrayList();
/* 43 */     for (RuntimeProfile profile : values()) {
/* 44 */       files.add(profile.getCode());
/*    */     }
/* 46 */     return files;
/*    */   }
/*    */ 
/*    */   public final boolean isNotLocal() {
/* 50 */     return LOCAL != this;
/*    */   }
/*    */ }

/* Location:           E:\demo\git\spring-could-demo\cn\guludai\container\guludai-container-runtime\0.0.1-SNAPSHOT\guludai-container-runtime-0.0.1-20180627.035509-108.jar
 * Qualified Name:     cn.guludai.container.runtime.profile.RuntimeProfile
 * JD-Core Version:    0.6.0
 */