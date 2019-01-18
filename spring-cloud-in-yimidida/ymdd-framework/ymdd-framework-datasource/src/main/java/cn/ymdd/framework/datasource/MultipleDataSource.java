//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cn.ymdd.framework.datasource;

import cn.ymdd.framework.datasource.bind.DataSourceMapper;
import cn.ymdd.framework.toolkit.util.ClassUtil;
import cn.ymdd.framework.toolkit.util.CollectionUtil;
import cn.ymdd.framework.toolkit.util.StringUtil;
import com.alibaba.druid.pool.DruidDataSource;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.sql.DataSource;
import org.apache.ibatis.executor.statement.BaseStatementHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.ProxyMethodInvocation;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.ReflectionUtils;

abstract class MultipleDataSource extends AbstractRoutingDataSource implements EnvironmentAware {
    private static final Logger log = LoggerFactory.getLogger(MultipleDataSource.class);
    private static final ThreadLocal<MultipleDataSource.RoutingChain> sourcesHolder = new ThreadLocal();
    private final Map<Class<?>, MultipleDataSource.RoutingModel> sourcesProxy = Maps.newConcurrentMap();
    private final Map<Object, Object> sourcesMapper = Maps.newConcurrentMap();
    private final List<String> sourcesDatabase = Lists.newArrayList();
    private Environment environment;
    private Boolean inited = false;

    MultipleDataSource() {
    }

    protected final String determineCurrentEnvironmentKey(String... keys) {
        return StringUtil.join(keys, ".");
    }

    protected final Object determineCurrentLookupKey() {
        MultipleDataSource.RoutingChain chain = (MultipleDataSource.RoutingChain)sourcesHolder.get();
        if (null == chain) {
            throw new NullPointerException("@DataSourceMapper");
        } else {
            return chain.getValue();
        }
    }

    public synchronized void setEnvironment(Environment environment) {
        if (!this.inited) {
            this.environment = environment;
            String dbs = environment.getProperty("mysql.database");
            if (StringUtil.isNotEmpty(dbs)) {
                String[] var3 = dbs.split(",");
                int var4 = var3.length;

                for(int var5 = 0; var5 < var4; ++var5) {
                    String db = var3[var5];
                    this.sourcesDatabase.add(db);
                }
            }
        }

    }

    @PreDestroy
    public final synchronized void closeDataSource() {
        try {
            if (this.inited) {
                Iterator var1 = this.sourcesMapper.values().iterator();

                while(var1.hasNext()) {
                    Object ds = var1.next();
                    if (ds instanceof DruidDataSource && ((DruidDataSource)ds).isInited()) {
                        ((DruidDataSource)ds).close();
                    }
                }
            }
        } finally {
            this.sourcesDatabase.clear();
            this.sourcesMapper.clear();
            this.sourcesProxy.clear();
        }

    }

    @PostConstruct
    public synchronized void initedDataSource() {
        if (this.sourcesDatabase != null && !this.sourcesDatabase.isEmpty()) {
            String max = String.valueOf(50);
            String min = String.valueOf(5);
            if (!this.inited && this.inited == true) {
                Iterator var3 = this.sourcesDatabase.iterator();

                while(var3.hasNext()) {
                    String db = (String)var3.next();
                    String username = this.environment.getProperty(this.determineCurrentEnvironmentKey("mysql", db, "username"));
                    String password = this.environment.getProperty(this.determineCurrentEnvironmentKey("mysql", db, "password"));
                    String maxActive = this.environment.getProperty(this.determineCurrentEnvironmentKey("mysql", db, "max"), max);
                    String minIdle = this.environment.getProperty(this.determineCurrentEnvironmentKey("mysql", db, "min"), min);
                    String url = this.environment.getProperty(this.determineCurrentEnvironmentKey("mysql", db, "url"));
                    DruidDataSource druid = new DruidDataSource();
                    druid.setMaxActive(Integer.parseInt(maxActive));
                    druid.setMinIdle(Integer.parseInt(minIdle));
                    druid.setUsername(username);
                    druid.setPassword(password);
                    druid.setUrl(url);
                    DataSourceHelper.checkDataSource(druid);
                    DataSourceHelper.configDataSourceProperties(druid);
                    DataSourceHelper.configDataSourceArguments(druid);
                    this.sourcesMapper.put(db, druid);
                }

                this.setTargetDataSources(this.sourcesMapper);
                if (this.sourcesDatabase.size() == 1) {
                    this.setDefaultTargetDataSource(this.sourcesMapper.get(this.sourcesDatabase.get(0)));
                }
            }

        } else {
            throw new NullPointerException("mysql.database");
        }
    }

    @Bean
    @Primary
    @Autowired
    public SqlSessionFactoryBean initedSessionFactoryBean(final DataSource dataSource) {
        return (new SqlSessionFactoryBean() {
            public SqlSessionFactoryBean init() {
                try {
                    this.setMapperLocations((new PathMatchingResourcePatternResolver()).getResources("classpath*:mappers/**/*.xml"));
                    this.setConfigLocation(new ClassPathResource("mybatis-config.xml"));
                    this.setPlugins(new Interceptor[]{MultipleDataSource.this.new RoutingInterceptor()});
                    this.setDataSource(dataSource);
                    return this;
                } catch (IOException var2) {
                    throw new IllegalStateException(var2);
                }
            }
        }).init();
    }

    @Bean
    @Primary
    @Autowired
    public PlatformTransactionManager initedTransactionManager(DataSource dataSource) {
        return (new MultipleDataSource.RoutingTransaction(dataSource) {
            public PlatformTransactionManager init() {
                try {
                    this.afterPropertiesSet();
                    return this;
                } catch (Exception var2) {
                    throw new IllegalStateException(var2);
                }
            }
        }).init();
    }

    static final void refreshChain(boolean source, String value) {
        MultipleDataSource.RoutingChain chain = (MultipleDataSource.RoutingChain)sourcesHolder.get();
        if (chain == null) {
            sourcesHolder.set((new MultipleDataSource.RoutingChain(source)).setValue(value));
        } else {
            chain.setTail((new MultipleDataSource.RoutingChain(source)).setValue(value));
            sourcesHolder.set(chain.getTail());
            chain.getTail().setHead(chain);
        }

    }

    static final void removeChain(boolean source) {
        MultipleDataSource.RoutingChain chain = (MultipleDataSource.RoutingChain)sourcesHolder.get();
        if (null != chain && (source && chain.isOwner() || !source && !chain.isOwner())) {
            if (chain.getHead() != null) {
                chain.getHead().setTail((MultipleDataSource.RoutingChain)null);
            }

            sourcesHolder.remove();
            sourcesHolder.set(chain.getHead());
        }

    }

    final MultipleDataSource.RoutingModel resolveMethod(Class<?> clazz, String sign, String name, Object... args) {
        MultipleDataSource.RoutingModel model = (MultipleDataSource.RoutingModel)this.sourcesProxy.get(clazz);
        if (model == null) {
            Map var6 = this.sourcesProxy;
            synchronized(this.sourcesProxy) {
                if (null == (model = (MultipleDataSource.RoutingModel)this.sourcesProxy.get(clazz))) {
                    DataSourceMapper dsm = this.resolveClass(clazz);
                    model = new MultipleDataSource.RoutingModel(dsm == null ? "" : dsm.database());
                    this.sourcesProxy.put(clazz, model);
                }
            }
        }

        if (null == model.getSignature(sign)) {
            synchronized(model) {
                if (!model.isSignature(sign)) {
                    Method md = ReflectionUtils.findMethod(clazz, name, CollectionUtil.isEmpty(args) ? null : (Class[])((Class[])args));
                    DataSourceMapper dsm = (DataSourceMapper)md.getAnnotation(DataSourceMapper.class);
                    model.addSignature(sign, dsm == null ? "" : dsm.database());
                }
            }
        }

        return model;
    }

    final DataSourceMapper resolveClass(Class<?> clazz) {
        if (Object.class != clazz) {
            Class clzaa = clazz;

            while(clzaa != Object.class) {
                DataSourceMapper datasource = (DataSourceMapper)clzaa.getAnnotation(DataSourceMapper.class);
                clzaa = clzaa.getSuperclass();
                if (datasource != null) {
                    return datasource;
                }
            }

            Class<?>[] clzss = clazz.getInterfaces();
            Class[] var4 = clzss;
            int var5 = clzss.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                Class<?> clssz = var4[var6];
                DataSourceMapper datasource = (DataSourceMapper)clssz.getAnnotation(DataSourceMapper.class);
                if (datasource != null) {
                    return datasource;
                }
            }
        }

        return null;
    }

    protected static class RoutingModel implements Serializable {
        private static final long serialVersionUID = -4605379820182634454L;
        private Map<String, String> signature = Maps.newConcurrentMap();
        private final String dbase;

        public RoutingModel(String dbase) {
            this.dbase = dbase;
        }

        public String getDbase(String md) {
            return this.isSignature(md) ? this.getSignature(md) : this.dbase;
        }

        public MultipleDataSource.RoutingModel addSignature(String md, String dbase) {
            this.signature.put(md, dbase);
            return this;
        }

        public boolean isSignature(String md) {
            return StringUtil.isNotEmpty(this.getSignature(md));
        }

        public String getSignature(String md) {
            return (String)this.signature.get(md);
        }
    }

    protected static class RoutingChain implements Serializable {
        private static final long serialVersionUID = -279594865632707595L;
        private MultipleDataSource.RoutingChain head;
        private MultipleDataSource.RoutingChain tail;
        private boolean trans;
        private boolean open;
        private final boolean owner;
        private int time = -1;
        private String value;

        public RoutingChain(boolean owner) {
            this.owner = owner;
        }

        public boolean isSameChannel() {
            return this.head == null || StringUtil.isNotEmpty(this.value) && this.value.equals(this.head.getValue());
        }

        public MultipleDataSource.RoutingChain setTail(MultipleDataSource.RoutingChain tail) {
            this.tail = tail;
            return this;
        }

        public MultipleDataSource.RoutingChain setHead(MultipleDataSource.RoutingChain head) {
            this.head = head;
            return this;
        }

        public MultipleDataSource.RoutingChain setValue(String value) {
            this.value = value;
            return this;
        }

        public MultipleDataSource.RoutingChain setTime(int time) {
            this.time = time;
            return this;
        }

        public void setTrans(boolean trans) {
            this.trans = trans;
        }

        public void setOpen(boolean open) {
            this.open = open;
        }

        public MultipleDataSource.RoutingChain getHead() {
            return this.head;
        }

        public MultipleDataSource.RoutingChain getTail() {
            return this.tail;
        }

        public String getValue() {
            return this.value;
        }

        public boolean isOwner() {
            return this.owner;
        }

        public boolean isTrans() {
            return this.trans;
        }

        public boolean isOpen() {
            return this.open;
        }

        public int getTime() {
            return this.time;
        }
    }

    @Aspect
    @Component
    @Order(-2147483648)
    protected class RoutingLookup implements Serializable {
        private static final long serialVersionUID = 211232186025093413L;

        protected RoutingLookup() {
        }

        @Pointcut("execution(public * cn.guludai..domain..*.*(..))")
        public void instanceLookupDomain() {
        }

        @Pointcut("execution(public * cn.guludai..service..*.*(..))")
        public void instanceLookupService() {
        }

        @Around("instanceLookupDomain() || instanceLookupService()")
        public Object instance(ProceedingJoinPoint joinPoint) throws Throwable {
            if (!(joinPoint instanceof MethodInvocationProceedingJoinPoint)) {
                throw new IllegalStateException("Not support");
            } else {
                ProxyMethodInvocation method = (ProxyMethodInvocation)ClassUtil.getDeclaredFieldValue(joinPoint, "methodInvocation");
                Class<?> clazz = AopUtils.getTargetClass(joinPoint.getTarget());
                String sign = joinPoint.getSignature().toString();
                String name = joinPoint.getSignature().getName();
                Class<?>[] args = method.getMethod().getParameterTypes();
                MultipleDataSource.RoutingModel model = MultipleDataSource.this.resolveMethod(clazz, sign, name, (Object[])args);
                String value = model.getDbase(sign);
                if (StringUtil.isEmpty(value)) {
                    return joinPoint.proceed();
                } else {
                    Object var9;
                    try {
                        MultipleDataSource.refreshChain(true, value);
                        var9 = joinPoint.proceed();
                    } finally {
                        MultipleDataSource.removeChain(true);
                    }

                    return var9;
                }
            }
        }
    }

    protected static class RoutingTransaction implements PlatformTransactionManager, InitializingBean {
        private final DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        private static final int MIN_TRANSACTION_TIMEOUT = 1;
        private static final int MAX_TRANSACTION_TIMEOUT = 30;

        public RoutingTransaction(DataSource dataSource) {
            this.transactionManager.setDataSource(dataSource);
        }

        public TransactionStatus getTransaction(TransactionDefinition definition) throws TransactionException {
            int time = definition.getTimeout();
            if (time <= 1 || time > 30) {
                time = 30;
            }

            DefaultTransactionDefinition defaults = new DefaultTransactionDefinition(definition);
            MultipleDataSource.RoutingChain chain = (MultipleDataSource.RoutingChain)MultipleDataSource.sourcesHolder.get();
            if (chain == null) {
                defaults.setTimeout(time);
            } else if (chain.isSameChannel()) {
                defaults.setTimeout(chain.setTime(time).getTime());
                defaults.setPropagationBehavior(0);
            } else {
                defaults.setPropagationBehavior(3);
                if (chain.getHead() != null && time > chain.getTime() / 2) {
                    defaults.setTimeout(chain.setTime(chain.getTime() / 2).getTime());
                } else {
                    defaults.setTimeout(chain.setTime(time).getTime());
                }
            }

            return this.transactionManager.getTransaction(defaults);
        }

        public void commit(TransactionStatus status) throws TransactionException {
            this.transactionManager.commit(status);
        }

        public void rollback(TransactionStatus status) throws TransactionException {
            this.transactionManager.rollback(status);
        }

        public void afterPropertiesSet() throws Exception {
            if (this.transactionManager.getDataSource() == null) {
                throw new IllegalArgumentException("Property 'dataSource' is required");
            }
        }
    }

    @Intercepts({@Signature(
            type = StatementHandler.class,
            method = "prepare",
            args = {Connection.class, Integer.class}
    )})
    protected class RoutingInterceptor implements Interceptor {
        private final Field mappedDelegate = ClassUtil.getDeclaredField(RoutingStatementHandler.class, "delegate");
        private final Field mappedStatement = ClassUtil.getDeclaredField(BaseStatementHandler.class, "mappedStatement");

        public RoutingInterceptor() {
        }

        public void setProperties(Properties properties) {
            if (MultipleDataSource.log.isDebugEnabled()) {
                MultipleDataSource.log.debug("MultipleInterceptor properties: " + properties);
            }

        }

        public Object intercept(Invocation invocation) throws Throwable {
            Object var2;
            try {
                var2 = invocation.proceed();
            } finally {
                MultipleDataSource.removeChain(false);
            }

            return var2;
        }

        public Object plugin(Object target) {
            try {
                Object agency = target;
                if (target instanceof RoutingStatementHandler) {
                    agency = this.mappedDelegate.get(target);
                }

                if (agency instanceof BaseStatementHandler) {
                    MappedStatement mapper = (MappedStatement)this.mappedStatement.get(agency);
                    String id = mapper.getId();
                    String clazz = id.substring(0, id.lastIndexOf("."));
                    String name = id.substring(id.lastIndexOf(".") + 1);
                    MultipleDataSource.RoutingModel model = MultipleDataSource.this.resolveMethod(Class.forName(clazz), id, name);
                    String value = model.getDbase(id);
                    if (StringUtil.isEmpty(value)) {
                        if (MultipleDataSource.sourcesHolder.get() == null) {
                            throw new NullPointerException();
                        }
                    } else {
                        MultipleDataSource.refreshChain(false, value);
                    }
                }

                return Plugin.wrap(target, this);
            } catch (Exception var9) {
                throw new IllegalStateException(var9);
            }
        }
    }
}
