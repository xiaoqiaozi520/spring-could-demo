/*     */ package cn.guludai.container.web.restful;
/*     */ 
/*     */ import cn.guludai.api.data.Request;
/*     */ import cn.guludai.api.data.Response;
/*     */ import cn.guludai.framework.toolkit.util.ClassUtil;
/*     */ import cn.guludai.framework.toolkit.util.CollectionUtil;
/*     */ import com.alibaba.fastjson.JSON;
/*     */ import com.alibaba.fastjson.JSONArray;
/*     */ import com.alibaba.fastjson.JSONException;
/*     */ import com.alibaba.fastjson.JSONObject;
/*     */ import com.alibaba.fastjson.serializer.SerializerFeature;
/*     */ import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Type;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import javax.xml.transform.Source;
/*     */ import org.springframework.cglib.core.ReflectUtils;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.http.HttpInputMessage;
/*     */ import org.springframework.http.HttpOutputMessage;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.converter.ByteArrayHttpMessageConverter;
/*     */ import org.springframework.http.converter.FormHttpMessageConverter;
/*     */ import org.springframework.http.converter.GenericHttpMessageConverter;
/*     */ import org.springframework.http.converter.HttpMessageConverter;
/*     */ import org.springframework.http.converter.HttpMessageNotReadableException;
/*     */ import org.springframework.http.converter.HttpMessageNotWritableException;
/*     */ import org.springframework.http.converter.ResourceHttpMessageConverter;
/*     */ import org.springframework.http.converter.StringHttpMessageConverter;
/*     */ import org.springframework.http.converter.xml.SourceHttpMessageConverter;
/*     */ 
/*     */ class RestfulConverter extends FastJsonHttpMessageConverter
/*     */   implements GenericHttpMessageConverter<Object>
/*     */ {
/*     */   private static final int SUPPORT_GENERICS = 1;
/*  50 */   public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
/*  51 */   public static final HttpMessageConverter<?> JSON_CONVERTER = getJsonConverter();
/*  52 */   public static final HttpMessageConverter<?> STRING_CONVERTER = getStringConverter();
/*  53 */   public static final HttpMessageConverter<?> FORM_CONVERTER = getFormConverter();
/*  54 */   public static final List<HttpMessageConverter<?>> ALL_CONVERTER = getAllConverter();
/*     */ 
/*     */   protected static final HttpMessageConverter<?> getJsonConverter() {
/*  57 */     RestfulConverter json = new RestfulConverter();
/*  58 */     json.setFeatures(new SerializerFeature[] { SerializerFeature.SkipTransientField, SerializerFeature.QuoteFieldNames, SerializerFeature.WriteDateUseDateFormat, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullListAsEmpty, SerializerFeature.WriteNullBooleanAsFalse });
/*     */ 
/*  60 */     json.setSupportedMediaTypes(Arrays.asList(new MediaType[] { MediaType.APPLICATION_JSON_UTF8, MediaType.APPLICATION_JSON }));
/*  61 */     json.setDefaultCharset(DEFAULT_CHARSET);
/*  62 */     json.setCharset(DEFAULT_CHARSET);
/*  63 */     return json;
/*     */   }
/*     */ 
/*     */   protected static final HttpMessageConverter<?> getStringConverter() {
/*  67 */     StringHttpMessageConverter string = new StringHttpMessageConverter();
/*  68 */     string.setSupportedMediaTypes(Arrays.asList(new MediaType[] { MediaType.TEXT_PLAIN }));
/*  69 */     string.setDefaultCharset(DEFAULT_CHARSET);
/*  70 */     string.setWriteAcceptCharset(false);
/*  71 */     return string;
/*     */   }
/*     */ 
/*     */   protected static final HttpMessageConverter<?> getFormConverter()
/*     */   {
/*  76 */     FormHttpMessageConverter form = new FormHttpMessageConverter();
/*  77 */     form.addPartConverter(new ByteArrayHttpMessageConverter());
/*  78 */     form.addPartConverter(new ResourceHttpMessageConverter());
/*  79 */     form.addPartConverter(new SourceHttpMessageConverter());
/*  80 */     form.addPartConverter(STRING_CONVERTER);
/*  81 */     form.addPartConverter(JSON_CONVERTER);
/*  82 */     form.setCharset(DEFAULT_CHARSET);
/*  83 */     return form;
/*     */   }
/*     */ 
/*     */   protected static final List<HttpMessageConverter<?>> getAllConverter() {
/*  87 */     return Arrays.asList(new HttpMessageConverter[] { STRING_CONVERTER, JSON_CONVERTER, FORM_CONVERTER, new ResourceHttpMessageConverter() {
/*     */       public ResourceHttpMessageConverter addDefaultCharset() {
/*  89 */         super.setDefaultCharset(RestfulConverter.DEFAULT_CHARSET);
/*  90 */         return this;
/*     */       }
/*     */     }
/*  92 */     .addDefaultCharset(), new ByteArrayHttpMessageConverter() {
/*     */       public ByteArrayHttpMessageConverter addDefaultCharset() {
/*  94 */         super.setDefaultCharset(RestfulConverter.DEFAULT_CHARSET);
/*  95 */         return this;
/*     */       }
/*     */     }
/*  97 */     .addDefaultCharset(), new SourceHttpMessageConverter() {
/*     */       public SourceHttpMessageConverter<Source> addDefaultCharset() {
/*  99 */         super.setDefaultCharset(RestfulConverter.DEFAULT_CHARSET);
/* 100 */         return this;
/*     */       }
/*     */     }
/* 102 */     .addDefaultCharset() });
/*     */   }
/*     */ 
/*     */   public boolean canRead(Type type, Class<?> contextClass, MediaType mediaType)
/*     */   {
/* 107 */     if (mediaType == null) {
/* 108 */       return false;
/*     */     }
/* 110 */     return canRead(mediaType);
/*     */   }
/*     */ 
/*     */   public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage)
/*     */     throws IOException, HttpMessageNotReadableException
/*     */   {
/* 116 */     Class targetClass = (type instanceof Class) ? (Class)type : null;
/* 117 */     if ((type instanceof Class)) {
/* 118 */       return readInternal(targetClass, inputMessage);
/*     */     }
/* 120 */     ResolvableType targetType = ResolvableType.forType(type);
/* 121 */     Object obj = readInternal(targetType.getRawClass(), inputMessage);
/* 122 */     return read(targetType, obj);
/*     */   }
/*     */ 
/*     */   public final Object read(ResolvableType targetType, Object value)
/*     */   {
/* 127 */     if ((targetType == null) || (value == null)) {
/* 128 */       return value;
/*     */     }
/* 130 */     if (Response.class.isAssignableFrom(targetType.getRawClass())) {
/* 131 */       Response res = (Response)value;
/* 132 */       if ((null != res) && (res.getData() != null)) {
/* 133 */         return addData(res, addData(targetType.getGeneric(new int[] { 0 }), res.getData()));
/*     */       }
/*     */     }
/* 136 */     if (Request.class.isAssignableFrom(targetType.getRawClass())) {
/* 137 */       Request req = (Request)value;
/* 138 */       if ((null != req) && (req.getData() != null)) {
/* 139 */         return addData(req, addData(targetType.getGeneric(new int[] { 0 }), req.getData()));
/*     */       }
/*     */     }
/* 142 */     return value;
/*     */   }
/*     */ 
/*     */   public boolean canWrite(Type type, Class<?> clazz, MediaType mediaType)
/*     */   {
/* 147 */     return canWrite(mediaType);
/*     */   }
/*     */ 
/*     */   public void write(Object t, Type type, MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException
/*     */   {
/* 152 */     writeInternal(t, outputMessage);
/*     */   }
/*     */ 
/*     */   private Object addData(ResolvableType targetType, Object targetData) {
/* 156 */     if ((Map.class.isAssignableFrom(targetType.getRawClass())) && 
/* 157 */       ((targetData instanceof JSONObject))) {
/* 158 */       Map map = Maps.newHashMap();
/* 159 */       for (Map.Entry ety : ((JSONObject)targetData).entrySet()) {
/* 160 */         map.put(addData(targetType.getGeneric(new int[] { 0 }), ety.getKey()), addData(targetType.getGeneric(new int[] { 1 }), ety.getValue()));
/*     */       }
/* 162 */       return map;
/*     */     }
/*     */ 
/* 165 */     if ((List.class.isAssignableFrom(targetType.getRawClass())) && 
/* 166 */       ((targetData instanceof JSONArray))) {
/* 167 */       List list = Lists.newArrayList();
/* 168 */       for (??? = ((JSONArray)targetData).iterator(); ???.hasNext(); ) { Object arr = ???.next();
/* 169 */         list.add(addData(targetType.getGeneric(new int[] { 0 }), arr));
/*     */       }
/* 171 */       return list;
/*     */     }
/*     */ 
/* 174 */     if (CollectionUtil.isEmpty(targetType.getGenerics())) {
/* 175 */       if ((targetData instanceof JSONObject)) {
/* 176 */         return JSON.toJavaObject((JSONObject)targetData, targetType.getRawClass());
/*     */       }
/* 178 */       if ((targetData instanceof JSONArray)) {
/* 179 */         throw new JSONException("Not support data type");
/*     */       }
/* 181 */       return targetData;
/*     */     }
/*     */ 
/* 184 */     if (targetType.getGenerics().length > 1) {
/* 185 */       throw new JSONException("Not support too many generic type");
/*     */     }
/* 187 */     if ((targetData instanceof JSONArray)) {
/* 188 */       throw new JSONException("Not support data type");
/*     */     }
/* 190 */     if ((targetData instanceof JSONObject)) {
/* 191 */       Object targetObject = ReflectUtils.newInstance(targetType.getRawClass());
/* 192 */       for (Map.Entry entry : ((JSONObject)targetData).entrySet()) {
/* 193 */         Object value = entry.getValue();
/* 194 */         if (value == null)
/*     */           continue;
/* 196 */         if ((value instanceof JSONArray))
/* 197 */           value = addData(ResolvableType.forClassWithGenerics(List.class, new ResolvableType[] { targetType.getGeneric(new int[] { 0 }) }), value);
/* 198 */         else if ((value instanceof JSONObject)) {
/* 199 */           value = addData(ResolvableType.forClassWithGenerics(Map.class, new ResolvableType[] { targetType.getGeneric(new int[] { 0 }), targetType.getGeneric(new int[] { 1 }) }), value);
/*     */         }
/* 201 */         ClassUtil.setDeclaredFieldValue(targetObject, (String)entry.getKey(), value);
/*     */       }
/* 203 */       return targetObject;
/*     */     }
/* 205 */     return addData(targetType.getGeneric(new int[] { 0 }), targetData);
/*     */   }
/*     */ 
/*     */   private <T> Response<T> addData(Response<T> res, Object data)
/*     */   {
/* 212 */     res.setData(data);
/* 213 */     return res;
/*     */   }
/*     */ 
/*     */   private <T> Request<T> addData(Request<T> req, Object data)
/*     */   {
/* 218 */     req.setData(data);
/* 219 */     return req;
/*     */   }
/*     */ }

/* Location:           C:\Users\Administrator\Desktop\container\guludai-container-web\0.0.1-SNAPSHOT\guludai-container-web-0.0.1-20180428.084950-107.jar
 * Qualified Name:     cn.guludai.container.web.restful.RestfulConverter
 * JD-Core Version:    0.6.0
 */