package com.easy.user.boot;

import com.easy.common.handler.HandlerInitializer;
import com.easy.common.transport.ServerPorts;
import com.easy.gateway.GatewayMotanRefererConfig;
import com.easy.user.UserMotanRefererConfig;
import com.weibo.api.motan.common.MotanConstants;
import com.weibo.api.motan.util.MotanSwitcherUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@ComponentScan(basePackages = "com.easy",
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = UserMotanRefererConfig.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = GatewayMotanRefererConfig.class)
        })
@EnableTransactionManagement
@SpringBootApplication
public class UserMain {
    private static final Logger logger = LoggerFactory.getLogger(UserMain.class);

    public static void main(String[] args) {
        SpringApplication.run(UserMain.class, args);
        MotanSwitcherUtil.setSwitcherValue(MotanConstants.REGISTRY_HEARTBEAT_SWITCHER, true);
    }

    @Bean
    public TomcatEmbeddedServletContainerFactory tomcatEmbeddedServletContainerFactory() {
        TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory(ServerPorts.userTomcatHttpPort.getPort());
        return factory;
    }

    @Bean
    public HandlerInitializer handlerInitializer() {
        HandlerInitializer initializer = new HandlerInitializer();
        return initializer;
    }
}
