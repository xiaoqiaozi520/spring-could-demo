package cn.ymdd.framework.toolkit.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

public abstract class ClassUtil extends ClassUtils {
    public static List<Field> getDeclaredField(Class<?> clz) {
        try {
            List fds = new ArrayList();
            Field[] fd = clz.getDeclaredFields();
            for (Field field : fd) {
                field.setAccessible(true);
                fds.add(field);
            }
            return fds;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

    }

    public static Field getDeclaredField(Class<?> clz, String field) {
        try {
            Field fd = clz.getDeclaredField(field);
            fd.setAccessible(true);
            return fd;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

    }

    public static List<String> getDeclaredFieldNames(Class<?> clz) {
        try {
            List names = new ArrayList();
            Field[] fd = clz.getDeclaredFields();
            for (Field field : fd) {
                names.add(field.getName());
            }
            return names;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

    }

    public static <T> T getDeclaredFieldValue(Object target, String field) {
        boolean fds = false;
        boolean mds = false;
        Field fd = null;
        try {
            fd = ReflectionUtils.findField(target.getClass(), field);
            fds = fd.isAccessible();
            fd.setAccessible(true);
            Object localObject1 = ReflectionUtils.getField(fd, target);
            return (T) localObject1;
        } catch (Exception e) {
            Method metd = null;
            try {
                String name = "get" + StringUtil.capitalize(field);
                metd = ReflectionUtils.findMethod(target.getClass(), name);
                mds = metd.isAccessible();
                metd.setAccessible(true);
                Object localObject2 = ReflectionUtils.invokeMethod(metd, target);

                if (metd != null)
                    metd.setAccessible(mds);
                return (T) localObject2;
            } catch (Exception e2) {
                throw new IllegalStateException(e2);
            } finally {
                if (metd != null)
                    metd.setAccessible(mds);
            }
        } finally {
            if (fd != null)
                fd.setAccessible(fds);
        }

    }

    public static void setDeclaredFieldValue(Object target, String field, Object value) {
        boolean fds = false;
        boolean mds = false;
        Field fd = null;
        try {
            fd = ReflectionUtils.findField(target.getClass(), field);
            fds = fd.isAccessible();
            fd.setAccessible(true);
            ReflectionUtils.setField(fd, target, value);
        } catch (Exception e) {
            Method metd = null;
            try {
                String name = "set" + StringUtil.capitalize(field);
                metd = ReflectionUtils.findMethod(target.getClass(), name, new Class[]{value.getClass()});
                mds = metd.isAccessible();
                metd.setAccessible(true);
                ReflectionUtils.invokeMethod(metd, target, new Object[]{value});
            } catch (Exception e2) {
                throw new IllegalStateException(e2);
            } finally {
                if (metd != null)
                    metd.setAccessible(mds);
            }
        } finally {
            if (fd != null)
                fd.setAccessible(fds);
        }
    }

    public static <T> T getDeclaredMethodValue(Object target, String methodName) {
        boolean mds = false;
        Method md = null;
        try {
            md = ReflectionUtils.findMethod(target.getClass(), methodName);
            mds = md.isAccessible();
            md.setAccessible(true);
            Object localObject1 = ReflectionUtils.invokeMethod(md, target);
            return (T) localObject1;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            if (md != null)
                md.setAccessible(mds);
        }
    }

    public static <T> void setDeclaredMethodValue(Object target, String methodName, T value) {
        boolean mds = false;
        Method md = null;
        try {
            md = ReflectionUtils.findMethod(target.getClass(), methodName, new Class[]{value.getClass()});
            mds = md.isAccessible();
            md.setAccessible(true);
            ReflectionUtils.invokeJdbcMethod(md, target, new Object[]{value});
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            if (md != null)
                md.setAccessible(mds);
        }
    }
}