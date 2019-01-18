//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cn.ymdd.framework.datasource;

import javax.sql.DataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnMissingBean({DataSource.class})
@AutoConfigureBefore({DataSourceAutoConfiguration.class})
public class DataSourceConfigurator {
    private static final Logger log = LoggerFactory.getLogger(DataSourceConfigurator.class);

    public DataSourceConfigurator() {
    }

    @Configuration("sqlSessionTemplate")
    @ConditionalOnMissingBean({SqlSessionTemplate.class})
    protected class SqlSessionTemplateConfigurator extends SqlSessionTemplate {
        public SqlSessionTemplateConfigurator(SqlSessionFactoryBean factory) {
            super(DataSourceHelper.createSqlSessionFactory(factory));
        }
    }

    @ConfigurationProperties(
            prefix = "mysql"
    )
    @MapperScan(
            basePackages = {"cn.guludai.*.domain.dao", "cn.guludai.*.domain.*.dao"}
    )
    @ConditionalOnProperty(
            prefix = "mysql",
            name = {"type"},
            havingValue = "multiple"
    )
    protected class MultipleDataSourceConfigurator extends MultipleDataSource {
        public MultipleDataSourceConfigurator() {
            if (DataSourceConfigurator.log.isInfoEnabled()) {
                DataSourceConfigurator.log.info("[ GULUDAI ] Inited multiple datasource configurator...");
            }

        }
    }

    @ConditionalOnMissingBean({MultipleDataSource.class})
    @ConfigurationProperties(
            prefix = "mysql"
    )
    @MapperScan(
            basePackages = {"cn.guludai.*.domain.dao", "cn.guludai.*.domain.*.dao"}
    )
    @ConditionalOnProperty(
            prefix = "mysql",
            name = {"username", "password", "url"}
    )
    protected class SimpleDataSourceConfigurator extends SimpleDataSource {
        private static final long serialVersionUID = -8578849211617865069L;

        public SimpleDataSourceConfigurator() {
            if (DataSourceConfigurator.log.isInfoEnabled()) {
                DataSourceConfigurator.log.info("[ GULUDAI ] Inited simple datasource configurator...");
            }

        }
    }
}
