package com.easy.game.push.test;

import com.alibaba.fastjson.JSON;
import com.easy.common.transport.ServerPorts;
import com.easy.common.transport.packet.push.RpcPushRequest;
import com.easy.common.rpcao.AuthRpcAo;
import com.easy.common.rpcvo.BaseRpcVo;
import com.easy.constant.enums.AgentMode;
import com.easy.constant.enums.BusinessType;
import com.easy.push.rpcapi.GamePushRpcService;
import com.weibo.api.motan.common.MotanConstants;
import com.weibo.api.motan.util.MotanSwitcherUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@ComponentScan(basePackages = "com.easy")
@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class GamePushTestMain {
    private static final Logger logger = LoggerFactory.getLogger(GamePushTestMain.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(GamePushTestMain.class, args);
        MotanSwitcherUtil.setSwitcherValue(MotanConstants.REGISTRY_HEARTBEAT_SWITCHER, true);

        GamePushRpcService bean = applicationContext.getBean(GamePushRpcService.class);

        AuthRpcAo ao = new AuthRpcAo();
        ao.setAgentMode(AgentMode.ANDRIOD);
        ao.setBusinessType(BusinessType.GAME);
        ao.setDeviceId(String.valueOf(System.currentTimeMillis()));

        logger.info("startTime={}", System.currentTimeMillis());

        RpcPushRequest request = new RpcPushRequest();
        request.setUid(1000000L);
        request.setData("hello easyfun," + System.currentTimeMillis());
        BaseRpcVo rpcVo = bean.push(request);
        logger.info("rpcVo={}", JSON.toJSONString(rpcVo));
    }

    @Bean
    public TomcatEmbeddedServletContainerFactory tomcatEmbeddedServletContainerFactory() {
        TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory(ServerPorts.gamePushTestTomcatHttpPort.getPort());
        return factory;
    }
}
