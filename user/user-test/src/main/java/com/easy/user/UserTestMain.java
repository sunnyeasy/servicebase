package com.easy.user;

import com.alibaba.fastjson.JSONObject;
import com.easy.common.container.Main;
import com.easy.common.network.ServerPorts;
import com.easy.common.rpcao.AuthRpcAo;
import com.easy.common.rpcvo.AuthRpcVo;
import com.easy.constant.enums.AgentMode;
import com.easy.constant.enums.BusinessType;
import com.easy.user.rpcapi.PushAuthRpcServiceAsync;
import com.weibo.api.motan.common.MotanConstants;
import com.weibo.api.motan.rpc.Future;
import com.weibo.api.motan.rpc.FutureListener;
import com.weibo.api.motan.rpc.ResponseFuture;
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
public class UserTestMain {
    private static final Logger logger = LoggerFactory.getLogger(UserTestMain.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(UserTestMain.class, args);
        MotanSwitcherUtil.setSwitcherValue(MotanConstants.REGISTRY_HEARTBEAT_SWITCHER, true);

        PushAuthRpcServiceAsync bean = applicationContext.getBean(PushAuthRpcServiceAsync.class);

        AuthRpcAo ao = new AuthRpcAo();
        ao.setAgentMode(AgentMode.ANDRIOD);
        ao.setBusinessType(BusinessType.GAME);
        ao.setDeviceId(String.valueOf(System.currentTimeMillis()));

        logger.info("startTime={}", System.currentTimeMillis());
        ResponseFuture future = bean.authAsync(ao);

        FutureListener listener = new FutureListener() {
            @Override
            public void operationComplete(Future future) throws Exception {
                logger.error("endTime={}, isSuccess={}", System.currentTimeMillis(), future.isSuccess());
                if (future.isSuccess()) {
                    AuthRpcVo auth = (AuthRpcVo) future.getValue();
                    logger.info("endTime={},auth={}", System.currentTimeMillis(), JSONObject.toJSON(auth));
                }
            }
        };
        future.addListener(listener);

        logger.info("wait response");
        Main.await("userTest");
    }

    @Bean
    public TomcatEmbeddedServletContainerFactory tomcatEmbeddedServletContainerFactory() {
        TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory(ServerPorts.userTestHttpPort.getPort());
        return factory;
    }
}
