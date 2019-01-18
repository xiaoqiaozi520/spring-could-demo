package cn.guludai.framework.toolkit.util;

import org.springframework.beans.BeanUtils;

public abstract class BeanUtil extends BeanUtils
{
    public static <T> T copyProperties(Object source, Class<T> clazz)
    {
        Object t = instantiate(clazz);
        copyProperties(source, t);
        return (T) t;
    }
}