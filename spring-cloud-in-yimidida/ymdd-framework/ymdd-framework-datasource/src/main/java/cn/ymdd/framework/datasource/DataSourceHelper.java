//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cn.ymdd.framework.datasource;

import cn.ymdd.framework.toolkit.util.StringUtil;
import com.alibaba.druid.pool.DruidDataSource;
import java.sql.SQLException;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class DataSourceHelper {
    public static final int DEFAULT_MAX_ACTIVE = 50;
    public static final int DEFAULT_MIN_IDEL = 5;
    public static final int DEFAULT_INIT_SIZE = 1;
    private static final Logger log = LoggerFactory.getLogger(DataSourceHelper.class);

    DataSourceHelper() {
    }

    static void checkDataSource(DruidDataSource datasource) {
        if (StringUtil.isEmpty(datasource.getUsername())) {
            throw new NullPointerException("username");
        } else if (StringUtil.isEmpty(datasource.getPassword())) {
            throw new NullPointerException("password");
        } else if (StringUtil.isEmpty(datasource.getUrl())) {
            throw new NullPointerException("url");
        }
    }

    static void configDataSourceProperties(DruidDataSource datasource) {
        if (datasource.isInited()) {
            throw new IllegalStateException("Datasource inited");
        } else {
            datasource.addConnectionProperty("zeroDateTimeBehavior", "convertToNull");
            datasource.addConnectionProperty("characterEncoding", "UTF-8");
            datasource.addConnectionProperty("autoReconnect", "true");
            datasource.addConnectionProperty("useUnicode", "true");
        }
    }

    static void configDataSourceArguments(DruidDataSource datasource) {
        if (datasource.isInited()) {
            throw new IllegalStateException("Datasource inited");
        } else {
            try {
                if (datasource.getMinIdle() > 50 || datasource.getMinIdle() <= 1) {
                    datasource.setMinIdle(5);
                }

                if (datasource.getMaxActive() > 50) {
                    datasource.setMaxActive(50);
                }

                datasource.setInitialSize(1);
                datasource.setMaxWait(10000L);
                datasource.setMaxOpenPreparedStatements(250);
                datasource.setMinEvictableIdleTimeMillis(300000L);
                datasource.setTimeBetweenEvictionRunsMillis(60000L);
                datasource.setTestWhileIdle(true);
                datasource.setTestOnReturn(false);
                datasource.setTestOnBorrow(false);
                datasource.setLogAbandoned(false);
                datasource.setRemoveAbandoned(true);
                datasource.setRemoveAbandonedTimeout(25200);
                datasource.setDriverClassName("com.mysql.jdbc.Driver");
                datasource.setValidationQuery("select 'x'");
                datasource.setFilters("stat");
            } catch (SQLException var2) {
                if (log.isErrorEnabled()) {
                    log.error(var2.getMessage(), var2);
                }
            }

        }
    }

    static SqlSessionFactory createSqlSessionFactory(SqlSessionFactoryBean factory) {
        try {
            return factory.getObject();
        } catch (Exception var2) {
            if (log.isErrorEnabled()) {
                log.error(var2.getMessage(), var2);
            }

            throw new IllegalStateException(var2);
        }
    }
}
