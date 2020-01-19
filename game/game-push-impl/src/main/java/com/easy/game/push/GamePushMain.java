package com.easy.game.push;

import com.easy.common.container.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class GamePushMain {
    private static final Logger logger = LoggerFactory.getLogger(GamePushMain.class);

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath*:game_gate_way.xml");

        GamePushServer server = context.getBean(GamePushServer.class);
        server.init(context);

        logger.info("Game gateway server started...");
        Main.await("Game gateway server");
    }
}
