package com.easy.pay.boot;

import com.easy.common.motan.MotanBeanConfig;
import com.easy.common.motan.MotanServers;
import com.easy.common.transport.ServerPorts;
import com.easy.gateway.rpcapi.GatewayRpcService;
import com.weibo.api.motan.config.springsupport.BasicServiceConfigBean;
import com.weibo.api.motan.config.springsupport.ServiceConfigBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PayMotanServiceConfig {

    @Bean
    public BasicServiceConfigBean basicServiceConfig() {
        BasicServiceConfigBean bean = new BasicServiceConfigBean();
        bean.setExport(MotanBeanConfig.MOTAN_RPC_SERVERPROTOCOL_NAME + ":" + ServerPorts.payMotanPort.getPort());
        bean.setGroup(MotanServers.groupName(MotanServers.pay));
        bean.setAccessLog(false);
        bean.setShareChannel(true);
        bean.setModule(MotanServers.pay.name());
        bean.setApplication(MotanServers.pay.name());
        bean.setRegistry(MotanBeanConfig.MOTAN_RPC_REGISTRY_NAME);
        return bean;
    }

    @Bean
    public ServiceConfigBean<GatewayRpcService> gatewayRpcService(@Autowired GatewayRpcService gatewayRpcService) {
        ServiceConfigBean<GatewayRpcService> bean = new ServiceConfigBean<>();
        bean.setInterface(GatewayRpcService.class);
        bean.setVersion(GatewayRpcService.VERSION);
        bean.setRef(gatewayRpcService);
//        bean.setFilter("serverExceptionFilter");
        return bean;
    }
}
