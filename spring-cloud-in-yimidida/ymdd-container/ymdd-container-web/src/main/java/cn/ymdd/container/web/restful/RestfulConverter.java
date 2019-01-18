//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cn.ymdd.container.web.restful;

import cn.ymdd.api.data.Request;
import cn.ymdd.api.data.Response;
import cn.ymdd.framework.toolkit.util.ClassUtil;
import cn.ymdd.framework.toolkit.util.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.xml.transform.Source;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.core.ResolvableType;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.GenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;

class RestfulConverter extends FastJsonHttpMessageConverter implements GenericHttpMessageConverter<Object> {
    private static final int SUPPORT_GENERICS = 1;
    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
    public static final HttpMessageConverter<?> JSON_CONVERTER = getJsonConverter();
    public static final HttpMessageConverter<?> STRING_CONVERTER = getStringConverter();
    public static final HttpMessageConverter<?> FORM_CONVERTER = getFormConverter();
    public static final List<HttpMessageConverter<?>> ALL_CONVERTER = getAllConverter();

    RestfulConverter() {
    }

    protected static final HttpMessageConverter<?> getJsonConverter() {
        RestfulConverter json = new RestfulConverter();
        json.setFeatures(new SerializerFeature[]{SerializerFeature.SkipTransientField, SerializerFeature.QuoteFieldNames, SerializerFeature.WriteDateUseDateFormat, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullListAsEmpty, SerializerFeature.WriteNullBooleanAsFalse});
        json.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON_UTF8, MediaType.APPLICATION_JSON));
        json.setDefaultCharset(DEFAULT_CHARSET);
        json.setCharset(DEFAULT_CHARSET);
        return json;
    }

    protected static final HttpMessageConverter<?> getStringConverter() {
        StringHttpMessageConverter string = new StringHttpMessageConverter();
        string.setSupportedMediaTypes(Arrays.asList(MediaType.TEXT_PLAIN));
        string.setDefaultCharset(DEFAULT_CHARSET);
        string.setWriteAcceptCharset(false);
        return string;
    }

    protected static final HttpMessageConverter<?> getFormConverter() {
        FormHttpMessageConverter form = new FormHttpMessageConverter();
        form.addPartConverter(new ByteArrayHttpMessageConverter());
        form.addPartConverter(new ResourceHttpMessageConverter());
        form.addPartConverter(new SourceHttpMessageConverter());
        form.addPartConverter(STRING_CONVERTER);
        form.addPartConverter(JSON_CONVERTER);
        form.setCharset(DEFAULT_CHARSET);
        return form;
    }

    protected static final List<HttpMessageConverter<?>> getAllConverter() {
        return Arrays.asList(STRING_CONVERTER, JSON_CONVERTER, FORM_CONVERTER, (new ResourceHttpMessageConverter() {
            public ResourceHttpMessageConverter addDefaultCharset() {
                super.setDefaultCharset(RestfulConverter.DEFAULT_CHARSET);
                return this;
            }
        }).addDefaultCharset(), (new ByteArrayHttpMessageConverter() {
            public ByteArrayHttpMessageConverter addDefaultCharset() {
                super.setDefaultCharset(RestfulConverter.DEFAULT_CHARSET);
                return this;
            }
        }).addDefaultCharset(), (new SourceHttpMessageConverter<Source>() {
            public SourceHttpMessageConverter<Source> addDefaultCharset() {
                super.setDefaultCharset(RestfulConverter.DEFAULT_CHARSET);
                return this;
            }
        }).addDefaultCharset());
    }

    public boolean canRead(Type type, Class<?> contextClass, MediaType mediaType) {
        return mediaType == null ? false : this.canRead(mediaType);
    }

    public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        Class<?> targetClass = type instanceof Class ? (Class)type : null;
        if (type instanceof Class) {
            return this.readInternal(targetClass, inputMessage);
        } else {
            ResolvableType targetType = ResolvableType.forType(type);
            return this.read(targetType, this.readInternal(targetType.getRawClass(), inputMessage));
        }
    }

    public final Object read(ResolvableType targetType, Object value) {
        if (targetType != null && value != null) {
            if (Response.class.isAssignableFrom(targetType.getRawClass())) {
                Response<?> res = (Response)value;
                if (null != res && res.getData() != null) {
                    this.setResponseData(res, this.resolveData(targetType.getGeneric(new int[]{0}), res.getData()));
                }
            }

            if (Request.class.isAssignableFrom(targetType.getRawClass())) {
                Request<?> req = (Request)value;
                if (null != req && req.getData() != null) {
                    this.setRequestData(req, this.resolveData(targetType.getGeneric(new int[]{0}), req.getData()));
                }
            }

            return value;
        } else {
            return value;
        }
    }

    public boolean canWrite(Type type, Class<?> clazz, MediaType mediaType) {
        return this.canWrite(mediaType);
    }

    public void write(Object t, Type type, MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        this.writeInternal(t, outputMessage);
    }

    private Object resolveData(ResolvableType targetType, Object targetData) {
        Iterator var4;
        Entry entry;
        if (Map.class.isAssignableFrom(targetType.getRawClass()) && targetData instanceof JSONObject) {
            Map<Object, Object> map = Maps.newHashMap();
            var4 = ((JSONObject)targetData).entrySet().iterator();

            while(var4.hasNext()) {
                entry = (Entry)var4.next();
                map.put(this.resolveData(targetType.getGeneric(new int[]{0}), entry.getKey()), this.resolveData(targetType.getGeneric(new int[]{1}), entry.getValue()));
            }

            return map;
        } else if (List.class.isAssignableFrom(targetType.getRawClass()) && targetData instanceof JSONArray) {
            List<Object> list = Lists.newArrayList();
            var4 = ((JSONArray)targetData).iterator();

            while(var4.hasNext()) {
                Object arr = var4.next();
                list.add(this.resolveData(targetType.getGeneric(new int[]{0}), arr));
            }

            return list;
        } else if (CollectionUtil.isEmpty(targetType.getGenerics())) {
            if (targetData instanceof JSONObject) {
                return JSON.toJavaObject((JSONObject)targetData, targetType.getRawClass());
            } else if (targetData instanceof JSONArray) {
                throw new JSONException("Not support data type");
            } else {
                return targetData;
            }
        } else if (targetType.getGenerics().length > 1) {
            throw new JSONException("Only support one generic type");
        } else if (targetData instanceof JSONArray) {
            throw new JSONException("Not support data type");
        } else if (targetData instanceof JSONObject) {
            Object targetObject = ReflectUtils.newInstance(targetType.getRawClass());
            var4 = ((JSONObject)targetData).entrySet().iterator();

            while(var4.hasNext()) {
                entry = (Entry)var4.next();
                Object value = entry.getValue();
                if (value instanceof JSON) {
                    ResolvableType clazz = this.resolveType(targetType, targetType.getRawClass(), (String)entry.getKey());
                    value = this.resolveData(ResolvableType.forClassWithGenerics(clazz.getRawClass(), clazz.getGenerics()), value);
                }

                if (value != null) {
                    ClassUtil.setDeclaredFieldValue(targetObject, (String)entry.getKey(), value);
                }
            }

            return targetObject;
        } else {
            return this.resolveData(targetType.getGeneric(new int[]{0}), targetData);
        }
    }

    private ResolvableType resolveType(ResolvableType type, Class<?> clazz, String field) {
        try {
            Field fd = clazz.getDeclaredField(field);
            ResolvableType[] cts = ResolvableType.forClass(clazz).getGenerics();
            if (CollectionUtil.isNotEmpty(cts)) {
                for(int i = 0; i < cts.length; ++i) {
                    if (fd.getGenericType().getTypeName().equals(cts[i].getType().getTypeName())) {
                        return type.getGeneric(new int[]{i});
                    }
                }
            }

            return ResolvableType.forClassWithGenerics(fd.getType(), type.getGenerics());
        } catch (Exception var7) {
            throw new IllegalStateException(var7);
        }
    }

    private <T> void setRequestData(Request<T> req, Object data) {
        req.setData(data);
    }

    private <T> void setResponseData(Response<T> res, Object data) {
        res.setData(data);
    }
}
