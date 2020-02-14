package com.easy.user.boot;

import com.easy.common.network.ServerPorts;
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

@ComponentScan(basePackages = "com.easy",
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = UserMotanRefererConfig.class)
        })
@SpringBootApplication
public class UserMain {
    private static final Logger logger = LoggerFactory.getLogger(UserMain.class);

    public static void main(String[] args) {
        SpringApplication.run(UserMain.class, args);
        MotanSwitcherUtil.setSwitcherValue(MotanConstants.REGISTRY_HEARTBEAT_SWITCHER, true);
    }

    @Bean
    public TomcatEmbeddedServletContainerFactory tomcatEmbeddedServletContainerFactory(){
        TomcatEmbeddedServletContainerFactory factory=new TomcatEmbeddedServletContainerFactory(ServerPorts.userHttpPort.getPort());
        return factory;
    }
}
