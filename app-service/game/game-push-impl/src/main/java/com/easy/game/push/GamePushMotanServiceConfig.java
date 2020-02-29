package com.easy.game.push;

import com.easy.common.motan.MotanBeanConfig;
import com.easy.common.motan.MotanServers;
import com.easy.common.transport.ServerPorts;
import com.easy.push.rpcapi.GamePushRpcService;
import com.weibo.api.motan.config.springsupport.BasicServiceConfigBean;
import com.weibo.api.motan.config.springsupport.ServiceConfigBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GamePushMotanServiceConfig {
    @Bean
    public BasicServiceConfigBean basicServiceConfig() {
        BasicServiceConfigBean bean = new BasicServiceConfigBean();
        bean.setExport(MotanBeanConfig.MOTAN_RPC_SERVERPROTOCOL_NAME + ":" + ServerPorts.gamePushMotanPort.getPort());
        bean.setGroup(MotanServers.groupName(MotanServers.gamePush));
        bean.setAccessLog(false);
        bean.setShareChannel(true);
        bean.setModule(MotanServers.gamePush.name());
        bean.setApplication(MotanServers.gamePush.name());
        bean.setRegistry(MotanBeanConfig.MOTAN_RPC_REGISTRY_NAME);
        return bean;
    }

    @Bean
    public ServiceConfigBean<GamePushRpcService> gamePushRpcService(@Autowired GamePushRpcService gamePushRpcService) {
        ServiceConfigBean<GamePushRpcService> bean = new ServiceConfigBean<>();
        bean.setInterface(GamePushRpcService.class);
        bean.setVersion(GamePushRpcService.VERSION);
        bean.setRef(gamePushRpcService);
//        bean.setFilter("serverExceptionFilter");
        return bean;
    }
}
