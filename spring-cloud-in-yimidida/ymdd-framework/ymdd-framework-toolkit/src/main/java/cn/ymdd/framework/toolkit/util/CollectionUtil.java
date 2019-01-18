/*    */ package cn.ymdd.framework.toolkit.util;
/*    */ 
/*    */ import com.google.common.collect.ArrayListMultimap;
/*    */ import com.google.common.collect.ListMultimap;
/*    */ import com.google.common.collect.Maps;
/*    */ import java.util.Iterator;
/*    */ import java.util.LinkedHashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Collectors;
/*    */ import java.util.stream.Stream;
/*    */ import org.apache.commons.collections.CollectionUtils;
/*    */ 
/*    */ public abstract class CollectionUtil extends CollectionUtils
/*    */ {
/*    */   public static <K, V> boolean isEmpty(Map<K, V> map)
/*    */   {
/* 22 */     return (map == null) || (map.isEmpty());
/*    */   }
/*    */ 
/*    */   public static <K, V> boolean isNotEmpty(Map<K, V> map) {
/* 26 */     return !isEmpty(map);
/*    */   }
/*    */ 
/*    */   public static <T> boolean isEmpty(T[] array) {
/* 30 */     return (array == null) || (array.length == 0);
/*    */   }
/*    */ 
/*    */   public static <T> boolean isNotEmpty(T[] array) {
/* 34 */     return !isEmpty(array);
/*    */   }
/*    */ 
/*    */   public static <T> boolean contains(T[] array, T val) {
/* 38 */     for (Object t : array) {
/* 39 */       if (val.equals(t)) {
/* 40 */         return true;
/*    */       }
/*    */     }
/* 43 */     return false;
/*    */   }
/*    */ 
/*    */   public static <T> T head(List<T> list) {
/* 47 */     return list.get(0);
/*    */   }
/*    */ 
/*    */   public static <T> T end(List<T> list) {
/* 51 */     return list.get(list.size() - 1);
/*    */   }
/*    */ 
/*    */   public static <T, R> List<R> map(List<T> list, Function<? super T, R> function) {
/* 55 */     return (List)list.stream().map(function).collect(Collectors.toList());
/*    */   }
/*    */ 
/*    */   public static <K, V> LinkedHashMap<K, V> ofMap(List<V> list, Function<V, K> keyFunction) {
/* 59 */     LinkedHashMap result = Maps.newLinkedHashMap();
/* 60 */     for (Iterator localIterator = list.iterator(); localIterator.hasNext(); ) { Object value = localIterator.next();
/* 61 */       result.put(keyFunction.apply((V) value), value);
/*    */     }
/* 63 */     return result;
/*    */   }
/*    */ 
/*    */   public static <K, V> ListMultimap<K, V> groupBy(List<V> list, Function<V, K> keyFunction) {
/* 67 */     ListMultimap result = ArrayListMultimap.create();
/* 68 */     for (Iterator localIterator = list.iterator(); localIterator.hasNext(); ) { Object value = localIterator.next();
/* 69 */       result.put(keyFunction.apply((V) value), value);
/*    */     }
/* 71 */     return result;
/*    */   }
/*    */ }

/* Location:           E:\demo\git\spring-could-demo\cn\guludai\framework\guludai-framework-toolkit\0.0.1-SNAPSHOT\guludai-framework-toolkit-0.0.1-20180529.160258-22.jar
 * Qualified Name:     cn.guludai.framework.toolkit.util.CollectionUtil
 * JD-Core Version:    0.6.0
 */