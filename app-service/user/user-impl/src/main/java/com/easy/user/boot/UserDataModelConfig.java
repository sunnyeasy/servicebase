package com.easy.user.boot;

import com.alibaba.druid.pool.DruidDataSource;
import com.easy.model.database.mysql.DataSourceConfigFactory;
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
        return DataSourceConfigFactory.create(environment);
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
