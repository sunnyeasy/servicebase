package com.easy.game.gateway;

import com.easy.common.transport.ServerPorts;
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

@ComponentScan(basePackages = "com.easy")
@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MultipartAutoConfiguration.class, DataSourceAutoConfiguration.class})
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class GameGatewayMain {
    private static final Logger logger = LoggerFactory.getLogger(GameGatewayMain.class);

    private static ConfigurableApplicationContext context;

    public static void main(String[] args) {

        context = SpringApplication.run(GameGatewayMain.class, args);

        GameGatewayServer server = context.getBean(GameGatewayServer.class);
        server.init(context);

        logger.info("Game gateway server started...");
    }

    @Bean
    public TomcatEmbeddedServletContainerFactory tomcatEmbeddedServletContainerFactory() {
        TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory(ServerPorts.gameGatewayTomcatHttpPort.getPort());
        return factory;
    }
}
