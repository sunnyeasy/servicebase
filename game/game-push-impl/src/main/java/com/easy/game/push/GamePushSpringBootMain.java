package com.easy.game.push;

import com.easy.common.network.ServerPorts;
import com.easy.push.GamePushMotanRefererConfig;
import com.weibo.api.motan.common.MotanConstants;
import com.weibo.api.motan.util.MotanSwitcherUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.web.MultipartAutoConfiguration;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;

@ComponentScan(basePackages = "com.easy",
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = GamePushMotanRefererConfig.class)
        })
@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MultipartAutoConfiguration.class, DataSourceAutoConfiguration.class})
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class GamePushSpringBootMain {
    private static final Logger logger = LoggerFactory.getLogger(GamePushSpringBootMain.class);

    private static ConfigurableApplicationContext context;

    public static void main(String[] args) {
        context = SpringApplication.run(GamePushSpringBootMain.class, args);

        GamePushServer server = context.getBean(GamePushServer.class);
        server.init(context);

        MotanSwitcherUtil.setSwitcherValue(MotanConstants.REGISTRY_HEARTBEAT_SWITCHER, true);
        logger.info("Game gateway server started...");
    }

    @Bean
    public TomcatEmbeddedServletContainerFactory tomcatEmbeddedServletContainerFactory() {
        TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory(ServerPorts.gamePushHttpPort.getPort());
        return factory;
    }

}
