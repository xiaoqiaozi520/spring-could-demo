package cn.ymdd.framework.cache.client;

import cn.ymdd.framework.cache.command.RedisAdvice;
import cn.ymdd.framework.toolkit.util.ClassUtil;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.springframework.data.redis.connection.DefaultStringRedisConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisConnectionUtils;

final class RedisFactory extends JedisConnectionFactory
{
    private static final ThreadLocal<ContextHolder> context = new ThreadLocal() {
        protected RedisFactory.ContextHolder initialValue() {
            return new RedisFactory.ContextHolder(null);
        }
    };

    static final ClassLoader getSupportLoader()
    {
        return RedisClient.class.getClassLoader();
    }

    static final Class<?>[] getSupportType() {
        return ClassUtil.getAllInterfacesForClass(RedisClient.class, getSupportLoader());
    }

    public static final RedisFactory createJedisFactory(RedisConfig config) {
        RedisFactory factory = new RedisFactory();
        factory.afterConfiguresSet(config);
        return factory;
    }

    public final RedisClient getJedisTemplate() {
        return getJedisTemplate(this);
    }

    protected final RedisClient getJedisTemplate(RedisFactory factory) {
        return (RedisClient)Proxy.newProxyInstance(getSupportLoader(), getSupportType(), new Object(factory)
        {
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
            {
                String name = method.getName();
                switch (name) {
                    case "hashCode":
                        return Integer.valueOf(System.identityHashCode(proxy));
                    case "equals":
                        return Boolean.valueOf(proxy == args[0]);
                    case "close":
                        return null;
                }

                RedisFactory.ContextHolder holder = ((RedisFactory.ContextHolder)RedisFactory.context.get()).updContextHolder(name);
                RedisConnection conn = null;
                try {
                    Exception error = (Exception) method.invoke(conn = holder.getConnection(), args);
                    if (RedisFactory.this.preAdvice((RedisClient)proxy, method, args)) {
                        error = RedisFactory.this.postAdvice((RedisClient)proxy, method, args);
                        RedisFactory.this.endAdvice((RedisClient)proxy, method, args);
                        if (error == null) {
                            List localList = holder.getResult();
                            return localList;
                        }
                        throw error;
                    }

                    if (holder.getAdvice().isStatus()) {
                        holder.addStore(name, method.getReturnType(), null);
                    }
                    if (holder.getConnection() == null) {
                        holder.setConnection(RedisFactory.this.getConnection());
                    }

                    return error;
                }
                catch (Exception e) {
                    if ((e instanceof InvocationTargetException)) {
                        throw ((InvocationTargetException)e).getTargetException();
                    }
                    throw e;
                } finally {
                    if (!holder.hasContextHolder()) {
                        RedisConnectionUtils.releaseConnection(conn, factory);
                        RedisFactory.context.remove(); }
                }
            }
        });
    }

    final boolean preAdvice(RedisClient template, Method method, Object[] args)
    {
        if (args != null) {
            for (Object arg : args) {
                if ((arg instanceof RedisAdvice)) {
                    return ((RedisAdvice)arg).prepare(template, method, args);
                }
            }
        }
        return false;
    }

    final Exception postAdvice(RedisClient template, Method method, Object[] args) {
        if (args != null) {
            for (Object arg : args) {
                if ((arg instanceof RedisAdvice)) {
                    ((ContextHolder)context.get()).updContextHolder("before");
                    return (Exception)((RedisAdvice)arg).execute(template, method, args);
                }
            }
        }
        return null;
    }

    final void endAdvice(RedisClient template, Method method, Object[] args) {
        if (args != null)
        {
            Iterator iter;
            for (Object arg : args)
                if ((arg instanceof RedisAdvice)) {
                    ContextHolder holder = ((ContextHolder)context.get()).updContextHolder("after");
                    List val = ((RedisAdvice)arg).commit(template, method, args);
                    iter = val.iterator();
                    for (ContextStore store : holder.getStore())
                        if (!store.isVoid())
                            store.setValue(iter.next());
                }
        }
    }

    final void afterConfiguresSet(RedisConfig config)
    {
        config.setMaxIdle(config.getMaxTotal());
        setHostName(config.getHostName());
        setPassword(config.getPassword());
        setTimeout(config.getTimeout());
        setPort(config.getPort());
        setPoolConfig(config);
        afterPropertiesSet();
    }

    static final class ContextStore
    {
        private Object value;
        private final Class<?> type;
        private final String command;

        public ContextStore(String command, Class<?> type, Object value)
        {
            this.command = command;
            this.value = value;
            this.type = type;
        }

        public boolean isVoid() {
            return Void.class.getSimpleName().equalsIgnoreCase(this.type.getName());
        }

        public String getCommand() {
            return this.command;
        }

        public Object getValue() {
            return this.value;
        }

        public void setValue(Object value) {
            this.value = value;
        }
    }

    static final class ContextStatus
    {
        private final String started;
        private final String closed;
        private boolean status = false;

        public ContextStatus(String started, String closed) {
            this.started = started;
            this.closed = closed;
        }

        public void setStatus(String status) {
            if (this.started.equalsIgnoreCase(status)) {
                this.status = true;
            }
            if (this.closed.equalsIgnoreCase(status))
                this.status = false;
        }

        public boolean isStatus()
        {
            return this.status;
        }
    }

    static final class ContextHolder
    {
        private RedisConnection connection;
        private final RedisFactory.ContextStatus mvcc;
        private final RedisFactory.ContextStatus channel;
        private final RedisFactory.ContextStatus session;
        private final RedisFactory.ContextStatus advice;
        private final List<RedisFactory.ContextStore> store = new ArrayList();

        public ContextHolder(RedisConnection connection) {
            this.channel = new RedisFactory.ContextStatus("openPipeline", "closePipeline");
            this.advice = new RedisFactory.ContextStatus("before", "after");
            this.session = new RedisFactory.ContextStatus("multi", "exec");
            this.mvcc = new RedisFactory.ContextStatus("watch", "unwatch");
            this.connection = connection;
        }

        public ContextHolder updContextHolder(String status) {
            this.session.setStatus(status);
            this.channel.setStatus(status);
            this.advice.setStatus(status);
            this.mvcc.setStatus(status);
            return this;
        }

        public boolean hasContextHolder() {
            return (this.advice.isStatus()) || (this.mvcc.isStatus()) || (this.channel.isStatus()) || (this.session.isStatus());
        }

        public ContextHolder setConnection(RedisConnection connection) {
            this.connection = new DefaultStringRedisConnection(connection);
            return this;
        }

        public void addStore(String name, Class<?> type, Object value) {
            this.store.add(new RedisFactory.ContextStore(name, type, value));
        }

        public RedisConnection getConnection() {
            return this.connection;
        }

        public RedisFactory.ContextStatus getAdvice() {
            return this.advice;
        }

        public List<RedisFactory.ContextStore> getStore() {
            return Collections.unmodifiableList(this.store);
        }

        public List<RedisResult> getResult() {
            List result = new ArrayList();
            for (RedisFactory.ContextStore cs : this.store) {
                result.add(new RedisResult(cs.getCommand(), cs.getValue()));
            }
            return result;
        }
    }
}