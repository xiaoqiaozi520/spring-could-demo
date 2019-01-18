/*    */ package cn.ymdd.framework.toolkit.id;
/*    */ 
/*    */ import cn.ymdd.framework.toolkit.util.StringUtil;
/*    */ import com.google.common.collect.Maps;
/*    */ import java.io.Serializable;
/*    */ import java.util.Map;
/*    */ import java.util.Random;
/*    */ 
/*    */ public final class GUID
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 673168082935484196L;
/* 20 */   private static final Map<String, GUID> guid = Maps.newConcurrentMap();
/*    */ 
/* 22 */   private final long twepoch = 1420041600000L;
/* 23 */   private final long workerIdBits = 5L;
/* 24 */   private final long datacenterIdBits = 5L;
/* 25 */   private final long maxWorkerId = 31L;
/* 26 */   private final long maxDatacenterId = 31L;
/* 27 */   private final long sequenceBits = 12L;
/* 28 */   private final long workerIdShift = 12L;
/* 29 */   private final long datacenterIdShift = 17L;
/* 30 */   private final long timestampLeftShift = 22L;
/* 31 */   private final long sequenceMask = 4095L;
/*    */   private long workerId;
/*    */   private long datacenterId;
/* 35 */   private long sequence = 0L;
/* 36 */   private long lastTimestamp = -1L;
/*    */ 
/*    */   public static String randomGUID() {
/* 39 */     int wid = new Random().nextInt(31);
/* 40 */     wid++; wid--; int cid = wid == 0 ? wid : wid;
/* 41 */     String key = wid + "-" + cid;
/* 42 */     if (guid.get(key) == null) {
/* 43 */       guid.putIfAbsent(key, new GUID(wid, cid));
/*    */     }
/* 45 */     String[] ids = new String[3];
/* 46 */     String gid = String.valueOf(((GUID)guid.get(key)).tilNextGuid());
/* 47 */     int i = 0; for (int j = 0; i < gid.length(); ) {
/* 48 */       ids[(j++)] = StringUtil.substring(gid, i);
/*    */     }
/* 50 */     return StringUtil.join(ids, "-");
/*    */   }
/*    */ 
/*    */   private GUID(long workerId, long datacenterId) {
/* 54 */     if ((workerId > 31L) || (workerId < 0L)) {
/* 55 */       throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", new Object[] { Long.valueOf(31L) }));
/*    */     }
/* 57 */     if ((datacenterId > 31L) || (datacenterId < 0L)) {
/* 58 */       throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", new Object[] { Long.valueOf(31L) }));
/*    */     }
/* 60 */     this.datacenterId = datacenterId;
/* 61 */     this.workerId = workerId;
/*    */   }
/*    */ 
/*    */   private synchronized long tilNextGuid() {
/* 65 */     long timestamp = timeGen();
/* 66 */     if (timestamp < this.lastTimestamp) {
/* 67 */       throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", new Object[] { Long.valueOf(this.lastTimestamp - timestamp) }));
/*    */     }
/* 69 */     if (this.lastTimestamp == timestamp) {
/* 70 */       this.sequence = (this.sequence + 1L & 0xFFF);
/* 71 */       if (this.sequence == 0L)
/* 72 */         timestamp = tilNextMillis(this.lastTimestamp);
/*    */     }
/*    */     else {
/* 75 */       this.sequence = 0L;
/*    */     }
/* 77 */     this.lastTimestamp = timestamp;
/* 78 */     return timestamp - 1420041600000L << 22 | this.datacenterId << 17 | this.workerId << 12 | this.sequence;
/*    */   }
/*    */ 
/*    */   private long tilNextMillis(long lastTimestamp) {
/* 82 */     long timestamp = timeGen();
/* 83 */     while (timestamp <= lastTimestamp) {
/* 84 */       timestamp = timeGen();
/*    */     }
/* 86 */     return timestamp;
/*    */   }
/*    */ 
/*    */   private long timeGen() {
/* 90 */     return System.currentTimeMillis();
/*    */   }
/*    */ }

/* Location:           E:\demo\git\spring-could-demo\cn\guludai\framework\guludai-framework-toolkit\0.0.1-SNAPSHOT\guludai-framework-toolkit-0.0.1-20180529.160258-22.jar
 * Qualified Name:     cn.guludai.framework.toolkit.id.GUID
 * JD-Core Version:    0.6.0
 */