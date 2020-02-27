package com.easy.user.boot;

import com.alibaba.druid.pool.DruidDataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

@MapperScan("com.easy.user.model.mysql.dao")
@Configuration
public class UserDataModelConfig {
    private static final Logger logger = LoggerFactory.getLogger(UserDataModelConfig.class);

    @Autowired
    private Environment environment;

//    public RedisClient redisClient() {
//
//    }

    @Bean
    public DruidDataSource dataSource() throws SQLException {
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

    @Bean
    public SqlSessionFactoryBean sqlSessionFactory(@Autowired DruidDataSource dataSource) throws IOException {
        SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSource);

        //spring获取classpath下通配的文件列表解析器
        ResourcePatternResolver resourceLoader = new PathMatchingResourcePatternResolver();
        Resource[] mapperReources = resourceLoader.getResources("com/easy/user/model/mysql/mapper/**/*.xml");
        sqlSessionFactory.setMapperLocations(mapperReources);

//        Interceptor[] plugins = new Interceptor[1];
//        plugins[0] = new DataPermissionsInterceptor();
//        sqlSessionFactory.setPlugins(plugins);
        return sqlSessionFactory;
    }

    @Bean
    public DataSourceTransactionManager transactionManager(@Autowired DruidDataSource dataSource) {
        DataSourceTransactionManager bean = new DataSourceTransactionManager();
        bean.setDataSource(dataSource);
        return bean;
    }

    // 创建事务通知
    @Bean
    public TransactionInterceptor txAdvice(@Autowired DataSourceTransactionManager transactionManager) throws Exception {
        Properties properties = new Properties();
		properties.setProperty("*WithTX", "PROPAGATION_REQUIRED,-Throwable");
        TransactionInterceptor tsi = new TransactionInterceptor(transactionManager, properties);
        return tsi;
    }

    @Bean
    public Advisor driverAdvisor(@Autowired TransactionInterceptor txAdvice) {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(* com.easy.user.service..*Impl.*(..))");
        return new DefaultPointcutAdvisor(pointcut, txAdvice);
    }
}
