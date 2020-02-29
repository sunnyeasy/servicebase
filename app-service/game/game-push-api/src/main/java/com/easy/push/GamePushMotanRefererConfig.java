package com.easy.push;

import com.easy.common.motan.MotanBeanConfig;
import com.easy.common.motan.MotanServers;
import com.easy.push.rpcapi.GamePushRpcService;
import com.weibo.api.motan.config.springsupport.BasicRefererConfigBean;
import com.weibo.api.motan.config.springsupport.RefererConfigBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GamePushMotanRefererConfig {
    @Bean
    public BasicRefererConfigBean gamePushBasicRefererConfig() {
        return MotanBeanConfig.getBasicRefererConfigBean(MotanServers.groupName(MotanServers.gamePush),
                MotanServers.gamePush.name(), MotanServers.gamePush.name());
    }

    @Bean
    public RefererConfigBean<GamePushRpcService> gamePushRpcService(@Qualifier("gamePushBasicRefererConfig") BasicRefererConfigBean gamePushBasicRefererConfig) {
        RefererConfigBean<GamePushRpcService> bean = new RefererConfigBean<>();
        bean.setVersion(GamePushRpcService.VERSION);
        bean.setInterface(GamePushRpcService.class);
        bean.setBasicReferer(gamePushBasicRefererConfig);
        bean.setRetries(0);
        return bean;
    }
}
