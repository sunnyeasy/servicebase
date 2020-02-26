package com.easy.gateway;

import com.easy.common.motan.MotanBeanConfig;
import com.easy.common.motan.MotanServers;
import com.easy.gateway.rpcapi.GatewayRpcService;
import com.easy.gateway.rpcapi.GatewayRpcServiceAsync;
import com.weibo.api.motan.config.springsupport.BasicRefererConfigBean;
import com.weibo.api.motan.config.springsupport.RefererConfigBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayMotanRefererConfig {
    @Bean
    public BasicRefererConfigBean userBasicRefererConfig() {
        return MotanBeanConfig.getBasicRefererConfigBean(MotanServers.groupName(MotanServers.user),
                MotanServers.user.name(), MotanServers.user.name());
    }

    @Bean
    public RefererConfigBean<GatewayRpcServiceAsync> userGatewayRpcService(@Qualifier("userBasicRefererConfig") BasicRefererConfigBean userBasicRefererConfig) {
        RefererConfigBean<GatewayRpcServiceAsync> bean = new RefererConfigBean<>();
        bean.setVersion(GatewayRpcServiceAsync.VERSION);
        bean.setInterface(GatewayRpcServiceAsync.class);
        bean.setBasicReferer(userBasicRefererConfig);
        bean.setRetries(0);
        return bean;
    }

    @Bean
    public BasicRefererConfigBean payBasicRefererConfig() {
        return MotanBeanConfig.getBasicRefererConfigBean(MotanServers.groupName(MotanServers.pay),
                MotanServers.pay.name(), MotanServers.pay.name());
    }

    @Bean
    public RefererConfigBean<GatewayRpcServiceAsync> payGatewayRpcService(@Qualifier("payBasicRefererConfig") BasicRefererConfigBean payBasicRefererConfig) {
        RefererConfigBean<GatewayRpcServiceAsync> bean = new RefererConfigBean<>();
        bean.setVersion(GatewayRpcServiceAsync.VERSION);
        bean.setInterface(GatewayRpcServiceAsync.class);
        bean.setBasicReferer(payBasicRefererConfig);
        bean.setRetries(0);
        return bean;
    }

    @Bean
    public GatewayMotanClient gatewayMotanClient(
            @Qualifier("userGatewayRpcService") GatewayRpcService userGatewayRpcService,
            @Qualifier("payGatewayRpcService") GatewayRpcService payGatewayRpcService) {
        GatewayMotanClient client = new GatewayMotanClient();
        client.put("/user", userGatewayRpcService);
        client.put("/pay", payGatewayRpcService);
        return client;
    }
}
