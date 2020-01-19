//package com.easy.game.gateway;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
//import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
//import org.springframework.boot.autoconfigure.web.MultipartAutoConfiguration;
//import org.springframework.context.ConfigurableApplicationContext;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.EnableAspectJAutoProxy;
//
//import java.util.concurrent.locks.Condition;
//import java.util.concurrent.locks.ReentrantLock;
//
//@ComponentScan(basePackages = {"com.easy"})
//@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MultipartAutoConfiguration.class, DataSourceAutoConfiguration.class})
//@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
//public class GameGateWaySpringBootMain {
//    private static final Logger logger = LoggerFactory.getLogger(GameGateWaySpringBootMain.class);
//
//    private static final ReentrantLock LOCK = new ReentrantLock();
//
//    private static final Condition STOP = LOCK.newCondition();
//
//    private static ConfigurableApplicationContext context;
//
//    public static void main(String[] args) {
//        context = SpringApplication.run(GameGateWaySpringBootMain.class, args);
//
//        GameGateWayServer gameGateWayServer = context.getBean(GameGateWayServer.class);
//        gameGateWayServer.init(context);
//
//        logger.info("Game gateway server started...");
//    }
//}
