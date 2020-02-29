package com.easy.model.database.mysql;

import com.alibaba.druid.pool.DruidDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

import java.sql.SQLException;

public class DataSourceConfigFactory {
    public static final Logger logger= LoggerFactory.getLogger(DataSourceConfigFactory.class);

    public static DruidDataSource create(Environment environment) throws SQLException {
        DruidDataSource dataSource = new DruidDataSource();

        String v = environment.getProperty("druid.driverClassName");
        dataSource.setDriverClassName(v);

        v = environment.getProperty("druid.url");
        dataSource.setUrl(v);

        v = environment.getProperty("druid.username");
        dataSource.setUsername(v);

        v = environment.getProperty("druid.password");
        dataSource.setPassword(v);

        v = environment.getProperty("druid.filters");
        dataSource.setFilters(v);

        v = environment.getProperty("druid.initialSize");
        dataSource.setInitialSize(Integer.parseInt(v));

        v = environment.getProperty("druid.maxActive");
        dataSource.setMaxActive(Integer.parseInt(v));

        v = environment.getProperty("druid.minIdle");
        dataSource.setMinIdle(Integer.parseInt(v));

        v = environment.getProperty("druid.maxWait");
        dataSource.setMaxWait(Long.parseLong(v));

        v = environment.getProperty("druid.timeBetweenEvictionRunsMillis");
        dataSource.setTimeBetweenEvictionRunsMillis(Long.parseLong(v));

        v = environment.getProperty("druid.minEvictableIdleTimeMillis");
        dataSource.setMinEvictableIdleTimeMillis(Long.parseLong(v));

        v = environment.getProperty("druid.validationQuery");
        dataSource.setValidationQuery(v);

        v = environment.getProperty("druid.testOnBorrow");
        dataSource.setTestOnBorrow(Boolean.parseBoolean(v));

        v = environment.getProperty("druid.testOnReturn");
        dataSource.setTestOnReturn(Boolean.parseBoolean(v));

        v = environment.getProperty("druid.removeAbandoned");
        dataSource.setRemoveAbandoned(Boolean.parseBoolean(v));// 打开removeAbandoned功能

        v = environment.getProperty("druid.removeAbandonedTimeout");
        dataSource.setRemoveAbandonedTimeout(Integer.parseInt(v));

        v = environment.getProperty("druid.logAbandoned");
        dataSource.setLogAbandoned(Boolean.parseBoolean(v));// 关闭abanded连接时输出错误日志

        v = environment.getProperty("druid.testWhileIdle");
        dataSource.setTestWhileIdle(Boolean.parseBoolean(v));

        //dataSource.fill(1);//做数据库连接测试
        return dataSource;
    }
}
