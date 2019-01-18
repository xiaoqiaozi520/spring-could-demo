//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cn.ymdd.framework.datasource;

import cn.ymdd.framework.datasource.MultipleDataSource.RoutingTransaction;
import cn.ymdd.framework.toolkit.util.StringUtil;
import com.alibaba.druid.pool.DruidDataSource;
import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.sql.DataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.transaction.PlatformTransactionManager;

abstract class SimpleDataSource extends DruidDataSource implements InitializingBean {
    private static final long serialVersionUID = 2187649553848341660L;
    public static final String MYSQL_PREFIX = "mysql";

    public SimpleDataSource() {
        DataSourceHelper.configDataSourceProperties(this);
    }

    @PostConstruct
    protected synchronized void initedDataSource() {
        if (!this.inited) {
            DataSourceHelper.configDataSourceArguments(this);
        }

    }

    @PreDestroy
    protected synchronized void closeDataSource() {
        if (this.inited) {
            this.close();
        }

    }

    @Bean
    @Primary
    @Autowired
    public PlatformTransactionManager initedTransactionManager(DataSource dataSource) {
        return new RoutingTransaction(dataSource);
    }

    @Bean
    @Primary
    @Autowired
    public SqlSessionFactoryBean initedSqlSessionFactoryBean(final DataSource dataSource) {
        return (new SqlSessionFactoryBean() {
            public SqlSessionFactoryBean init() {
                try {
                    this.setMapperLocations((new PathMatchingResourcePatternResolver()).getResources("classpath*:mappers/**/*.xml"));
                    this.setConfigLocation(new ClassPathResource("mybatis-config.xml"));
                    this.setDataSource(dataSource);
                    return this;
                } catch (IOException var2) {
                    throw new IllegalStateException(var2);
                }
            }
        }).init();
    }

    public void afterPropertiesSet() throws Exception {
        DataSourceHelper.checkDataSource(this);
    }

    public void setUsername(String username) {
        if (StringUtil.isEmpty(this.getUsername())) {
            super.setUsername(username);
        }

    }

    public void setPassword(String password) {
        if (StringUtil.isEmpty(this.getPassword())) {
            super.setPassword(password);
        }

    }

    public void setUrl(String url) {
        if (StringUtil.isEmpty(this.getUrl())) {
            super.setUrl(url);
        }

    }
}
